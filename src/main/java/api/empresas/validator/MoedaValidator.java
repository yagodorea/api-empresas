package api.empresas.validator;

import java.util.Currency;

public class MoedaValidator {

	public static boolean isValidMoeda(String moeda) {
		try {
			Currency.getInstance(moeda);
		} catch (IllegalArgumentException | NullPointerException e) {
			return false;
		}
		return true;
	}
}
