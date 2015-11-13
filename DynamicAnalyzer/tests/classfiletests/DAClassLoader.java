package classfiletests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class DAClassLoader extends ClassLoader{

	public DAClassLoader(ClassLoader parent) {
	   super(parent);
	}

	public Class loadClass() throws ClassNotFoundException {


	    try {
	     String url = "file:/home/koenigr/gradual-java/JGS/gradual-java/DynamicAnalyzer/sootOutput/main/testclasses/Simple.class";
	     URL myUrl = new URL(url);
	     URLConnection connection = myUrl.openConnection();
	     InputStream input = connection.getInputStream();
	     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	     int data = input.read();

	       while(data != -1){
	         buffer.write(data);
	         data = input.read();
	       }

	       input.close();

	       byte[] classData = buffer.toByteArray();

	       return defineClass("main.testclasses.Simple",
	         classData, 0, classData.length);

	       } catch (MalformedURLException e) {
	            e.printStackTrace();
	       } catch (IOException e) {
	            e.printStackTrace();
	       }

	 return null;
}

}
