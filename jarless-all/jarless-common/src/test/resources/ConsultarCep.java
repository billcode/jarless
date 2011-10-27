package br.com.bc.service;

import br.com.bc.jarless.Execution;

public class ConsultarCep implements Execution {

	@Override
	public Object execute(Object request) {
		
		String result = "CEP nao encontrado";
		
		String cep = "";
		if (request instanceof String) {
			cep = (String) request;
		}
		
		if (cep.equals("05458001")) {
			result = "Av Diogenes R de Lima";
		}
		
		return result;
	}
	

}
