package api.empresas.controller;

import api.empresas.domain.EmpresaCompleta;
import api.empresas.domain.Endereco;
import api.empresas.domain.converter.EmpresaConverter;
import api.empresas.interfaces.Messages;
import api.empresas.interfaces.Error;
import api.empresas.domain.Empresa;
import api.empresas.service.CotacaoService;
import api.empresas.service.EmpresaService;
import api.empresas.service.EmpresaWSService;
import api.empresas.specification.EmpresaFilter;
import api.empresas.validator.CNPJValidator;
import api.empresas.validator.EmpresaValidator;
import api.empresas.validator.MoedaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
public class EmpresaController {

	private final EmpresaService empresaService;
	private final EmpresaWSService empresaWSService;
	private final CotacaoService cotacaoService;

	@Autowired
	public EmpresaController(
			EmpresaService empresaService,
			EmpresaWSService empresaWSService,
			CotacaoService cotacaoService) {
		this.empresaService = empresaService;
		this.empresaWSService = empresaWSService;
		this.cotacaoService = cotacaoService;
	}

	@GetMapping("/empresas")
	public ResponseEntity<?> getEmpresas(
			@RequestParam(name = "cidade", required = false) String cidade,
			@RequestParam(name = "moeda", required = false) String moeda,
			@RequestParam(name = "limit", required = true) Integer limit,
			@RequestParam(name = "offset", required = true) Integer offset
			) {
		if (!Objects.nonNull(limit) || limit <= 0) {
			limit = 10;
		}
		if (!Objects.nonNull(offset) || offset <= 0) {
			offset = 1;
		}

		if (!MoedaValidator.isValidMoeda(moeda)) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(new Error(Messages.INVALID_CURRENCY));
		}

		Pageable pageable = PageRequest.of(offset - 1, limit);

		EmpresaFilter filter = new EmpresaFilter();
		filter.setCidade(cidade);
		filter.setMoeda(moeda);

		List<Empresa> listEmpresas = empresaService.getByFilter(filter, pageable);

		List<EmpresaCompleta> completas = new ArrayList<>();

		for(Empresa emp : listEmpresas) {
			EmpresaCompleta completa = EmpresaConverter.toEmpresaCompleta(emp);
			completa.setCotacao(cotacaoService.getCotacao(emp.getMoeda()));
			completas.add(completa);
		}

