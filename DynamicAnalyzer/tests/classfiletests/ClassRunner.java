package classfiletests;

import utils.logging.L1Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;



public class ClassRunner {

  static String s = null;
  static boolean error_recognized = false;
  static String fileName = "";
  static Logger logger = L1Logger.getLogger();

  protected static void runClass(String fileName) {

    ClassRunner.fileName = fileName;

    try {

      String[] cmd = {"/bin/sh", "-c", "cd sootOutput; java " + fileName};
      Process process = Runtime.getRuntime().exec(cmd);

      BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

      // read the output from the command
      System.out.println("Here is the standard output of the command:\n");
      while ((s = stdInput.readLine()) != null) {
        System.out.println(s);
      }
            
      // read any errors from the attempted command
      BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
      
      System.out.println("Here is the standard error of the command (if any):\n");
      while ((s = stdError.readLine()) != null) {
        System.out.println(s);
        error_recognized = true;
      }
        
      if (error_recognized) {
        printByteCode();
      }
        
    } catch (IOException e) {
      System.out.println("Class couldn't be executed");
      e.printStackTrace();
    }
  }

  protected static void printByteCode() {

    try {

      String[] cmd = {"/bin/sh", "-c", "cd sootOutput; javap -c " + "main.testclasses.Simple"};
      Process process = Runtime.getRuntime().exec(cmd);

      BufferedReader stdInput = new BufferedReader(new
          InputStreamReader(process.getInputStream()));

      // read the output from the command
      logger.severe("Here is the standard output of the command:\n");
      while ((s = stdInput.readLine()) != null) {
        System.out.println(s);
      }

      BufferedReader stdError = new BufferedReader(new
              InputStreamReader(process.getErrorStream()));
      
      // read any errors from the attempted command
      logger.severe("Here is the standard error of the command (if any):\n");
      while ((s = stdError.readLine()) != null) {
        System.out.println(s);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  protected static void runClass2(String fileName) throws InvocationTargetException {

    ClassRunner.fileName = fileName;

    try { 
      DaClassLoader classloader = new DaClassLoader(ClassRunner.class.getClassLoader(), fileName);
      Class<?> loadedClass = classloader.loadClass();
      logger.info("aClass.getName() = " + loadedClass.getName());
      Object[] args = new Object[1];
      Method method = loadedClass.getMethod("main", String[].class);
      method.invoke(null, args);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
  }
}