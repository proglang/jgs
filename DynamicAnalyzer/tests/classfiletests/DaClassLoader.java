package classfiletests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * Class which loads external classes.
 */
public class DaClassLoader extends ClassLoader{

  // TODO define path
  String path = "file:/home/koenigr/gradual-java/JGS/gradual-java/DynamicAnalyzer/"
      + "sootOutput/main/testclasses/";
  String file = "";

  /**
   * @param parent
   * @param file
   */
  public DaClassLoader(ClassLoader parent, String file) {
    super(parent);
    
    // TODO Check format of filename
    this.file = file;
  }

  /**
   * Method to load class which is specified over the constructor.
   * @return Returns loaded class. If it fails, it returns null.
   * @throws ClassNotFoundException Throws an Exception if the class couldn't be loaded.
   */
  public Class<?> loadClass() throws ClassNotFoundException {

    try {
      String url = path + file + ".class";
      URL myUrl = new URL(url);
      URLConnection connection = myUrl.openConnection();
      InputStream input = connection.getInputStream();
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      int data = input.read();
  
      while (data != -1) {
        buffer.write(data);
        data = input.read();
      }

      input.close();

      byte[] classData = buffer.toByteArray();

      return defineClass("main.testclasses." + file,
        classData, 0, classData.length);

    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}
