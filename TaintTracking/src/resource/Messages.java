package resource;

import java.util.List;
import java.util.ResourceBundle;

/**
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class Messages {

	/**
	 * DOC
	 */
	private final static ResourceBundle bundle = ResourceBundle.getBundle("resource.messages");

	/**
	 * DOC
	 * 
	 * @param keyword
	 * @param args
	 * @return
	 */
	public static String getMsg(String keyword, Object... args) {
		return String.format(bundle.getLocale(), bundle.getString(keyword), args);
	}

	/**
	 * DOC
	 * 
	 * @param noun
	 * @return
	 */
	public static String addArticle(String noun) {
		boolean isVocal = false;
		for (String vocal : new String[] { "a", "e", "i", "o", "u" }) {
			if (noun.startsWith(vocal)) isVocal = true;
		}
		return String.format("%s %s", (isVocal ? "an" : "a"), noun);
	}
	
	/**
	 * DOC
	 * 
	 * @param list
	 * @return
	 */
	public static String commaList(List<String> list) {
		String result = "";
		for (String item : list) {
			if (!result.equals("")) result += ", ";
			result += item;
		}
		return result;
	}
	
	/**
	 * DOC
	 * 
	 * @param list
	 * @return
	 */
	public static String commaList(String... list) {
		String result = "";
		for (String item : list) {
			if (!result.equals("")) result += ", ";
			result += item;
		}
		return result;
	}

}