		return ResponseEntity.ok(completas);
	}

	@GetMapping("/empresas/cnpj/{cnpj}")
	public ResponseEntity<?> getEmpresaPorCnpj(@PathVariable(value = "cnpj") String cnpj) {
		cnpj = straightenCnpj(cnpj);
		if (!CNPJValidator.isValidCnpj(cnpj)) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(new Error(Messages.INVALID_CNPJ));
		}

		Optional<Empresa> empresa = empresaService.getByCnpj(cnpj);
		if (empresa.isPresent()) {

			EmpresaCompleta empresaCompleta = EmpresaConverter.toEmpresaCompleta(empresa.get());
			empresaCompleta.setCotacao(cotacaoService.getCotacao(empresaCompleta.getMoeda()));

			return ResponseEntity.ok(empresaCompleta);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/empresas/{empresaId}")
	public ResponseEntity<?> getEmpresa(@PathVariable(value = "empresaId") UUID empresaId) {
		Optional<Empresa> empresa = empresaService.getEmpresaById(empresaId);
		if (!empresa.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		EmpresaCompleta empresaCompleta = EmpresaConverter.toEmpresaCompleta(empresa.get());
		empresaCompleta.setCotacao(cotacaoService.getCotacao(empresaCompleta.getMoeda()));

		return ResponseEntity.ok(empresaCompleta);
	}

	@PostMapping("/empresas")
	public ResponseEntity<?> postEmpresa(@RequestBody Empresa empresa) {
		empresa.setCnpj(straightenCnpj(empresa.getCnpj()));

		if (!CNPJValidator.isValidCnpj(empresa.getCnpj())) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(new Error(Messages.INVALID_CNPJ));
		}
		if (!ObjectUtils.isEmpty(empresa.getMoeda()) && !MoedaValidator.isValidMoeda(empresa.getMoeda())) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(new Error(Messages.INVALID_CURRENCY));
		}
		Optional<Empresa> existe = empresaService.getByCnpj(empresa.getCnpj());
		if (existe.isPresent()) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new Error(Messages.ALREADY_REGISTERED));
		}
		Empresa empresaFinal = empresaWSService.getEmpresa(empresa);

		List<Error> errors = EmpresaValidator.validateEmpresa(empresaFinal);

		if (!CollectionUtils.isEmpty(errors)) {
			return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(errors);
		}

		EmpresaCompleta empresaCompleta = EmpresaConverter.toEmpresaCompleta(empresaService.postEmpresa(empresaFinal));

		// Set cotacao
		empresaCompleta.setCotacao(cotacaoService.getCotacao(empresaCompleta.getMoeda()));

		return new ResponseEntity<>(empresaCompleta, HttpStatus.OK);
	}

	@PutMapping("/empresas/{empresaId}")
	public ResponseEntity<?> putEmpresa(@RequestBody Empresa empresa,
	                          @PathVariable(value = "empresaId") UUID empresaId) {
		empresa.setCnpj(straightenCnpj(empresa.getCnpj()));
		Optional<Empresa> empresaCompleta = empresaService.getEmpresaById(empresaId);
		if (empresaCompleta.isPresent()) {
			Empresa empresaFinal = empresaCompleta.get();
			if (!ObjectUtils.isEmpty(empresa.getNome())) {
				empresaFinal.setNome(empresa.getNome());
			}
			if (!ObjectUtils.isEmpty(empresa.getEmail())) {
				empresaFinal.setEmail(empresa.getEmail());
			}
			if (!ObjectUtils.isEmpty(empresa.getMoeda())) {
				empresaFinal.setMoeda(empresa.getMoeda());
			}
			if (!ObjectUtils.isEmpty(empresa.getCnpj())) {
				empresaFinal.setCnpj(empresa.getCnpj());
			}
			if (!ObjectUtils.isEmpty(empresa.getEndereco())) {
				Endereco novoEndereco = empresaFinal.getEndereco();
				if (!ObjectUtils.isEmpty(empresa.getEndereco().getCidade())) {
					novoEndereco.setCidade(empresa.getEndereco().getCidade());
				}
				if (!ObjectUtils.isEmpty(empresa.getEndereco().getComplemento())) {
					novoEndereco.setComplemento(empresa.getEndereco().getComplemento());
				}
				if (!ObjectUtils.isEmpty(empresa.getEndereco().getBairro())) {
					novoEndereco.setBairro(empresa.getEndereco().getBairro());
				}
				if (!ObjectUtils.isEmpty(empresa.getEndereco().getNumero())) {
					novoEndereco.setNumero(empresa.getEndereco().getNumero());
				}
				if (!ObjectUtils.isEmpty(empresa.getEndereco().getCep())) {
					novoEndereco.setCep(empresa.getEndereco().getCep());
				}
				if (!ObjectUtils.isEmpty(empresa.getEndereco().getLogradouro())) {
					novoEndereco.setLogradouro(empresa.getEndereco().getLogradouro());
				}
			}
			return new ResponseEntity<>(empresaService.putEmpresa(empresaFinal), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/empresas/{empresaId}")
	public ResponseEntity<?> deleteEmpresa(@PathVariable(value = "empresaId") UUID empresaId) {
		Optional<Empresa> empresa = empresaService.getEmpresaById(empresaId);
		if (empresa.isPresent()) {
			empresaService.deleteEmpresa(empresaId);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	private static String straightenCnpj(String cnpj) {
		cnpj = cnpj.replace(".","");
		cnpj = cnpj.replace("/","");
		cnpj = cnpj.replace("-","");
		return cnpj;
	}
}
