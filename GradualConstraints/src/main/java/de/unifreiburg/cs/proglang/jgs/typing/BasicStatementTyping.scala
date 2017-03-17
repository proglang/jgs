package de.unifreiburg.cs.proglang.jgs.typing

import de.unifreiburg.cs.proglang.jgs
import de.unifreiburg.cs.proglang.jgs.constraints.CTypes.{literal, variable}
import de.unifreiburg.cs.proglang.jgs.constraints.TypeViews.TypeView
import de.unifreiburg.cs.proglang.jgs.constraints.{CTypes, TypeDomain, TypeVars, _}
import de.unifreiburg.cs.proglang.jgs.instrumentation.{ACasts, CastUtils, Var}
import de.unifreiburg.cs.proglang.jgs.jimpleutils._
import de.unifreiburg.cs.proglang.jgs.signatures.Effects.emptyEffect
import de.unifreiburg.cs.proglang.jgs.signatures.{Effects, FieldTable, Param, Return, Signature, SignatureTable}
import de.unifreiburg.cs.proglang.jgs.signatures.Symbol._
import de.unifreiburg.cs.proglang.jgs.typing.BodyTypingResult.{makeResult, trivialWithEnv}
import de.unifreiburg.cs.proglang.jgs.util.NotImplemented
import soot._
import soot.jimple._
import soot.toolkits.scalar.LocalDefs

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * A context for typing basic statements (assignments, method calls,
  * instantiations... everything except branching and sequencing).
  *
  * @param < LevelT> The type of security levels.
  * @author fennell
  */
