package br.com.bc;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import br.com.bc.rest.ServiceEngine;

public class FilePublishTest {
	
	private ServiceEngine serviceEngine = null;
	
	public static RepositoryMock REPOSITORY_MOCK = new RepositoryMock();

	@Before public void init() {
		serviceEngine = ServiceEngine.getInstance();
		if (serviceEngine.getRepository() == null) {
			serviceEngine.setRepository(REPOSITORY_MOCK);
		}
	}
	
	@Test public void publishJson() throws Exception {
		
		/** Servico 1 */
		//dado o JSON do servico
		String json = FileConvertTest.CONSULTAR_CEP_JSON;
		
		//quando efetuamos a publicacao
		boolean result = ServiceEngine.getInstance().publishServiceJson(json);
		
		//entao a publicacao devera ser efetivada
		assertEquals(Boolean.TRUE, result);
		
		/** Servico 2 */
		json = FileConvertTest.LISTAR_CONTATOS_JSON;
		result = ServiceEngine.getInstance().publishServiceJson(json);
		assertEquals(Boolean.TRUE, result);
		
	}
	
	
}
