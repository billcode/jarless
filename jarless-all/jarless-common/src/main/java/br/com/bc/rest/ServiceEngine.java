package br.com.bc.rest;

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

	
	
	
	public void deployServiceJson(String content) throws Exception {
		ServiceDefinition pd = getServiceDefinitionFrom(content);
		deployService(pd);
	}
	
	
	
	
	public String executeService(String name, String request) throws Exception {
		String result = null;
		Action action = null;
		
		System.out.println("executando servico " + name);
		
		ServiceDefinition pd = getService(name);
		String actionName = pd.getMainClass().getName();

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