class BasicStatementTyping[LevelT](
                                    val csets: ConstraintSetFactory[LevelT],
                                    val tvars: TypeVars.MethodTypeVars,
                                    val cstrs: Constraints[LevelT],
                                    val currentMethod: SootMethod
                                  ) {
  def generate(s: Stmt, localDefs: LocalDefs,
               env: Environment,
               pc: java.util.Set[TypeVars.TypeVar],
               signatures: SignatureTable[LevelT],
               fields: FieldTable[LevelT], casts: ACasts[LevelT]): BodyTypingResult[LevelT] = {
    val g: Gen = new Gen(env, pc.toSet, signatures, fields, casts, localDefs)
    s.apply(g)
    if (!g.getErrorMsg.isEmpty) {
      throw new TypingException("There where errors during statement typing: \n" + g.getErrorMsg.mkString("\n"))
    }
    return g.getResult
  }


  /**
    * A statement switch that generates typing constraints.
    *
    * @author Luminous Fennell
    */
  class Gen(
             private val env: Environment,
             private val pcs: Set[TypeVars.TypeVar],
             private val signatures: SignatureTable[LevelT],
             private val fields: FieldTable[LevelT],
             private val casts: ACasts[LevelT],
             private val localDefs: LocalDefs
           ) extends AbstractStmtSwitch {

    private val errorMsg: ListBuffer[String] = ListBuffer[String]()

    def getErrorMsg: List[String] = errorMsg.toList



    private def extractEffects(rhs: Value): Effects[LevelT] = {
      val effectCases: RhsSwitch[LevelT] = new RhsSwitch[LevelT]((casts)) {
        def caseLocalExpr(atoms: java.util.Collection[Value]) {
          setResult(emptyEffect)
        }

        def caseCall(m: SootMethod, thisPtr: Option[Var[_]], args: java.util.List[Option[Var[_]]]) {
          setResult(getSignature(m).effects)
        }

        def caseGetField(field: FieldRef, thisPtr: Option[Var[_]]) {
          setResult(emptyEffect)
        }

        def caseCast(cast: ACasts.ValueCast[LevelT]) {
          setResult(emptyEffect)
        }

        def caseNew(`type`: RefType) {
          setResult(emptyEffect)
        }

        def caseConstant(v: Value) {
          setResult(emptyEffect)
        }

        def caseFail(e : Throwable) {
          throw new TypingAssertionFailure(s"Error analysing rhs for effects: ${rhs}\n  ${e.getMessage}")
        }

        override def defaultCase(v: AnyRef) {
          throw new NotImplemented("Effect extraction not implemented for case: " + v.toString)
        }
      }
      rhs.apply(effectCases)
      return effectCases.getResult.asInstanceOf[Effects[LevelT]]
    }

    private def getSignature(m: SootMethod): Signature[LevelT] = {
      val maybe_Result: Option[Signature[LevelT]] = signatures.get(m)
      if (maybe_Result.isEmpty) {
        throw new TypingAssertionFailure("No signature found for method " + m.toString)
      }
      return maybe_Result.get
    }

    private def getFieldType(f: SootField): TypeView[LevelT] = {
      return fields.get(f).getOrElse(
        throw new TypingAssertionFailure(
          "No field type found for field "
            + f.toString()))
    }

    private class ExprSwitch(
                              private val leDest: CTypes.CType[LevelT] => Constraint[LevelT],
                              private val toCType: Var[_] => CTypes.CType[LevelT],
                              private val constraints: ListBuffer[Constraint[LevelT]],
                              private val destTVar: TypeVars.TypeVar,
                              private val destCType: CTypes.CType[LevelT]
                            )
      extends RhsSwitch[LevelT](casts) {

      private var tags: TagMap[LevelT] = TagMap.empty[LevelT]

      def getTags: TagMap[LevelT] = {
        return tags
      }

      def caseLocalExpr(atoms: java.util.Collection[Value]) {
        Vars.getAllFromValues(atoms).map(toCType.andThen(leDest)).foreach(constraints += _)
      }

      def caseCall(m: SootMethod, thisPtr: Option[Var[_]], args: java.util.List[Option[Var[_]]]) {
        val argCount: Int = args.size
        val paramterCount: Int = m.getParameterCount
        if (argCount != paramterCount) {
          throw new RuntimeException(s"Argument count ($argCount) does not " + s"equal parameter count ($paramterCount): ${m.toString}")
        }
        val sig: Signature[LevelT] = getSignature(m)
        val instantiation: mutable.HashMap[jgs.signatures.Symbol[LevelT], CTypes.CType[LevelT]] = mutable.HashMap()
        val argTypes: List[Option[TypeVars.TypeVar]] = args.map(mv => mv.map(env.get)).toList
        (0 until argCount).foreach(i => {
          val mat = argTypes.get(i);
          mat.foreach(at => {
            instantiation.put(jgs.signatures.Symbol.param(i), variable(at));
          });
          if (!mat.isDefined) {
            instantiation.put(jgs.signatures.Symbol.param(i), literal(cstrs.types.pub));
          }
        })
        instantiation += jgs.signatures.Symbol.ret[LevelT] -> variable(destTVar)
        val tagMap: mutable.Map[Constraint[LevelT], TypeVarTags.TypeVarTag] = mutable.HashMap()
        sig.constraints.stream.foreach(sc => {
          val c = sc.toTypingConstraint(instantiation);
          val params =
            sc.symbols().iterator().filter(s => s.isInstanceOf[Param[LevelT]])
              .map(s => new TypeVarTags.MethodArg(m, (s.asInstanceOf[(Param[LevelT])]).position));
          val rets =
            sc.symbols().iterator().filter(s => s.isInstanceOf[Return[LevelT]])
              .map(s => new TypeVarTags.MethodReturn(m));
          constraints.add(c);
          (params ++ rets).foreach(t => tagMap += c -> t);
        })
        tags = TagMap.of(tagMap)
        thisPtr.map(toCType.andThen(leDest)).foreach(constraints += _)
        sig.effects.iterator().foreach(t => {
          pcs.foreach(pc => {
            constraints += (Constraints.le(variable(pc), literal(t)));
          });
        })
      }

      def caseGetField(field: FieldRef, thisPtr: Option[Var[_]]) {
        val destC: Constraint[LevelT] = leDest.apply(literal(getFieldType(field.getField)))
        tags = TagMap.of(destC, new TypeVarTags.Field(field.getField))
        (Iterator(destC) ++ thisPtr.map(tp => leDest.apply(variable(env.get(thisPtr.get)))).iterator).foreach(constraints += _)
      }

      def caseCast(cast: ACasts.ValueCast[LevelT]) {
        if (!compatible(cast.sourceType, cast.destType)) {
          errorMsg.add(String.format("Source type %s cannot be converted to destination type %s.", cast.sourceType, cast.destType))
          return
        }
        val mcstr: Option[Constraint[LevelT]] = (cast.value).map(v => Constraints.le(toCType.apply(v), literal(cast.sourceType)))
        mcstr.foreach(constraints += _)
        val destC: Constraint[LevelT] = Constraints.le(literal(cast.destType), destCType)
        constraints.add(destC)
        val conv: CastUtils.TypeViewConversion[LevelT] = new CastUtils.TypeViewConversion[LevelT](cast.sourceType, cast.destType)
        val tag: TypeVarTags.TypeVarTag = new TypeVarTags.Cast(conv)
        tags = TagMap.of(destC, tag).addAll(mcstr.map(c => TagMap.of(c, tag)).getOrElse(TagMap.empty[LevelT]))
      }

      def caseFail(e : Throwable) {
        errorMsg.add(e.getMessage)
      }

      def caseNew(`type`: RefType) {
      }

      def caseConstant(v: Value) {
      }
    }

    private def caseLocalDefinition(writeVar: Local, stmt: DefinitionStmt) {
      val constraints: ListBuffer[Constraint[LevelT]] = ListBuffer()
      val destTVar: TypeVars.TypeVar = tvars.forLocal(Vars.fromLocal(writeVar), stmt)
      val destCType: CTypes.CType[LevelT] = variable(destTVar)
      val leDest: Function[CTypes.CType[LevelT], Constraint[LevelT]] = ct => Constraints.le(ct, destCType)
      val toCType: Function[Var[_], CTypes.CType[LevelT]] = v => variable(env.get(v))
      val rhs: Value = stmt.getRightOp
      val sw  = new ExprSwitch(leDest, toCType, constraints, destTVar, destCType)
      rhs.apply(sw)
      this.pcs.foreach(pc => {
        constraints += (leDest.apply(variable(pc)));
      })
      val fin: Environment = env.add(Vars.fromLocal(writeVar), destTVar)
      setResult(makeResult(stmt, csets.fromCollection(constraints), pcs, env, fin, extractEffects(rhs), sw.getTags))
    }

    private def caseFieldDefinition(fieldRef: FieldRef, stmt: DefinitionStmt) {
      val field: SootField = fieldRef.getField
      val constraints: ListBuffer[Constraint[LevelT]] = ListBuffer()
      val fieldType: TypeView[LevelT] = getFieldType(field)
      val leDest: Function[CTypes.CType[LevelT], Constraint[LevelT]] = c => Constraints.le(c, CTypes.literal(fieldType))
      Vars.getAllFromValueBoxes(stmt.getLeftOp.getUseBoxes.asInstanceOf[java.util.Collection[ValueBox]]).map(v => Constraints.le(CTypes.variable(env.get(v)), CTypes.literal(fieldType))).foreach(constraints += _)
      val tags: TagMap[LevelT] = if ((stmt.getRightOp.isInstanceOf[Local])) {
        val rhs: Local = stmt.getRightOp.asInstanceOf[Local]
        val cstr: Constraint[LevelT] = leDest.apply(CTypes.variable(env.get(Vars.fromLocal(rhs))))

        constraints.add(cstr)
        TagMap.of(cstr, new TypeVarTags.Field(field))
      }
      else if (stmt.getRightOp.isInstanceOf[Constant]) {
        TagMap.empty[LevelT]
      }
      else {
        throw new TypingAssertionFailure(s"""Only field updates of the form \"x.F = y\" of \"x.F = c\" are supported. Found ${stmt}""")
      }
      pcs.foreach(v => constraints.add(leDest.apply(CTypes.variable(v))))
      val effects: Effects[LevelT] = if ((currentMethod.getName == "<init>") && fieldRef.isInstanceOf[InstanceFieldRef]) {
        val base: Value = (fieldRef.asInstanceOf[InstanceFieldRef]).getBase
        if (base.isInstanceOf[ThisRef]) {
          emptyEffect[LevelT]
        } else if (base.isInstanceOf[Local]) {
          val baseDefs: List[Unit] = localDefs.getDefsOfAt(base.asInstanceOf[Local], stmt).toList
          if (baseDefs.size == 1) {
            val baseDef: Stmt = baseDefs.get(0).asInstanceOf[Stmt]
            if (baseDef.isInstanceOf[IdentityStmt] && (baseDef.asInstanceOf[IdentityStmt]).getRightOp.isInstanceOf[ThisRef]) {
              emptyEffect[LevelT]
            }
            else {
              Effects.emptyEffect[LevelT].add(fieldType)
            }
          }
          else {
            Effects.emptyEffect[LevelT].add(fieldType)
          }
        }
        else {
          Effects.emptyEffect[LevelT].add(fieldType)
        }
      }
      else if ((currentMethod.getName == "<clinit>") && fieldRef.isInstanceOf[StaticFieldRef] && (field.getDeclaringClass == currentMethod.getDeclaringClass)) {
        Effects.emptyEffect[LevelT]
      }
      else {
        Effects.emptyEffect[LevelT].add(fieldType)
      }
      setResult(makeResult(stmt, csets.fromCollection(constraints), pcs, env, env, effects, tags))
    }

    private def caseDefinitionStmt(stmt: DefinitionStmt) {
      val lhs: Value = stmt.getLeftOp
      if (lhs.isInstanceOf[Local]) {
        caseLocalDefinition((lhs.asInstanceOf[Local]), stmt)
      }
      else if (lhs.isInstanceOf[FieldRef]) {
        caseFieldDefinition((lhs.asInstanceOf[FieldRef]), stmt)
      }
      else {
        throw new TypingAssertionFailure("Extracting write locations for statement " + stmt + " is not supported yet.")
      }
    }

    override def caseAssignStmt(stmt: AssignStmt) {
      caseDefinitionStmt(stmt)
    }

    private def compatible(sourceType: TypeView[LevelT], destType: TypeView[LevelT]): Boolean = {
      return (cstrs.types.dyn == destType) ^ (cstrs.types.dyn == sourceType)
    }

    private def noRestrictions(s : Stmt) {
      setResult(trivialWithEnv(s, csets, pcs, env))
    }

    override def caseIdentityStmt(stmt: IdentityStmt) {
      this.caseDefinitionStmt(stmt)
    }

    override def caseNopStmt(stmt: NopStmt) {
      noRestrictions(stmt)
    }

    override def caseGotoStmt(stmt: GotoStmt) {
      noRestrictions(stmt)
    }

    override def caseReturnStmt(stmt: ReturnStmt) {
      if (stmt.getOp.isInstanceOf[Local]) {
        val r: Var[_] = Vars.fromLocal(stmt.getOp.asInstanceOf[Local])
        setResult(makeResult(stmt, csets.fromCollection((Iterator(Constraints.le[LevelT](variable(env.get(r)), variable(tvars.ret))) ++
          this.pcs.iterator.map(pcVar => Constraints.le[LevelT](variable(pcVar),
            variable(tvars.ret())))).toList.asJavaCollection),
          pcs,
          env, env,
          emptyEffect[LevelT],
          TagMap.empty[LevelT]))
      }
      else if (stmt.getOp.isInstanceOf[Constant]) {
        noRestrictions(stmt)
      }
      else {
        throw new RuntimeException("Did not expect to return a " + stmt.getOp.getClass)
      }
    }

    override def caseInvokeStmt(stmt: InvokeStmt) {
      val e: Value = stmt.getInvokeExpr
      val constraints: ListBuffer[Constraint[LevelT]] = ListBuffer()
      val dummy: Local = Jimple.v.newLocal("DUMMY_FOR_INVOKE_STMT", null)
      val destTVar: TypeVars.TypeVar = tvars.forLocal(Vars.fromLocal(dummy), stmt)
      val destCType: CTypes.CType[LevelT] = variable(destTVar)
      val leDest: Function[CTypes.CType[LevelT], Constraint[LevelT]] = t => Constraints.le(t, destCType)
      val toCType: Function[Var[_], CTypes.CType[LevelT]] = v => variable(env.get(v))
      val sw: BasicStatementTyping[LevelT]#Gen#ExprSwitch = new ExprSwitch(leDest, toCType, constraints, destTVar, destCType)
      e.apply(sw)
      setResult(makeResult(stmt, csets.fromCollection(constraints), pcs, env, env, extractEffects(e), sw.getTags))
    }

    // Cast for *trivial* if statements (that is, if-statements with a single successor;
    //   they are considered basic statemetn and can be treated as a noop)
    override def caseIfStmt(stmt: IfStmt) {
      noRestrictions(stmt)
    }

    override def caseReturnVoidStmt(stmt: ReturnVoidStmt) {
      noRestrictions(stmt)
    }

    override def defaultCase(obj: AnyRef) {
      throw new NotImplemented("Typing basic statement $obj is not supported yet")
    }

    override def getResult: BodyTypingResult[LevelT] = {
      return super.getResult.asInstanceOf[BodyTypingResult[LevelT]]
    }
  }

}