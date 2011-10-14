package br.com.bc.repository;

import br.com.bc.rest.model.ClassDefinition;
import br.com.bc.rest.model.ServiceDefinition;

public interface Repository {

	void addClass(ClassDefinition classDefinition);

	void addService(ServiceDefinition serviceDefinition);
	
	ServiceDefinition getServiceDefinition(String name) throws Exception;

	ClassDefinition getClassDefinition(String name) throws Exception;
	
}