package api.empresas.specification;

import api.empresas.domain.Empresa;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class EmpresaSpecification {
	public static Specification<Empresa> byFilter(EmpresaFilter filter) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (!ObjectUtils.isEmpty(filter.getCidade())) {
				predicates.add(criteriaBuilder.like(
						criteriaBuilder.upper(
								root.get("endereco").get("cidade")),
						"%" + filter.getCidade().toUpperCase() + "%"));
			}

			if (!ObjectUtils.isEmpty(filter.getMoeda())) {
				predicates.add(criteriaBuilder.like(
						criteriaBuilder.upper(
								root.get("moeda")),
						"%" + filter.getMoeda().toUpperCase() + "%"));
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}

	public static Specification<Empresa> byCnpj(String cnpj) {
		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cnpj"),cnpj));
	}
}
