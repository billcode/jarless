package br.com.bc.jarless.web.controller;

import java.util.List;

import br.com.bc.rest.ServiceEngine;
import br.com.bc.rest.model.ServiceDefinition;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class AdminController {
	
	private final Result result;
	
	public AdminController(Result result) {
		this.result = result;
	}
	
	@Get @Path("/servicos/list")
    public void listAllServices() {
		
		List<ServiceDefinition> list = ServiceEngine.getInstance().getServices();;
    	result.include("servicos", list);
    }

}
