package api.empresas.validator;

import org.junit.Assert;
import org.junit.Test;

public class CNPJValidatorTest {

	@Test
	public void shouldValidateValidCNPJ() {
		String cnpj = "91.486.727/0001-86";

		Assert.assertTrue(CNPJValidator.isValidCnpj(cnpj));
	}

	@Test
	public void shouldNotValidateInvalidCNPJ() {
		String cnpj = "NotAValidCNPJ";

		Assert.assertFalse(CNPJValidator.isValidCnpj(cnpj));
	}

	@Test
	public void shouldNotValidateEmptyCNPJ() {
		String cnpj = "";

		Assert.assertFalse(CNPJValidator.isValidCnpj(cnpj));
	}


	@Test
	public void shouldNotValidateNullCNPJ() {
		String cnpj = null;

		Assert.assertFalse(CNPJValidator.isValidCnpj(cnpj));
	}
}
