package api.empresas.validator;

import api.empresas.domain.Empresa;
import api.empresas.interfaces.Error;
import api.empresas.interfaces.Messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmpresaValidator {
	public static List<Error> validateEmpresa(Empresa empresa) {
		List<Error> errors = new ArrayList<>();

		if (!Objects.nonNull(empresa.getCnpj())) {
			errors.add(new Error(Messages.INVALID_CNPJ));
		}

		return errors;
	}
}
