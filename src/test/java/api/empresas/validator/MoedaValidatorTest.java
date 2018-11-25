package api.empresas.validator;

import org.junit.Assert;
import org.junit.Test;

public class MoedaValidatorTest {

	@Test
	public void shouldValidateValidMoeda() {
		String moeda = "EUR";

		Assert.assertTrue(MoedaValidator.isValidMoeda(moeda));
	}

	@Test
	public void shouldNotValidateInvalidMoeda() {
		String moeda = "invalid";

		Assert.assertFalse(MoedaValidator.isValidMoeda(moeda));
	}
}
