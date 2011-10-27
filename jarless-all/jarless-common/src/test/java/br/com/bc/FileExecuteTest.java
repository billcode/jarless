package br.com.bc;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import br.com.bc.rest.ServiceEngine;

public class FileExecuteTest {
	
	private FilePublishTest PUBLISH = new FilePublishTest();
	
	private ServiceEngine serviceEngine = null;

	@Before public void init() {
		serviceEngine = ServiceEngine.getInstance();
		if (serviceEngine.getRepository() == null) {
			serviceEngine.setRepository(FilePublishTest.REPOSITORY_MOCK);
		}
	}	
	
	@Test public void executarServicoNaoExistente() throws Exception {
		
		//dado o servico
		String service = "ConsultarCep";
		
		try {
			
			//quando executamos
			String result = serviceEngine.executeService(service, "05458001");
			
		} catch (Exception ex) {
			//entao deve retornar um erro de servico nao encontrado
			assertEquals("Service not found: ConsultarCep", ex.getMessage());
		}

	}
	
	@Test 
	public void executarServicoExistente() throws Exception {
		
		//dado o servico publicado
		String service = "ConsultarCep";
		PUBLISH.publishJson();
		
		//quando executamos
		String result = serviceEngine.executeService(service, "05458001");
		
		//entao deve retornar um endereco valido
		assertEquals("Av Diogenes R de Lima", result);

	}
	
	
	
}
