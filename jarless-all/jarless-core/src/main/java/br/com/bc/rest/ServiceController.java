package br.com.bc.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.bc.rest.model.ServiceDefinition;


@Path("/service")
public class ServiceController {

	@GET
	@Path("/execute/{param}/{request}")
	public Response printMessage(@PathParam("param") String serviceName,
								 @PathParam("request") String serviceRequest) {

		String result = "";
		
		try {
			
			result = ServiceEngine.getInstance().executeService(serviceName, serviceRequest);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Response.status(200).entity(result).build();

	}
	
	


//	@GET
//	@Path("/list")
//	@Produces("application/json")
//	public ServiceDefinition getProductInJSON() {
//
//		Product product = new Product();
//		product.setName("iPad 3");
//		product.setQty(999);
//		
//		return null; 
//	}

	
	
	@GET
	@Path("/list")
	@Produces("text/plain")
	public Response listAllServices() {
		//@Produces("application/json")
		
		List<ServiceDefinition> services = ServiceEngine.getInstance().getServices();
		
		List<String> result = new ArrayList<String>();
		for (ServiceDefinition service : services) {
			result.add(service.getName());
		}
		
		return Response.status(200).entity(result).build();
	}
	

	
	
}