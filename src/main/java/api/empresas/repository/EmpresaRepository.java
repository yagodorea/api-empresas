package api.empresas.repository;

import api.empresas.domain.Empresa;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface EmpresaRepository extends PagingAndSortingRepository<Empresa,UUID>, JpaSpecificationExecutor<Empresa> {
}