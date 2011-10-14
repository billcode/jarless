package br.com.bc.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.bc.rest.model.ServiceDefinition;


@Path("/execute")
public class ServiceController {

	@GET
	@Path("/service/{param}/{request}")
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
	
	


	@GET
	@Path("/get")
	@Produces("application/json")
	public ServiceDefinition getProductInJSON() {
//
//		Product product = new Product();
//		product.setName("iPad 3");
//		product.setQty(999);
//		
		return null; 
	}
//
//	@POST
//	@Path("/post")
//	@Consumes("application/json")
//	public Response createProductInJSON(Product product) {
//
//		String result = "Product created : " + product;
//		return Response.status(201).entity(result).build();
//		
//	}
//		
	

	
	
}