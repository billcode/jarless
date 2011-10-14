package br.com.bc.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import br.com.bc.rest.ServiceEngine;

public class ExecuteServiceTest {

	@Test
	public void executaServicoConsultaCep() throws Exception {
		String servico = "ServicoConsultaCep";

		String result = ServiceEngine.getInstance().executeService(servico, "11111111");
		assertNotNull(result);
		assertEquals("CEP nao encontrado", result);
		
		result = ServiceEngine.getInstance().executeService(servico, "05458001");
		assertNotNull(result);
		assertEquals("Av Diogenes R de Lima", result);		
	}
	

	
}
