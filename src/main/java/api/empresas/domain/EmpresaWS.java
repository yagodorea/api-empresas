package api.empresas.domain;

import lombok.Data;

@Data
public class EmpresaWS {
	String nome;
	String email;
	String logradouro;
	String complemento;
	String numero;
	String municipio;
	String bairro;
	String cep;
}
