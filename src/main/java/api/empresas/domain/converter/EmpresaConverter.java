package api.empresas.domain.converter;

import api.empresas.domain.Empresa;
import api.empresas.domain.EmpresaCompleta;
import org.springframework.util.ObjectUtils;

public class EmpresaConverter {

	public static EmpresaCompleta toEmpresaCompleta(Empresa empresa) {
		EmpresaCompleta empresaCompleta = new EmpresaCompleta();

		if (!ObjectUtils.isEmpty(empresa.getId())) {
			empresaCompleta.setId(empresa.getId());
		}

		if (!ObjectUtils.isEmpty(empresa.getNome())) {
			empresaCompleta.setNome(empresa.getNome());
		}

		if (!ObjectUtils.isEmpty(empresa.getEmail())) {
			empresaCompleta.setEmail(empresa.getEmail());
		}

		if (!ObjectUtils.isEmpty(empresa.getCnpj())) {
			empresaCompleta.setCnpj(empresa.getCnpj());
		}

		if (!ObjectUtils.isEmpty(empresa.getMoeda())) {
			empresaCompleta.setMoeda(empresa.getMoeda());
		}

		if (!ObjectUtils.isEmpty(empresa.getEndereco())) {
			empresaCompleta.setEndereco(empresa.getEndereco());
		}

		return empresaCompleta;
	}
}
