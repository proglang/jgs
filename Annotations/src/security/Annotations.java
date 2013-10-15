package security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>TaintTracking Annotations</h1>
 * 
 * The class {@link Annotations} provides multiple types of annotations. These annotations are
 * required for the <b>'TaintTracking'-analysis</b> and the developer, who wants to analyze his
 * program, has to annotated his source code with those annotations. The class includes an
 * annotation type for specifying the <em>security level</em> of a class or instance member (see
 * {@link FieldSecurity}), as well as 3 kinds of annotations to add additional informations to
 * methods. These annotations allow to specify the <em>security level</em> of the parameters of a
 * method (see {@link ParameterSecurity}), the <em>security level</em> of the value which is
 * returned by a method (see {@link ReturnSecurity}) and also to specify the <em>write effects</em>
 * of a method or a class (see {@link WriteEffect}).
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.3
 */
public class Annotations {

	/**
	 * <h1>Member security level annotation</h1>
	 * 
	 * The annotation {@link FieldSecurity} allows to specify the <em>security level</em> of a class
	 * or instance member. This annotation type can be used only for fields and should contain a
	 * concrete <em>security level</em> (such a concrete level should be specified by the
	 * <em>SootSecurityLevel</em> implementation of the developer which inherits from the class
	 * {@link SecurityLevel}) as String.
	 * 
	 * <h2>Example:</h2>
	 * 
	 * <pre>
	 * <code>
	 * &#64WriteEffect({})
	 * public class Example {
	 * 
	 * 	<b>&#64FieldSecurity("low")</b>
	 * 	public static int lowMember;
	 * 
	 * 	<b>&#64FieldSecurity("high")</b>
	 * 	public int highMember;
	 * 	
	 * 	...
	 * 
	 * }
	 * </code>
	 * </pre>
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface FieldSecurity {

		/** The class or instance member <em>security level</em> as String. */
		String value();

	}

	/**
	 * <h1>Parmeter security level annotation</h1>
	 * 
	 * The annotation {@link ParamterSecurity} allows to specify the <em>security level</em> of the
	 * method parameters. This annotation type can be used for methods and constructors, and should
	 * contain a list of concrete <em>security levels</em> (such a concrete level should be
	 * specified by the <em>SootSecurityLevel</em> implementation of the developer which inherits
	 * from the class {@link SecurityLevel}) and/or of a variable <em>security levels</em> as String
	 * array. A variable <em>security level</em> consists of the character '{@code *}' followed by a
	 * number, which starts from 0. E.g. if the resulting <em>security level</em> depends on two
	 * levels of unknown arguments, those two variable <em>security levels</em> are call {@code *0}
	 * and {@code *1}. The labeling, especially the choice of the numbers has to be without
	 * interruptions. The benefit of the variable <em>security level</em> is the calculation of the
	 * return <em>security level</em> based on the <em>security level</em> of the arguments.
	 * 
	 * <h2>Examples:</h2>
	 * <ol>
	 * <li>
	 * 
	 * <pre>
	 * <code>
	 * &#64WriteEffect({})
	 * public class Example {
	 * 
	 * 	&#64WriteEffect({})
	 * 	<b>&#64ParameterSecurity({"low", "*0"})</b>
	 * 	&#64ReturnSecurity("void")
	 * 	public void method(int arg1, int arg2){
	 * 	
	 * 		...
	 * 	
	 * 	}
	 * 
	 * 	...
	 * 
	 * }
	 * </code>
	 * </pre>
	 * 
	 * </li>
	 * <li>
	 * 
	 * <pre>
	 * <code>
	 * &#64WriteEffect({})
	 * public class Example {
	 * 
	 * 	&#64WriteEffect({})
	 * 	<b>&#64ParameterSecurity({"*1", "*0"})</b>
	 * 	&#64ReturnSecurity("void")
	 * 	public void method(int arg1, int arg2){
	 * 	
	 * 		...
	 * 	
	 * 	}
	 * 
	 * 	...
	 * 
	 * }
	 * </code>
	 * </pre>
	 * 
	 * </li>
	 * <li>
	 * 
	 * <pre>
	 * <code>
	 * &#64WriteEffect({})
	 * public class Example {
	 * 
	 * 	&#64WriteEffect({})
	 * 	<b>&#64ParameterSecurity({"*0", "*1", "*2"})</b>
	 * 	&#64ReturnSecurity("max(*0, max(*1, *2))")
	 * 	public int method(int arg1, int arg2, int arg3){
	 * 	
	 * 		...
	 * 	
	 * 	}
	 * 
	 * 	...
	 * 
	 * }
	 * </code>
	 * </pre>
	 * 
	 * </li>
	 * </ol>
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ParameterSecurity {

		/** The parameter <em>security levels</em> as String array. */
		String[] value();

	}

	/**
	 * <h1>Return security level annotation</h1>
	 * 
	 * The annotation {@link ReturnSecurity} allows to specify the <em>security level</em> of the
	 * returned value of a method. This annotation type can be used for methods and should contain a
	 * concrete <em>security level</em> (such a concrete level should be specified by the
	 * <em>SootSecurityLevel</em> implementation of the developer which inherits from the class
	 * {@link SecurityLevel}, see 1st example) or a variable <em>security level</em> (a variable
	 * security level is specified by the {@link ParameterSecurity} annotation of the same method
	 * and starts with the sign ' {@code *}' followed by a number, see 2nd example) as String. It is
	 * also possible to use a level equation as result level, e.g. "{@code max(*0, min(*1, high))}".
	 * A level equation allows the usage of the operators {@code min} and {@code max} and the usage
	 * of concrete <em>security levels</em> as well as variable <em>security levels</em> as
	 * operands, see 3th example. In the case of variable <em>security levels</em> the analysis
	 * tries solving the given equation by inserting the corresponding <em>security levels</em> of
	 * the arguments. For methods without a return value the keyword {@code void} should be used as
	 * value of the annotation, see 4th example. <b>Constructors don't need a {@link ReturnSecurity}
	 * annotation with the value {@code void}.</b>
	 * 
	 * 
	 * <h2>Examples:</h2>
	 * <ol>
	 * <li>
	 * 
	 * <pre>
	 * <code>
	 * &#64WriteEffect({})
	 * public class Example {
	 * 
	 * 	&#64WriteEffect({})
	 * 	&#64ParameterSecurity({})
	 * 	<b>&#64ReturnSecurity("low")</b>
	 * 	public int method() {
	 * 
	 * 		...
	 * 
	 * 	}
	 * 
	 * }
	 * </code>
	 * </pre>
	 * 
	 * </li>
	 * <li>
	 * 
	 * <pre>
	 * <code>
	 * &#64WriteEffect({})
	 * public class Example {
	 * 
	 * 	&#64WriteEffect({})
	 * 	&#64ParameterSecurity({"*0"})
	 * 	<b>&#64ReturnSecurity("*0")</b>
	 * 	public int method(int variableParam) {
	 * 
	 * 		...
	 * 
	 * 	}
	 * 
	 * }
	 * </code>
	 * </pre>
	 * 
	 * </li>
	 * <li>
	 * 
	 * <pre>
	 * <code>
	 * &#64WriteEffect({})
	 * public class Example {
	 * 
	 * 	&#64WriteEffect({})
	 * 	&#64ParameterSecurity({"*0"})
	 * 	<b>&#64ReturnSecurity("max(*0,high)")</b>
	 * 	public int method(int variableParam) {
	 * 
	 * 		...
	 * 
	 * 	}
	 * 
	 * }
	 * </code>
	 * </pre>
	 * 
	 * </li>
	 * <li>
	 * 
	 * <pre>
	 * <code>
	 * &#64WriteEffect({})
	 * public class Example {
	 * 
	 * 	&#64WriteEffect({})
	 * 	&#64ParameterSecurity({})
	 * 	<b>&#64ReturnSecurity("void")</b>
	 * 	public void method() {
	 * 
	 * 		...
	 * 
	 * 	}
	 * 
	 * }
	 * </code>
	 * </pre>
	 * 
	 * </li>
	 * </ol>
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ReturnSecurity {

		/** The return security level as String. */
		String value();

	}

	/**
	 * <h1>Write effect annotation</h1>
	 * 
	 * The annotation {@link WriteEffect} allows to specify the <em>security levels</em> to which a
	 * class or a method has <em>write effects</em>. This annotation type can be used for methods
	 * and classes, and should contain a list of concrete <em>security levels</em> (such a concrete
	 * level should be specified by the <em>SootSecurityLevel</em> implementation of the developer
	 * which inherits from the class {@link SecurityLevel}) as String array. The class
	 * <em>write effects</em> are those which occur during the initialization of the class (e.g.
	 * when an instance of class is created, a static method of class is invoked, a value to a
	 * static field is assigned or a non-constant field is used). The <em>write effects</em> of a
	 * method are those which occur inside of the method body.
	 * 
	 * <h2>Write effects:</h2>
	 * <ol>
	 * <li>assignment:
	 * <ul>
	 * <li>instance member: <em>Write effect</em> to the <em>security level</em> of the field.</li>
	 * <li>class member: <em>Write effect</em> to the <em>security level</em> of the field as well
	 * as <em>write
	 * effects</em> of the class which declares the field.</li>
	 * <li>array: <em>Write effect</em> to the <em>security level</em> of the array, even when it is
	 * a local array.</li>
	 * </ul>
	 * </li>
	 * <li>method invoke:
	 * <ul>
	 * <li>method: <em>Write effects</em> of the invoked method.</li>
	 * <li>static method: <em>Write effects</em> of the invoked method as well as
	 * <em>write effects</em> of the class which declares the invoked method.</li>
	 * </ul>
	 * </li>
	 * </ol>
	 * <h2>Example:</h2>
	 * 
	 * <pre>
	 * <code>
	 * <b>&#64WriteEffect({"low"})</b>
	 * public class Example {
	 * 
	 * 	<b>&#64WriteEffect({"low", "high"})</b>
	 * 	&#64ParameterSecurity({})
	 * 	&#64ReturnSecurity("void")
	 * 	public void method() {
	 * 		
	 * 		...
	 * 
	 * 	}
	 * 
	 * 	...
	 * 
	 * }
	 * </code>
	 * </pre>
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface WriteEffect {

		/** The <em>write effects</em> of a class or a method as String array. */
		String[] value();

	}

}