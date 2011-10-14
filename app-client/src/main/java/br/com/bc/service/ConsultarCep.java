package br.com.bc.service;

import java.io.IOException;
import java.io.StringWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import br.com.bc.rest.Action;

public class ConsultarCep implements Action {

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
	
	
	public String geraJson() throws IOException {
		JSONArray data = new JSONArray();
		
		JSONObject cd = new JSONObject();
		cd.put("data", data);
		cd.put("name","br.com.bc.service.ConsultarCep");
		cd.put("source", "");
		
		JSONArray list = new JSONArray();
		list.add(cd);
		
		JSONObject pd = new JSONObject();
		pd.put("classes", list);
		pd.put("name","ConsultarCep");
		pd.put("request","");
		pd.put("response","");
		
		StringWriter out = new StringWriter();
		JSONValue.writeJSONString(pd, out);
		String jsonText = out.toString();
		
		return jsonText;		
	}
}
