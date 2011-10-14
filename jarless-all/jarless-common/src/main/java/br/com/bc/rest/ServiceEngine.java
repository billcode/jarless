package br.com.bc.rest;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import br.com.bc.repository.Repository;
import br.com.bc.repository.RepositoryImpl;
import br.com.bc.rest.model.ClassDefinition;
import br.com.bc.rest.model.ServiceDefinition;

public class ServiceEngine {

	private static final ServiceEngine INSTANCE = new ServiceEngine();
	
	private Repository repository = null;

	private ServiceEngine() {
		repository = new RepositoryImpl();
	}

	public static ServiceEngine getInstance() { return INSTANCE; }
	
	public void deployService(ServiceDefinition service) throws Exception {
		repository.addService(service);
	}

	public ServiceDefinition getService(String name) throws Exception {
		ServiceDefinition service = repository.getServiceDefinition(name);
		
		if (service == null) {
			throw new Exception("Servico nao existe");
		}
		
		return service;
	}

	
	
	
	public void deployServiceJson(String serviceName) throws Exception {
		File file = new File(serviceName);
		String serviceJson = ServiceEngine.getInstance().getContents(file);
	
		ServiceDefinition pd = getServiceDefinitionFrom(serviceJson);
		deployService(pd);
	}
	
	
	
	
	public String executeService(String name, String request) throws Exception {
		String result = null;
		Action action = null;
		
		System.out.println("executando servico " + name);
		
		ServiceDefinition pd = getService(name);
		String actionName = pd.getClasses().get(0).getName();;

		ServiceContext ctx = new ServiceContext(repository);

		// carrega a classe e cria uma nova instancia
		Object obj = null;
		try {
			obj = ctx.getClassLoader().loadClass(actionName).newInstance();

		} catch (InstantiationException iEx) {
			throw new Exception(actionName, iEx);

		} catch (IllegalAccessException iaEx) {
			throw new Exception(actionName, iaEx);

		} catch (ClassNotFoundException cnfEx) {
			throw new Exception(actionName, cnfEx);

		} catch (NoClassDefFoundError ncdfErr) {
			throw new Exception(actionName, ncdfErr);
			
		} catch (Exception ncdfErr) {
			throw new Exception(actionName, ncdfErr);
		}

		action = (Action) obj;
		
		Object response = action.execute(request);
		
		if (pd.getResponse() != null && pd.getResponse().equals("json")) {
			
			//JSONArray jsonArray = JSONArray.fromObject( response );
			//result = jsonArray.toString();
			
			StringWriter out = new StringWriter();
			JSONValue.writeJSONString(response, out);
			result = out.toString();
			
		} else {
			result = String.valueOf( response );
		}
		
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	public ServiceDefinition getServiceDefinitionFrom(String jsonText) {

		ServiceDefinition pd = new ServiceDefinition();

		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory() {
			public List creatArrayContainer() {
				return new LinkedList();
			}

			public Map createObjectContainer() {
				return new LinkedHashMap();
			}

		};

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
				} else if (entry.getKey().equals("classes")) {
					LinkedList list = (LinkedList) entry.getValue();

					List<ClassDefinition> classes = getClassesDefinitionFrom(list);
					pd.setClasses(classes);
				}

			}

			loadServiceClasses(pd);

		} catch (ParseException pe) {
			System.out.println(pe);
		}
		
		return pd;
	}

	private List<ClassDefinition> getClassesDefinitionFrom(LinkedList list) {

		Iterator iter = list.iterator();
		List<ClassDefinition> classes = new ArrayList<ClassDefinition>();

		while (iter.hasNext()) {
			// Map.Entry entry = (Map.Entry) iter.next();
			LinkedHashMap<String, String> entry = (LinkedHashMap) iter.next();

			ClassDefinition classDef = new ClassDefinition();

			classDef.setName(entry.get("name"));
			// if (entry.getKey().equals("name")) {

			// }
			classes.add(classDef);

		}

		return classes;
	}	
	private void loadServiceClasses(ServiceDefinition pd) {

		for (ClassDefinition cd : pd.getClasses()) {
			loadClass(cd);
		}
	}

	private void loadClass(ClassDefinition classdef) {
		String path = "target/classes/";

		String fileName = classdef.getName().replaceAll("\\.", "/");

		File file = new File(path + fileName + ".class");
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

		classdef.setData(classData);
	}

	public String getContents(File aFile) {
		StringBuilder contents = new StringBuilder();

		try {
			BufferedReader input = new BufferedReader(new FileReader(aFile));
			try {
				String line = null; // not declared within while loop
				while ((line = input.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return contents.toString();
	}	

}