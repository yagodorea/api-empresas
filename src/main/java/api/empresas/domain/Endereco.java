package api.empresas.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
public class Endereco {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	private String logradouro;

	private String numero;

	private String complemento;

	private String cep;

	private String bairro;

	private String cidade;
}
