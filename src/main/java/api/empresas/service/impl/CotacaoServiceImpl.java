package api.empresas.service.impl;

import api.empresas.domain.Cotacao;
import api.empresas.repository.CotacaoRepository;
import api.empresas.service.CotacaoService;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CotacaoServiceImpl implements CotacaoService {

	private static Logger logger = LogManager.getLogger();

	private static final String WSurl = "http://economia.awesomeapi.com.br/jsonp/";

	private final CotacaoRepository cotacaoRepository;

	@Autowired
	public CotacaoServiceImpl(CotacaoRepository cotacaoRepository) {
		this.cotacaoRepository = cotacaoRepository;
	}

	@Override
	public String getCotacao(String moeda) {

		if (moeda.equals("BRL")) {
			return "1.000";
		}

		Optional<Cotacao> cotacao = cotacaoRepository.findById(moeda);

		if (cotacao.isPresent() && !isOutdated(cotacao.get().getTimestamp())) {
			return cotacao.get().getBid();
		}

		logger.info("Chamando WS de cotacao");

		RestTemplate restTemplate = new RestTemplate();

		// Set User-Agent header because awesomeapi.com.br doesn't accept Java/1.8.0_181 as a user agent
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.set("User-Agent", "curl/7.47.0");
		HttpEntity entity = new HttpEntity(headers);

		Cotacao cot = new Cotacao();
		try {
			// Get string and parse to JSON object
			ResponseEntity<String> result = restTemplate.exchange(WSurl + moeda + "-BRL/1", HttpMethod.GET,entity,String.class);
			Gson googleJson = new Gson();
			ArrayList lista = googleJson.fromJson(result.getBody(),ArrayList.class);
			LinkedTreeMap t = (LinkedTreeMap) lista.get(0);
			cot.setBid(t.get("bid").toString());
			cot.setCode(t.get("code").toString());
		} catch (Exception ex) {
			logger.error("Erro chamando WebService de Cotaçoes. " + ex.toString());
			return null;
		}

		updateTimestamp(cot.getCode(), cot.getBid());

		return cot.getBid();
	}

	private void updateTimestamp(String code, String bid) {
		Cotacao cotacao = new Cotacao();
		String now = String.valueOf(System.currentTimeMillis());

		cotacao.setBid(bid);
		cotacao.setCode(code);
		cotacao.setTimestamp(now);

		cotacaoRepository.save(cotacao);
	}

	private static boolean isOutdated(String timestamp) {
		if (ObjectUtils.isEmpty(timestamp)) {
			return true;
		}

		Long now = System.currentTimeMillis();
		logger.debug(now);
		Long then = Long.valueOf(timestamp);
		logger.debug(then);

		if (now - then > 1200000) {
			return true;
		} else {
			return false;
		}
	}
}
