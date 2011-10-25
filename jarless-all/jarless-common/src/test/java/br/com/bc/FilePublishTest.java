package br.com.bc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.bc.rest.ServiceEngine;

public class FilePublishTest {

	@Test public void publishJson() throws Exception {
		
		//dado o JSON do servico
		String json = FileConvertTest.CONSULTAR_CEP_JSON;
		
		//quando efetuamos a publicacao
		boolean result = ServiceEngine.getInstance().publishServiceJson(json);
		
		//entao a publicacao devera ser efetivada
		assertEquals(Boolean.TRUE, result);
	}
	
	
}
