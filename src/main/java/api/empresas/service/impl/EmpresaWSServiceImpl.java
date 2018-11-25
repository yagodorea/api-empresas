package api.empresas.service.impl;

import api.empresas.domain.Empresa;
import api.empresas.domain.EmpresaWS;
import api.empresas.domain.Endereco;
import api.empresas.service.CotacaoService;
import api.empresas.service.EmpresaWSService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

@Service
public class EmpresaWSServiceImpl implements EmpresaWSService {

	private static Logger logger = LogManager.getLogger();

	private final String WSurl = "https://www.receitaws.com.br/v1/cnpj/";
	private final CotacaoService cotacaoService;

	@Autowired
	public EmpresaWSServiceImpl(CotacaoService cotacaoService) {
		this.cotacaoService = cotacaoService;
	}

	/**
	 * Funçao para obtençao dos dados da empresa via WebService
	 * Caso o usuario insira as informaçoes manualmente, elas vao sobrescrever
	 * os dados obtidos pelo serviço externo.
	 *
	 * Se o WS estiver indisponivel, nenhum dado podera ser obtido, e o fluxo
	 * continuara somente com os dados fornecidos.
	 *
	 * @param empresa Dados da empresa inseridos pelo usuario
	 * @return empresa Dados completos da empresa, obtidos via WS
	 */
	@Override
	public Empresa getEmpresa(Empresa empresa) {
		String cnpj = empresa.getCnpj();
		RestTemplate restTemplate = new RestTemplate();
		EmpresaWS empresaWS;
		try {
			empresaWS = restTemplate.getForObject(WSurl + cnpj, EmpresaWS.class);
		} catch (Exception ex) {
			logger.error("Erro chamando WebService de Empresas. " + ex.getMessage());
			return empresa;
		}

		if (ObjectUtils.isEmpty(empresa.getNome())) {
			empresa.setNome(empresaWS.getNome());
		}
		if (ObjectUtils.isEmpty(empresa.getEmail())) {
			empresa.setEmail(empresaWS.getEmail());
		}
		empresa.setCnpj(cnpj);
		if (ObjectUtils.isEmpty(empresa.getMoeda())) {
			empresa.setMoeda("BRL");
		}

		Endereco endereco = new Endereco();
		if (ObjectUtils.isEmpty(empresa.getEndereco())) {
			empresa.setEndereco(new Endereco());
		}
		if (ObjectUtils.isEmpty(empresa.getEndereco().getCidade())) {
			endereco.setCidade(empresaWS.getMunicipio());
		}
		if (ObjectUtils.isEmpty(empresa.getEndereco().getLogradouro())) {
			endereco.setLogradouro(empresaWS.getLogradouro());
		}
		if (ObjectUtils.isEmpty(empresa.getEndereco().getCep())) {
			endereco.setCep(empresaWS.getCep());
		}
		if (ObjectUtils.isEmpty(empresa.getEndereco().getNumero())) {
			endereco.setNumero(empresaWS.getNumero());
		}
		if (ObjectUtils.isEmpty(empresa.getEndereco().getBairro())) {
			endereco.setBairro(empresaWS.getBairro());
		}
		if (ObjectUtils.isEmpty(empresa.getEndereco().getComplemento())) {
			endereco.setComplemento(empresaWS.getComplemento());
		}

		empresa.setEndereco(endereco);
		return empresa;
	}
}
