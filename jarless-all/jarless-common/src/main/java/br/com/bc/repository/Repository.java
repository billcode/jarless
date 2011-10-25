package br.com.bc.repository;

import java.util.List;

import br.com.bc.rest.model.ClassDefinition;
import br.com.bc.rest.model.ServiceDefinition;

public interface Repository {

	void addClass(ClassDefinition classDefinition);

	void addService(ServiceDefinition serviceDefinition);
	
	ServiceDefinition getServiceDefinition(String name);

	ClassDefinition getClassDefinition(String name);
	
	List<ServiceDefinition> getServices();
	
}