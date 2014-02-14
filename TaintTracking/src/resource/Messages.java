package resource;

import java.util.ResourceBundle;

public class Messages {
	
	private final static ResourceBundle bundle = ResourceBundle.getBundle("resource.messages");
	
	public static String get(String keyword, Object... args) {
        return String.format(bundle.getLocale(), bundle.getString(keyword), args);
    }

}
