package api.empresas.domain;

import lombok.Data;

import java.util.UUID;

@Data
public class EmpresaCompleta {
	private UUID id;

	private String cnpj;

	private String nome;

	private String email;

	private Endereco endereco;

	private String moeda;

	private String cotacao;

}
