package br.com.bc.rest;

import br.com.bc.repository.Repository;
import br.com.bc.rest.model.ClassDefinition;

public class ServiceClassLoader extends ClassLoader {

	private Repository repository;

	public ServiceClassLoader(ClassLoader newParent, Repository newRepository) {
		super(newParent);
		repository = newRepository;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class findClass(String name) throws ClassNotFoundException {
		byte[] data = null;

		try {
			ClassDefinition classDefinition = repository.getClassDefinition(name);
			data = classDefinition.getData();
		} catch (Exception pdcEx) {
			throw new ClassNotFoundException("Class '" + name + "' not found'!", pdcEx);
		}

		return defineClass(name, data, 0, data.length);
	}
}