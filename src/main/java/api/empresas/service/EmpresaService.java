package api.empresas.service;

import api.empresas.domain.Empresa;
import api.empresas.specification.EmpresaFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmpresaService {

	Optional<Empresa> getEmpresaById(UUID id);

	List<Empresa> getByFilter(EmpresaFilter filter, Pageable pageable);

	Optional<Empresa> getByCnpj(String cnpj);

	Empresa postEmpresa(Empresa empresa);

	Empresa putEmpresa(Empresa empresa);

	void deleteEmpresa(UUID id);
}
