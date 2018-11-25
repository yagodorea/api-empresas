package api.empresas.validator;

import api.empresas.domain.Empresa;
import api.empresas.domain.Endereco;
import api.empresas.interfaces.Error;
import api.empresas.interfaces.Messages;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EmpresaValidatorTest {

	@Test
	public void shouldValidateValidEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setCnpj("91.486.727/0001-86");
		empresa.setEmail("some-email@email.com");
		empresa.setNome("Minha Empresa");
		empresa.setMoeda("BRL");

		Endereco endereco = new Endereco();
		endereco.setLogradouro("Rua Joaquim Santos");
		endereco.setNumero("123");
		endereco.setCep("11233490");
		endereco.setBairro("Centro");
		endereco.setComplemento("Casa");
		endereco.setCidade("Sao Paulo");
		empresa.setEndereco(endereco);

		List<Error> errors = EmpresaValidator.validateEmpresa(empresa);
		List<Error> expected = new ArrayList<>();

		Assert.assertEquals(expected, errors);
		Assert.assertEquals(0,errors.size());
	}

	@Test
	public void shouldNotValidateEmpresaSemCNPJ() {

		Empresa empresa = new Empresa();
		empresa.setEmail("some-email@email.com");
		empresa.setNome("Minha Empresa");
		empresa.setMoeda("BRL");

		Endereco endereco = new Endereco();
		endereco.setLogradouro("Rua Joaquim Santos");
		endereco.setNumero("123");
		endereco.setCep("11233490");
		endereco.setBairro("Centro");
		endereco.setComplemento("Casa");
		endereco.setCidade("Sao Paulo");
		empresa.setEndereco(endereco);

		List<Error> errors = EmpresaValidator.validateEmpresa(empresa);
		List<Error> expected = new ArrayList<>();
		expected.add(new Error(Messages.INVALID_CNPJ));

		Assert.assertEquals(expected, errors);
		Assert.assertEquals(1,errors.size());
	}

	@Test
	public void shouldNotValidateEmpresaComMoedaInvalida() {

		Empresa empresa = new Empresa();
		empresa.setCnpj("91.486.727/0001-86");
		empresa.setEmail("some-email@email.com");
		empresa.setNome("Minha Empresa");
		empresa.setMoeda("blabla");

		Endereco endereco = new Endereco();
		endereco.setLogradouro("Rua Joaquim Santos");
		endereco.setNumero("123");
		endereco.setCep("11233490");
		endereco.setBairro("Centro");
		endereco.setComplemento("Casa");
		endereco.setCidade("Sao Paulo");
		empresa.setEndereco(endereco);

		List<Error> errors = EmpresaValidator.validateEmpresa(empresa);
		List<Error> expected = new ArrayList<>();
		expected.add(new Error(Messages.INVALID_CURRENCY));

		Assert.assertEquals(expected, errors);
		Assert.assertEquals(1,errors.size());
	}

	@Test
	public void shouldNotValidateNullEmpresa() {
		List<Error> errors = EmpresaValidator.validateEmpresa(null);
		List<Error> expected = new ArrayList<>();
		expected.add(new Error(Messages.INVALID_INPUT));

		Assert.assertEquals(expected, errors);
		Assert.assertEquals(1,errors.size());
	}
}
