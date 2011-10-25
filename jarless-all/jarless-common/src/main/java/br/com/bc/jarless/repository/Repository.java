package br.com.bc.jarless.repository;

import java.util.List;

import br.com.bc.rest.model.ClassDefinition;
import br.com.bc.rest.model.ServiceDefinition;

public interface Repository {

	void addService(ServiceDefinition serviceDefinition);
	
	ServiceDefinition getServiceDefinition(String name);

	ClassDefinition getClassDefinition(String name);
	
	List<ServiceDefinition> getServices();
	
}