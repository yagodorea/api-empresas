package api.empresas.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "cotacoes")
public class Cotacao {

	@Id
	String code;

	String bid;

	String timestamp;
}
