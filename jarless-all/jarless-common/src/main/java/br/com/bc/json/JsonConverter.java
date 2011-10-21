package br.com.bc.json;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import br.com.bc.rest.model.ClassDefinition;

public class JsonConverter {
	
	private String binfolder = null;
	
	private String binExtension = null;

	public String geraJson(String servicename, String request, String response, String classname) {
		String json = null;
		
		try {
			
			byte[] classdata = loadClass(classname);
					
			JSONArray data = new JSONArray();
			for (int i=0; i < classdata.length; i++) {
				data.add(classdata[i]);
			}
			
			JSONObject cd = new JSONObject();
			cd.put("data", data);
			cd.put("name", classname);
			cd.put("source", "");
			
			JSONArray list = new JSONArray();
			list.add(cd);
			
			JSONObject pd = new JSONObject();
			pd.put("classes", list);
			pd.put("name", servicename);
			pd.put("request", request);
			pd.put("response", response);
			
			StringWriter out = new StringWriter();
			JSONValue.writeJSONString(pd, out);
			json = out.toString();			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return json;
	}

	
	private byte[] loadClass(String classname) {

		String fileName = classname.replaceAll("\\.", "/");

		File file = new File(binfolder + fileName + binExtension);
		FileInputStream fis = null;

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		byte[] classData = null;

		try {

			fis = new FileInputStream(file);

			int b;
			while ((b = fis.read()) != -1) {
				out.write(b);
			}

			fis.close();
			out.close();

			classData = out.toByteArray();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return classData;
	}		
	
	public void setBinFolder(String binfolder) {
		this.binfolder = binfolder; 
	}

	public void setBinExtension(String binExtension) {
		this.binExtension = binExtension;
	}
}
