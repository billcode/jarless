package br.com.bc.rest;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import br.com.bc.jarless.Execution;
import br.com.bc.jarless.exception.JarlessException;
import br.com.bc.jarless.repository.Repository;
import br.com.bc.rest.model.ClassDefinition;
import br.com.bc.rest.model.ServiceDefinition;

public class ServiceEngine {

	private static final ServiceEngine INSTANCE = new ServiceEngine();
	
	private Repository repository = null;

	private ServiceEngine() {
	}

	public static ServiceEngine getInstance() { return INSTANCE; }
	
	public void publishService(ServiceDefinition service) {
		repository.addService(service);
	}

	public ServiceDefinition getService(String name) {
		ServiceDefinition service = repository.getServiceDefinition(name);
		
		if (service == null) {
			throw new JarlessException("Service not found: " + name);
		}
		
		return service;
	}

	
	
	
	public boolean publishServiceJson(String content) {
		ServiceDefinition pd = getServiceDefinitionFrom(content);
		publishService(pd);
		return true;
	}
	
	
	
	
	public String executeService(String name, String request) {
		String result = null;
		
		System.out.println("executando servico " + name);
		
		ServiceDefinition pd = getService(name);
		String actionName = pd.getMainClass().getName();

		ServiceContext ctx = new ServiceContext(repository);

		// carrega a classe e cria uma nova instancia
		Object obj = loadClass(actionName, ctx);

		Execution execution = (Execution) obj;
		
		Object response = execution.execute(request);
		
		result = prepareResponse(pd, response);
		
		return result;
	}

	private String prepareResponse(ServiceDefinition pd, Object response) {
		boolean jsonresponse = (pd.getResponse() != null && pd.getResponse().equals("json"));
		
		if  (jsonresponse) {
			return prepareResponseJson(response);
		} else {
			return prepareResponseString(response);
		}
	}
	
	
	private String prepareResponseString(Object response) {
		return String.valueOf( response );
	}
	
	private String prepareResponseJson(Object response) {
		StringWriter out = new StringWriter();
		try {
			JSONValue.writeJSONString(response, out);
		} catch (IOException e) {
			throw new JarlessException("Error generating response", e);
		}
		return out.toString();
	}
	

	private Object loadClass(String actionName, ServiceContext ctx) {
		Object result = null;
		
		try {
			
			result = ctx.getClassLoader().loadClass(actionName).newInstance();

		} catch (InstantiationException iEx) {
			throw new JarlessException("Class Error 1: " + actionName, iEx);

		} catch (IllegalAccessException iaEx) {
			throw new JarlessException("Class Error 2: " + actionName, iaEx);

		} catch (ClassNotFoundException cnfEx) {
			throw new JarlessException("Class Error 3: " + actionName, cnfEx);

		} catch (NoClassDefFoundError ncdfErr) {
			throw new JarlessException("Class Error 4: " + actionName, ncdfErr);
			
		} catch (Exception ncdfErr) {
			throw new JarlessException("Class Error 5: " + actionName, ncdfErr);
		}
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
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
				} else if (entry.getKey().equals("main_class")) {
					
					//LinkedList list = (LinkedList) entry.getValue();
					//List<ClassDefinition> classes = getClassesDefinitionFrom(list);
					
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
			System.out.println(pe);
		}
		
		return pd;
	}

	
	public List<ServiceDefinition> getServices() {
		List<ServiceDefinition> result = null;
		
		try {
			result = repository.getServices();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}