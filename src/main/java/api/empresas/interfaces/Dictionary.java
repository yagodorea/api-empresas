package api.empresas.interfaces;

import java.util.Collections;
import java.util.ResourceBundle;

public class Dictionary {
	private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");

	private static final Dictionary dictionary = new Dictionary();

	private Dictionary() {
		super();
	}

	public static Dictionary get(){
		return dictionary;
	}

	public String valueOf(String key){
		return MESSAGES.getString(key);
	}

	public Iterable<String> keys(){
		return 	Collections.list(MESSAGES.getKeys());
	}
}
