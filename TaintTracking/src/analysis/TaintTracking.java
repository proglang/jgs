package analysis;

import java.util.ArrayList;
import java.util.List;
import logging.SecurityLogger;
import model.LocalMap;
import model.SecurityField;
import model.SecurityMethod;
import model.SecurityMethod.*;

import security.LevelEquation;
import security.LevelEquationVisitor.*;
import security.SecurityAnnotation;
import soot.Local;
import soot.SootMethod;
import soot.Value;
import soot.Unit;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.ParameterRef;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.jimple.internal.ImmediateBox;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JCastExpr;
import soot.jimple.internal.JGotoStmt;
import soot.jimple.internal.JIdentityStmt;
import soot.jimple.internal.JIfStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JNopStmt;
import soot.jimple.internal.JReturnStmt;
import soot.jimple.internal.JReturnVoidStmt;
import soot.jimple.internal.JVirtualInvokeExpr;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import utils.SootUtils;


/**
 * This analysis tracks the high-security variables.
 * @author fennell
 *
 */
public class TaintTracking extends ForwardFlowAnalysis<Unit, LocalMap> {
	private static final String CLINIT_MEHTOD_NAME = "<clinit>";
	private static final String INIT_MEHTOD_NAME = "<init>";
	private SecurityAnnotation securityAnnotation = null;
	private SecurityMethod securityMethod = null;
	private SecurityLogger log;
	//int i = 0;

	public TaintTracking(SecurityLogger log, SootMethod sootMethod, SecurityAnnotation securityAnnotations, DirectedGraph<Unit> graph) {
		super(graph);
		System.err.println("[" + sootMethod.getName() + "] - TaintTracking");
		LocalMap.number = 0;
		this.log = log;
		this.securityMethod = new SecurityMethod(sootMethod, true);
		this.securityAnnotation = securityAnnotations;
		if (sootMethod.getName().equals(CLINIT_MEHTOD_NAME)) {
			log.debug(CLINIT_MEHTOD_NAME + " ???");
			// TODO: Check whether level of variable which will be assigned to field is weaker
		} else if (sootMethod.getName().equals(INIT_MEHTOD_NAME)) {
			log.debug(INIT_MEHTOD_NAME + " ???");
			// ??? same as normal methods but no return level ???
			// TODO: Check whether level of variable which will be assigned to field is weaker
		} else {
			long sourceLine = 0;
			if (this.securityMethod.areMethodParameterSecuritiesValid(securityAnnotation, log)) {
				if (this.securityMethod.isReturnSecurityValid(securityAnnotation, log)) {
					doAnalysis();
				} else {
					this.log.error(SootUtils.generateFileName(sootMethod), sourceLine, ("Analysis will not performed for method " + SootUtils.generateMethodSignature(sootMethod) + " because of an invalid return security level."));
				}
			} else {
				this.log.error(SootUtils.generateFileName(sootMethod), sourceLine,("Analysis will not performed for method " + SootUtils.generateMethodSignature(sootMethod) + " because of invalid parameter security levels."));
			}
		}
	}

//	private Map<JNopStmt, Stmt> nopTable = new HashMap<JNopStmt, Stmt>();
	
