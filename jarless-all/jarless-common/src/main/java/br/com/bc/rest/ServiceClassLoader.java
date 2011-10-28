package br.com.bc.rest;

import br.com.bc.jarless.exception.JarlessException;
import br.com.bc.jarless.repository.Repository;
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
		
		name = name.replace(".java", "");
		
		return defineClass(name, data, 0, data.length);
	}
	
	
	public Object loadServiceClass(String actionName) {
		Object result = null;
		
		try {
			
			result = super.loadClass(actionName).newInstance();

		} catch (InstantiationException iEx) {
			throw new JarlessException("Class Error 1: " + actionName, iEx);

		} catch (IllegalAccessException iaEx) {
			throw new JarlessException("Class Error 2: " + actionName, iaEx);

		} catch (ClassNotFoundException cnfEx) {
			throw new JarlessException("Class Error 3: " + actionName, cnfEx);

		} catch (NoClassDefFoundError ncdfErr) {
			throw new JarlessException("Class Error 4: " + actionName, ncdfErr);
			
		} catch (Exception ncdfErr) {
			throw new JarlessException("Class Error 5: " + actionName, ncdfErr);
		}
		return result;
	}	
}