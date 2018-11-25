package api.empresas.service.impl;

import api.empresas.domain.Empresa;
import api.empresas.repository.EmpresaRepository;
import api.empresas.service.EmpresaService;
import api.empresas.specification.EmpresaFilter;
import api.empresas.specification.EmpresaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmpresaServiceImpl implements EmpresaService {

	final
	EmpresaRepository empresaRepository;

	@Autowired
	public EmpresaServiceImpl(EmpresaRepository empresaRepository) {
		this.empresaRepository = empresaRepository;
	}

	@Override
	public Optional<Empresa> getEmpresaById(UUID id) {
		return empresaRepository.findById(id);
	}

	@Override
	public List<Empresa> getByFilter(EmpresaFilter filter, Pageable pageable) {
		Page<Empresa> result = empresaRepository.findAll(EmpresaSpecification.byFilter(filter), pageable);
		return result.getContent();
	}

	@Override
	public Optional<Empresa> getByCnpj(String cnpj) {
		return empresaRepository.findOne(EmpresaSpecification.byCnpj(cnpj));
	}

	@Override
	public Empresa postEmpresa(Empresa empresa) {
		return empresaRepository.save(empresa);
	}

	@Override
	public Empresa putEmpresa(Empresa empresa) {
		return empresaRepository.save(empresa);
	}

	@Override
	public void deleteEmpresa(UUID id) {
		empresaRepository.deleteById(id);
	}
}
