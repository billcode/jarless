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

import br.com.bc.rest.model.ServiceDefinition;


@Path("/service")
public class ServiceController {

	@GET
	@Path("/execute/{param}/{request}")
	public Response execute(@PathParam("param") String serviceName,
								 @PathParam("request") String serviceRequest) {

		String result = "";
		
		try {
			
			result = ServiceEngine.getInstance().executeService(serviceName, serviceRequest);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

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
		try {
			
			String content = readContent(is);
			ServiceEngine.getInstance().deployServiceJson(content);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	protected String readContent(InputStream is) throws IOException {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	      sb.append(line);
	    }
	    is.close();
	    return sb.toString();		
	}

	
}