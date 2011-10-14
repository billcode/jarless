package br.com.bc.rest;

import br.com.bc.repository.Repository;

public class ServiceContext {

	ServiceClassLoader classLoader;
	
	Repository repository;
	
	public ServiceContext(Repository repository) {
		this.repository = repository;
	}
	
	
	public ServiceClassLoader getClassLoader() throws Exception {

		if (classLoader == null) {
			classLoader = new ServiceClassLoader(getClass().getClassLoader(), repository);
		}

		return classLoader;
	}	
}