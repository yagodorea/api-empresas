package api.empresas.domain;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "empresas")
public class Empresa {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	private String cnpj;

	private String nome;

	private String email;

	@JoinColumn(name = "endereco", referencedColumnName = "id")
	@OneToOne(cascade = CascadeType.ALL)
	private Endereco endereco;

	private String moeda;
}
