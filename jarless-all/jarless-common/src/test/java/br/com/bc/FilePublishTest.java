package br.com.bc;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import br.com.bc.rest.ServiceEngine;

public class FilePublishTest {
	
	private ServiceEngine serviceEngine = null;

	@Before public void init() {
		serviceEngine = ServiceEngine.getInstance();
		if (serviceEngine.getRepository() == null) {
			serviceEngine.setRepository(new RepositoryMock());
		}
	}
	
	@Test public void publishJson() throws Exception {
		
		//dado o JSON do servico
		String json = FileConvertTest.CONSULTAR_CEP_JSON;
		
		//quando efetuamos a publicacao
		boolean result = ServiceEngine.getInstance().publishServiceJson(json);
		
		//entao a publicacao devera ser efetivada
		assertEquals(Boolean.TRUE, result);
	}
	
	
}
