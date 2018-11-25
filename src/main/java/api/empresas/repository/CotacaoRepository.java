package api.empresas.repository;

import api.empresas.domain.Cotacao;
import org.springframework.data.repository.CrudRepository;

public interface CotacaoRepository extends CrudRepository<Cotacao,String> {
}
