package br.com.bc.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.bc.jarless.exception.JarlessException;
import br.com.bc.jarless.repository.prevayler.RepositoryImpl;
import br.com.bc.rest.model.ServiceDefinition;


@Path("/service")
public class ServiceController {

	public ServiceController() {
		init();		
	}
	
	@GET
	@Path("/execute/{param}/{request}")
	public Response execute(@PathParam("param") String serviceName,
								 @PathParam("request") String serviceRequest) {
		
		String result = ServiceEngine.getInstance().executeService(serviceName, serviceRequest);

		return Response.status(200).entity(result).build();

	}
	
	





	@GET
	@Path("/list")
	@Produces("text/plain")
	public Response listAllServices() {
		List<ServiceDefinition> services = ServiceEngine.getInstance().getServices();
		
		List<String> result = new ArrayList<String>();
		for (ServiceDefinition service : services) {
			result.add(service.getName());
		}
		
		return Response.status(200).entity(result).build();
	}
	
	
	@PUT
	@Path("/publish")
	@Consumes("application/text")
	public void publish(InputStream is) {
		String content = readContent(is);
		ServiceEngine.getInstance().publishServiceJson(content);
	}
	
	
	protected String readContent(InputStream is) {
		
		StringBuilder result = new StringBuilder();
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			is.close();
			
		} catch (IOException e) {
			throw new JarlessException("Error reading class content", e);
		}
		
	    return result.toString();		
	}

	private void init() {
		if (ServiceEngine.getInstance().getRepository() == null) {
			ServiceEngine.getInstance().setRepository(RepositoryImpl.getInstance());
		}
	}	
}