	@Override
	protected void flowThrough(LocalMap in, Unit d, LocalMap out) {
		copy(in, out);
		Stmt statement = (Stmt) d;
		in.prettyPrint(log, "flowThrough() in for " +  securityMethod.getSootMethod().getName());
		out.prettyPrint(log, "flowThrough() out@begin for " +  securityMethod.getSootMethod().getName());
		if (isEndOfImplicitFlow(statement)) {
			out.removeLastProgramCounterLevel();
		}
		long sourceLine = SootUtils.extractLineNumberFrom(statement);
		log.jimple(d.toString() + " [" + d.getClass().toString() + "]^" + in.getStrongestProgramCounterLevel() + "(" +  in.getStack().size() + ")");
		//System.err.println(d.toString());
		//log.debug(d.toString() + " [" + d.getClass().toString() + "]");
		if (statement instanceof JIdentityStmt) {
			JIdentityStmt jJIdentityStmt = (JIdentityStmt) d;
			Value left = jJIdentityStmt.getLeftOp();
			Value right = jJIdentityStmt.getRightOp();
			if (right instanceof ParameterRef) {
				ParameterRef parameterRef = (ParameterRef) right;
				MethodParameter methodParameter = this.securityMethod.getMethodParameterAtIndex(parameterRef.getIndex());
				if (left instanceof Local) {
					Local local = (Local) left;
					if (methodParameter.getPosition() >= 0 && methodParameter.getName().equals(local.getName()) && methodParameter.getType().equals(parameterRef.getType())) {
						if (!out.update(local, methodParameter.getLevel())) {
							// TODO NO UPDATE
						}
					} else {
						// TODO Extracted Parameter doesn't match to this one
					}
				} else {
					// TODO Something other than Local ???
				}
			} else if (right instanceof ThisRef) {
				if (left instanceof Local) {
					Local local = (Local) left;
					if (!out.update(local, securityAnnotation.getWeakestSecurityLevel())) {
						// TODO NO UPDATE
					}
				} else {
					// TODO Something other than Local ???
				}	
			}
		} else if (statement instanceof JIfStmt) {
			JIfStmt jIfStmt = (JIfStmt) statement;
			Value condition = jIfStmt.getCondition();
			String conditionMaxLevel = securityAnnotation.getWeakestSecurityLevel();
			for(Object obj : condition.getUseBoxes()){
				if (obj instanceof ImmediateBox){
					ImmediateBox immediateBox = (ImmediateBox) obj;
					Value value = immediateBox.getValue();
					if (value instanceof Local) {
						Local local = (Local) value;
						if (in.containsLocal(local)) {
							if (securityAnnotation.isWeakerOrEqualsThan(conditionMaxLevel, in.getLevelOfLocal(local))) {
								conditionMaxLevel = in.getLevelOfLocal(local);
							}
						} else {
							// Local unknown
						}
					} else {
						// TODO: Condition contains something other than local
						//System.err.println("Condition contains an unknown value: " + value.getClass().getName() + " -> " + value.toString());
					}
				} else {
					// TODO: Condition contains something other than local
					//System.err.println("Condition contains an unknown usebox: " + obj.getClass().getName() + " -> " + obj.toString());
				}
			}
			out.addProgramCounterLevel(conditionMaxLevel);
			// TODO
//			Stmt stmt = jIfStmt.getTarget();
//			if (stmt instanceof JNopStmt) {
//				JNopStmt jNopStmt = (JNopStmt) stmt;
//				List<JNopStmt> list = new ArrayList<JNopStmt>();
//				list.add(jNopStmt);
//				nopTable.put(jNopStmt, jIfStmt);
//				System.err.println("IF target: " + jNopStmt.getClass().getName() + " ->" + list.toString());
//			}
		} else if (statement instanceof JGotoStmt){
			JGotoStmt jGotoStmt = (JGotoStmt) statement;
			// TODO
//			Unit target = jGotoStmt.getTarget();
//			System.err.println("Goto: " + jGotoStmt.getClass().getName() + " ->" + jGotoStmt.toString());
//			if (target instanceof JNopStmt) {
//				JNopStmt jNopStmt = (JNopStmt) target;
//				List<JNopStmt> list = new ArrayList<JNopStmt>();
//				list.add(jNopStmt);
//				nopTable.put(jNopStmt, jGotoStmt);
//				System.err.println("Goto target: " + jNopStmt.getClass().getName() + " ->" + list.toString());
//			}
		} else if (statement instanceof JNopStmt){
			JNopStmt jNopStmt = (JNopStmt) statement;
			// TODO
//			for (Object obj : jNopStmt.getBoxesPointingToThis()) {
//				if (obj instanceof StmtBox) {
//					StmtBox stmtBox = (StmtBox) obj;
//					Unit unit = stmtBox.getUnit();
//					System.err.println("Pointing to this: " + unit.getClass().getName() + " ->" +  unit.toString());
//					if (unit instanceof JNopStmt) {
//						
//					}
//				}
//			}
//			System.err.println("Pointing to this: " + nopTable.get(jNopStmt));
		} else if (statement instanceof JAssignStmt) {
			JAssignStmt jAssignStmt = (JAssignStmt) statement;
			Value right = jAssignStmt.getRightOp();
			Value left = jAssignStmt.getLeftOp();
			String rightLevel = securityAnnotation.getWeakestSecurityLevel();
			
			if (right instanceof FieldRef) {
				FieldRef fieldRef = (FieldRef) right;
				SecurityField securityField = new SecurityField(fieldRef.getField(), true);
				if (!securityField.isLibraryClass()) {
					if (securityField.isFieldSecurityLevelValid(securityAnnotation, log)) {
						rightLevel = securityField.getLevel();
					} else {
						// TODO Error invalid field annotations
						System.err.println("Error: Invalid annotations of field " + fieldRef.getField().toString());
					}
				} else {
					// TODO Warning Is Libaray
					System.err.println("Library field " + fieldRef.getField().toString());
				}
			} else if (right instanceof Local) {
				Local local = (Local) right;
				if (in.containsLocal(local)) {
					rightLevel = in.getLevelOfLocal(local);
				} else {
					// TODO Error Local not present
					System.err.println("Error Local not present");
				}
			} else if (right instanceof JCastExpr) {
				JCastExpr jCastExpr = (JCastExpr) right;
				Value value = jCastExpr.getOp();
				if (value instanceof Local) {
					Local local = (Local) value;
					if (in.containsLocal(local)) {
						rightLevel = in.getLevelOfLocal(local);
					} else {
						// TODO Error Local not present
						System.err.println("Error Local not present");
					}
				}
			} else if (right instanceof InvokeExpr) {
				/**
				 * TODO Check SideEffects if stronger security
				 */	
				InvokeExpr invokeExpr = (InvokeExpr) right;
				SecurityMethod securityInvokedMethod = new SecurityMethod(invokeExpr.getMethod(), true);
				// TODO: check the left of the object
				if (! securityInvokedMethod.isLibraryMethod()) {
					List<MethodParameter> invokedMethodParameter = securityInvokedMethod
							.getMethodParameters(); // Is Library = kein check
					if (invokeExpr.getArgCount() == invokedMethodParameter.size()) {
						if (securityAnnotation.isIdMethod(securityInvokedMethod.getSootMethod())) {
							if (!securityInvokedMethod.isReturnSecurityValid(securityAnnotation, log)) {
								System.err.println("NOT VALID");
							}
							LevelEquation levelEquation = securityInvokedMethod.getReturnLevelEquation();
							LevelEquationEvaluationVisitor levelEquationEvaluationVisitor = securityAnnotation.getLevelEquationEvaluationVisitor(new ArrayList<String>(), new ArrayList<String>());
							levelEquation.accept(levelEquationEvaluationVisitor);
							rightLevel = levelEquationEvaluationVisitor.getResultLevel();
						} else {
							List<String> parameterLevels = new ArrayList<String>();
							List<String> argumentLevels = new ArrayList<String>();
							for (int j = 0; j < invokeExpr.getArgCount(); j++) {
								Value value = invokeExpr.getArg(j);
								String argumentLevel = securityAnnotation.getWeakestSecurityLevel();
								String parameterLevel = invokedMethodParameter.get(j).getLevel();
								if (value instanceof Local) {
									Local local = (Local) value;
									if (in.containsLocal(local)) {
										argumentLevel = in.getLevelOfLocal(local);
									} else {
										// TODO Error Local not present
										System.err.println("Error: Local for argument not present");
									}
								} // else TODO argument is no Local
								if (!securityAnnotation.isWeakerOrEqualsThan(argumentLevel, parameterLevel)) {
									// TODO argument level is stronger than the parameter
									System.err.println("Argument level " + argumentLevel + " of " + value.toString() + " is stronger than the parameter level (" + parameterLevel + ")");
								}
								argumentLevels.add(argumentLevel);
								parameterLevels.add(parameterLevel);
							}
							if (!securityInvokedMethod.isReturnSecurityValid(securityAnnotation, log)) {
								System.err.println("NOT VALID");
							}
							LevelEquation levelEquation = securityInvokedMethod.getReturnLevelEquation();
							LevelEquationEvaluationVisitor levelEquationEvaluationVisitor = securityAnnotation.getLevelEquationEvaluationVisitor(argumentLevels, parameterLevels); //new LevelEquationEvaluationVisitor(argumentLevels, parameterLevels, availableSecurityLevels);
							levelEquation.accept(levelEquationEvaluationVisitor);
							rightLevel = levelEquationEvaluationVisitor.getResultLevel();
						}
					} else {
						// TODO: Arguments and parameter count incompatible
						System.err.println("Arguments and parameter count incompatible");
					}
				} else {
					// TODO: Invoke method is library
					System.err.println("Invoke method is library");
				}
				if (invokeExpr instanceof JVirtualInvokeExpr) {
					JVirtualInvokeExpr jVirtualInvokeExpr = (JVirtualInvokeExpr) invokeExpr;
					Value base = jVirtualInvokeExpr.getBase();
					if (base instanceof Local) {
						Local local = (Local) base;
						if (in.containsLocal(local)) {
							String localLevel = in.getLevelOfLocal(local);
							if (securityAnnotation.isWeakerOrEqualsThan(rightLevel, localLevel)) {
								rightLevel = localLevel;
							}
						}
					} else {
						// TODO Base is no local ???
					}
				}
			}
			/**
			 * TODO Ask PC-Level: 
			 * - Local: change level of Local to PC-level if pc-level is stronger than rightLevel
			 * - Field: Error if field level is weaker than PC-level
			 */
			if (left instanceof Local) {
				Local local = (Local) left;
				//log.debug("LOCAL" + " Assign " + i);
				String leftLevel = securityAnnotation.getWeakestSecurityLevel();
				if (in.containsLocal(local)) {
					leftLevel = in.getLevelOfLocal(local);
				} else {
					// TODO Error Local not present
					System.err.println("Error Local not present");
				}
				if (out.hasProgramCounterLevel()) {
					String pcLevel = out.getStrongestProgramCounterLevel();
					if (securityAnnotation.isWeakerOrEqualsThan(rightLevel, pcLevel)) {
						rightLevel = pcLevel;
					}
				}
				if (securityAnnotation.isWeakerOrEqualsThan(rightLevel, leftLevel)) {
					if (! leftLevel.equals(rightLevel)) {
						System.err.println("Security of local variable was weakened from " + leftLevel + " to " + rightLevel);
					}
				}
				if (!out.update(local,rightLevel)) {
					// TODO NO UPDATE
				}
			} else if (left instanceof FieldRef) {
				FieldRef fieldRef = (FieldRef) left;
				String leftLevel = securityAnnotation.getWeakestSecurityLevel();
				if (out.hasProgramCounterLevel()) {
					String pcLevel = out.getStrongestProgramCounterLevel();
					if (securityAnnotation.isWeakerOrEqualsThan(rightLevel, pcLevel)) {
						rightLevel = pcLevel;
					}
				}
				SecurityField securityField = new SecurityField(fieldRef.getField(), true);
				if (!securityField.isLibraryClass()) {
					if (securityField.isFieldSecurityLevelValid(securityAnnotation, log)) {
						leftLevel = securityField.getLevel();
					} else {
						// TODO Error invalid field annotations
						System.err.println("Error: Invalid annotations of field " + fieldRef.getField().toString());
					}
				} else {
					// TODO Warning Is Libaray
					System.err.println("Library field " + fieldRef.getField().toString());
				}
				if (!securityAnnotation.isWeakerOrEqualsThan(rightLevel, leftLevel)) {
					System.err.println("Assignment of high to field with low security " + fieldRef.getField().toString());
				} 
			}
			
		} else if (statement instanceof JInvokeStmt) {
			/**
			 * TODO Check SideEffects if stronger security
			 */	
			JInvokeStmt jInvokeStmt = (JInvokeStmt) statement;
			InvokeExpr invokeExpr =  jInvokeStmt.getInvokeExpr();
			SecurityMethod securityInvokedMethod = new SecurityMethod(invokeExpr.getMethod(), true); 
			if (! securityInvokedMethod.isLibraryMethod()) {
				List<MethodParameter> invokedMethodParameter = securityInvokedMethod
						.getMethodParameters(); // Is Library = kein check
				if (invokeExpr.getArgCount() == invokedMethodParameter.size()) {
					if (! securityAnnotation.isIdMethod(securityInvokedMethod.getSootMethod())) {
						List<String> parameterLevels = new ArrayList<String>();
						List<String> argumentLevels = new ArrayList<String>();
						for (int j = 0; j < invokeExpr.getArgCount(); j++) {
							Value value = invokeExpr.getArg(j);
							String argumentLevel = securityAnnotation.getWeakestSecurityLevel();
							String parameterLevel = invokedMethodParameter.get(j).getLevel();
							if (value instanceof Local) {
								Local local = (Local) value;
								if (in.containsLocal(local)) {
									argumentLevel = in.getLevelOfLocal(local);
								} else {
									// TODO Error Local not present
									System.err.println("Error: Local for argument not present");
								}
							} // else TODO argument is no Local
							// Ist result level Stern???
							if (!securityAnnotation.isWeakerOrEqualsThan(argumentLevel, parameterLevel)) {
								// TODO argument level is stronger than the parameter
								System.err.println("Argument level " + argumentLevel + " of " + value.toString() + " is stronger than the parameter level (" + parameterLevel + ")");
							}
							argumentLevels.add(argumentLevel);
							parameterLevels.add(parameterLevel);
						}
					}
				} else {
					// TODO: Arguments and parameter count incompatible
					System.err.println("Arguments and parameter count incompatible");
				}
			} else {
				// TODO: Invoke method is library
				System.err.println("Invoke method is library");
			}
		} else if (statement instanceof JReturnStmt) {
			JReturnStmt jReturnStmt = (JReturnStmt) statement;
			Value returnValue = jReturnStmt.getOp();
			String returnLevel = securityAnnotation.getWeakestSecurityLevel();
			if (returnValue instanceof Local) {
				Local local = (Local) returnValue;
				if (in.containsLocal(local)) {
					returnLevel = in.getLevelOfLocal(local);
					if (!securityAnnotation.isWeakerOrEqualsThan(returnLevel, securityMethod.getReturnLevel())) {
						System.err.println("Method return value with " + returnLevel + " security level. Expected was " + securityMethod.getReturnLevel() + " or weaker...");
					}
				} else {
					// TODO Error Local not present
					System.err.println("Error Local not present");
				}
			}
		} else if (statement instanceof JReturnVoidStmt) {
			if (! securityMethod.isReturnSecurityVoid(securityAnnotation)) {
				log.security(SootUtils.generateFileName(securityMethod.getSootMethod()), sourceLine, "Method " + SootUtils.generateMethodSignature(securityMethod.getSootMethod()) + " has a void return statement at source line " + sourceLine + ". But the return security level '" + this.securityMethod.getReturnLevel() + "' doesn't match the expected level '" + SecurityAnnotation.VOID_LEVEL + "'.");
			}
		}
		//log.debug(s.getClass().toString());
		out.prettyPrint(log, "flowThrough() out@result for " +  securityMethod.getSootMethod().getName());
	}

