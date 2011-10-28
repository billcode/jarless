package br.com.bc.rest;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.json.simple.JSONValue;

import br.com.bc.jarless.Execution;
import br.com.bc.jarless.exception.JarlessException;
import br.com.bc.jarless.repository.Repository;
import br.com.bc.json.JsonConverter;
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
		JsonConverter jsonConverter = new JsonConverter();
		
		ServiceDefinition pd = jsonConverter.getServiceDefinitionFrom(content);
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
		Object obj = ctx.getClassLoader().loadServiceClass(actionName);

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

	
	
	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
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