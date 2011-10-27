package br.com.bc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.bc.json.JsonConverter;

public class FileConvertTest {

	public static final String CONSULTAR_CEP_JSON = "{\"response\":\"\"," +
			"\"main_class\":{\"source\":\"\",\"name\":\"ConsultarCep.java\"," +
			"\"data\":[-54,-2,-70,-66,0,0,0,50,0,41,10,0,9,0,29,8,0,30,8,0,31,7,0,32,8,0,33,10,0,4,0,34,8,0,35,7,0,36,7,0,37,7,0,38,1,0,6,60,105,110,105,116,62,1,0,3,40,41,86,1,0,4,67,111,100,101,1,0,15,76,105,110,101,78,117,109,98,101,114,84,97,98,108,101,1,0,18,76,111,99,97,108,86,97,114,105,97,98,108,101,84,97,98,108,101,1,0,4,116,104,105,115,1,0,14,76,67,111,110,115,117,108,116,97,114,67,101,112,59,1,0,7,101,120,101,99,117,116,101,1,0,38,40,76,106,97,118,97,47,108,97,110,103,47,79,98,106,101,99,116,59,41,76,106,97,118,97,47,108,97,110,103,47,79,98,106,101,99,116,59,1,0,7,114,101,113,117,101,115,116,1,0,18,76,106,97,118,97,47,108,97,110,103,47,79,98,106,101,99,116,59,1,0,6,114,101,115,117,108,116,1,0,18,76,106,97,118,97,47,108,97,110,103,47,83,116,114,105,110,103,59,1,0,3,99,101,112,1,0,13,83,116,97,99,107,77,97,112,84,97,98,108,101,7,0,32,1,0,10,83,111,117,114,99,101,70,105,108,101,1,0,17,67,111,110,115,117,108,116,97,114,67,101,112,46,106,97,118,97,12,0,11,0,12,1,0,18,67,69,80,32,110,97,111,32,101,110,99,111,110,116,114,97,100,111,1,0,0,1,0,16,106,97,118,97,47,108,97,110,103,47,83,116,114,105,110,103,1,0,8,48,53,52,53,56,48,48,49,12,0,39,0,40,1,0,21,65,118,32,68,105,111,103,101,110,101,115,32,82,32,100,101,32,76,105,109,97,1,0,12,67,111,110,115,117,108,116,97,114,67,101,112,1,0,16,106,97,118,97,47,108,97,110,103,47,79,98,106,101,99,116,1,0,27,98,114,47,99,111,109,47,98,99,47,106,97,114,108,101,115,115,47,69,120,101,99,117,116,105,111,110,1,0,6,101,113,117,97,108,115,1,0,21,40,76,106,97,118,97,47,108,97,110,103,47,79,98,106,101,99,116,59,41,90,0,33,0,8,0,9,0,1,0,10,0,0,0,2,0,1,0,11,0,12,0,1,0,13,0,0,0,47,0,1,0,1,0,0,0,5,42,-73,0,1,-79,0,0,0,2,0,14,0,0,0,6,0,1,0,0,0,5,0,15,0,0,0,12,0,1,0,0,0,5,0,16,0,17,0,0,0,1,0,18,0,19,0,1,0,13,0,0,0,-110,0,2,0,4,0,0,0,32,18,2,77,18,3,78,43,-63,0,4,-103,0,8,43,-64,0,4,78,45,18,5,-74,0,6,-103,0,6,18,7,77,44,-80,0,0,0,3,0,14,0,0,0,30,0,7,0,0,0,10,0,3,0,12,0,6,0,13,0,13,0,14,0,18,0,17,0,27,0,18,0,30,0,21,0,15,0,0,0,42,0,4,0,0,0,32,0,16,0,17,0,0,0,0,0,32,0,20,0,21,0,1,0,3,0,29,0,22,0,23,0,2,0,6,0,26,0,24,0,23,0,3,0,25,0,0,0,12,0,2,-3,0,18,7,0,26,7,0,26,11,0,1,0,27,0,0,0,2,0,28]}," +
			"\"request\":\"\",\"name\":\"ConsultarCep\"}";

	@Test public void geraArquivoJson() throws Exception {
		
		//dado um arquivo de servico
		String service_class_name = "ConsultarCep.java";
		String request = "";
		String response = "";
		
		//quando queremos publicar o servico, precisamos da representacao JSON
		JsonConverter jsonConverter = new JsonConverter();
		jsonConverter.setBinFolder("src/test/resources/");
		jsonConverter.setBinExtension(".clazz");
		String result = jsonConverter.geraJson(service_class_name, request, response);
		
		//entao o JSON esperado para o servico acima deve ser
		assertEquals(CONSULTAR_CEP_JSON, result);
		
	}
	
	
}