	@Override
	protected LocalMap newInitialFlow() {
		LocalMap map = new LocalMap(this.securityMethod.getSootMethod().getActiveBody().getLocals(), securityAnnotation);
		map.prettyPrint(log, "newIntialFlow() for " + securityMethod.getSootMethod().getName());
		return map;
	}

	@Override
	protected LocalMap entryInitialFlow() {
		LocalMap map = new LocalMap(this.securityMethod.getSootMethod().getActiveBody().getLocals(), securityAnnotation);
		map.prettyPrint(log, "entryInitialFlow() for " +  securityMethod.getSootMethod().getName());
		return map;
	}

	@Override
	protected void merge(LocalMap in1, LocalMap in2, LocalMap out) {
		in1.prettyPrint(log, "merge() in1 for " +  securityMethod.getSootMethod().getName());
		in2.prettyPrint(log, "merge() in2 for " +  securityMethod.getSootMethod().getName());
		out.prettyPrint(log, "merge() out@begin for " +  securityMethod.getSootMethod().getName());
		copy(in1, out);
		out.addAllStronger(in2.getExtendedLocals());
		// TODO out.removeLastProgramCounterLevel();
		out.prettyPrint(log, "merge() out@result for " +  securityMethod.getSootMethod().getName());
	}

	@Override
	protected void copy(LocalMap source, LocalMap dest) {
		source.prettyPrint(log, "copy() source for " +  securityMethod.getSootMethod().getName());
		dest.prettyPrint(log, "copy() dest@begin for " +  securityMethod.getSootMethod().getName());
		dest.clear();
		dest.addAll(source.getExtendedLocals(), source.getStack());
		dest.prettyPrint(log, "copy() dest@result for " +  securityMethod.getSootMethod().getName());
		
	}
	
	private boolean isEndOfImplicitFlow(Stmt statement) {
		return false;
	}
	
	public void checkAnalyis() {
		
	}
}
