import effect.EffectAnnotation.*;

@ReadEffect({})
@WriteEffect({"SideEffectTest"})
@NewEffect({"java.lang.String"})
public class SideEffectTest {

	
	public static int s1 = 0;
	public static boolean s2 = true;
	public static String s3 = "Hello World!";
	public static String s4 = new String("Hello World!");
	public static boolean s5 = Boolean.getBoolean("true");
	public int d1 = 0;
	
	@ReadEffect({})
	@WriteEffect({"SideEffectTest"})
	@NewEffect({"java.lang.String"})
	public SideEffectTest() {
		super();
		s1 = 1;
	}
	
	@ReadEffect({"SideEffectTest"})
	@WriteEffect({"SideEffectTest"})
	@NewEffect({})
	public void hello() {
		d1 = getInt();
	}
	
	@ReadEffect({})
	@WriteEffect({})
	@NewEffect({})
	public int getInt() {
		return 1;
	}
	
	

	
}
