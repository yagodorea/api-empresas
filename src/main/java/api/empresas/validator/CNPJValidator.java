package api.empresas.validator;

/**
 * Static class for checking validity of a CNPJ document
 *
 * @author Yago Dórea (yago.dorea@gmail.com)
 */
public class CNPJValidator {

	private static final int[] weight = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

	private CNPJValidator() { super(); }

	/**
	 * CPF validator
	 *
	 * @param cnpj string to be verified
	 * @return boolean
	 */
	public static boolean isValidCnpj(String cnpj) {
		if (cnpj != null) {
			// Remove eventual extra spaces
			cnpj = cnpj.trim();

			// 14 chars when CNPJ w/o punctuation, 18 when it has
			if (cnpj.length() != 14 && cnpj.length() != 18) return false;

			// Remove punctuation to process
			cnpj = cnpj.replaceAll("\\D", "");

			// Calculate verification digits
			Integer digit1 = calculateDigit(cnpj.substring(0, 12), weight);
			Integer digit2 = calculateDigit(cnpj.substring(0, 12) + digit1, weight);

			return cnpj.equals(cnpj.substring(0, 12) + digit1.toString() + digit2.toString());
		} else return false;
	}

	public static boolean isValidCnpj(Long cnpj) {
		String stringCnpj = "" + cnpj;
		return isValidCnpj(stringCnpj);
	}

	private static int calculateDigit(String str, int[] weight) {
		int sum = 0;
		// The verification digit is obtained by measuring the weighted average of the
		// base digits, with the verification digit being the rest of the division
		for (int i = str.length()-1, digit; i >= 0; i-- ) {
			digit = Integer.parseInt(str.substring(i,i+1));
			sum += digit*weight[weight.length-str.length()+i];
		}
		sum = 11 - sum % 11;
		return sum > 9 ? 0 : sum;
	}

}
