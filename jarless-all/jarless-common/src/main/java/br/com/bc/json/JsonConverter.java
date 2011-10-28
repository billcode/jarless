package br.com.bc.json;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import br.com.bc.jarless.exception.JarlessException;
import br.com.bc.rest.model.ClassDefinition;
import br.com.bc.rest.model.ServiceDefinition;

public class JsonConverter {
	
	private String binfolder = null;
	
	private String binExtension = null;
	
	JSONParser parser = new JSONParser();
	ContainerFactory containerFactory = null;
	
	public JsonConverter() {
		containerFactory = new ContainerFactory() {
			public List creatArrayContainer() {
				return new LinkedList();
			}

			public Map createObjectContainer() {
				return new LinkedHashMap();
			}

		};	
	}
	
	public void setBinFolder(String binfolder) {
		this.binfolder = binfolder; 
	}

	public void setBinExtension(String binExtension) {
		this.binExtension = binExtension;
	}	
	
	
	public String geraJson(String classname, String request, String response) {
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
			
			JSONObject pd = new JSONObject();
			pd.put("name", classname.replace(".java", ""));
			pd.put("main_class", cd);
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

		String fileName = classname.replace(".java", "").replaceAll("\\.", "/");

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
	

	
	
	
	public ServiceDefinition getServiceDefinitionFrom(String jsonText) {

		ServiceDefinition pd = new ServiceDefinition();

		try {

			Map json = (Map) parser.parse(jsonText, containerFactory);
			Iterator iter = json.entrySet().iterator();

			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();

				if (entry.getKey().equals("name")) {
					pd.setName(entry.getValue().toString());
					
				} else if (entry.getKey().equals("request")) {
					pd.setRequest(entry.getValue().toString());
					
				} else if (entry.getKey().equals("response")) {
					pd.setResponse(entry.getValue().toString());
					
				} else if (entry.getKey().equals("main_class")) {
					LinkedHashMap<String, Object> entryChild = (LinkedHashMap) entry.getValue();

					ClassDefinition mainClass = new ClassDefinition();
					mainClass.setName(entryChild.get("name").toString());
					
					LinkedList<Byte> datalist = (LinkedList<Byte>) entryChild.get("data");
					
					byte[] newdata = new byte[datalist.size()];
					int index = 0;
					
					for(int i=0; i < datalist.size(); i++) {
						String newvalue = String.valueOf(datalist.get(i)); 
						newdata[i] = Byte.parseByte(newvalue);
					}
					mainClass.setData(newdata);
					pd.setMainClass(mainClass);
				}
			}

		} catch (ParseException pe) {
			throw new JarlessException("Error parsing json. ", pe);
		}
		
		return pd;
	}	
}
