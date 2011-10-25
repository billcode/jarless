package br.com.bc;

import java.util.ArrayList;
import java.util.List;

import br.com.bc.jarless.repository.Repository;
import br.com.bc.rest.model.ClassDefinition;
import br.com.bc.rest.model.ServiceDefinition;

public class RepositoryMock implements Repository {

	private static List<ServiceDefinition> services = new ArrayList<ServiceDefinition>();
	
	@Override
	public void addService(ServiceDefinition serviceDefinition) {
		services.add(serviceDefinition);
	}

	@Override
	public ServiceDefinition getServiceDefinition(String name) {

		ServiceDefinition result = null;
		for (ServiceDefinition service : services) {
			if (service.getName().equals(name)) {
				result = service;
				break;
			}
		}
		return result;
	}

	@Override
	public ClassDefinition getClassDefinition(String name) {
		
		ClassDefinition result = null;
		for (ServiceDefinition service : services) {
			ClassDefinition mainClass = service.getMainClass();
			if (name.equals(mainClass.getName())) {
				result = mainClass;
				break;
			}
		}		
		
		return result;
	}

	@Override
	public List<ServiceDefinition> getServices() {
		return services;
	}

}
