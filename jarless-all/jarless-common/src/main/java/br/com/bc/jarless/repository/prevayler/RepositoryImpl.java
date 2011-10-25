package br.com.bc.jarless.repository.prevayler;

import java.util.ArrayList;
import java.util.List;

import br.com.bc.jarless.repository.Repository;
import br.com.bc.rest.model.ClassDefinition;
import br.com.bc.rest.model.ServiceDefinition;

public class RepositoryImpl implements Repository {
	
	private static RepositoryImpl INSTANCE = null;
	
	public static RepositoryImpl getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new RepositoryImpl();
			PrevaylerController.INSTANCE.initialize();
		}
		return INSTANCE;
	}
	
	public PrevaylerController getPrevaylerController() {
		return PrevaylerController.INSTANCE;
	}	
	
	
	
	@Override
	public void addService(ServiceDefinition serviceDefinition) {
		getPrevaylerController().getPrevayler().execute(new AdicionarServiceDefinition(serviceDefinition.getName(), serviceDefinition.getRequest(), 
				serviceDefinition.getResponse(), serviceDefinition.getMainClass()));

		ListaServiceDefinition lista = ((ListaServiceDefinition) getPrevaylerController().getPrevayler().prevalentSystem());
		System.out.println(lista.size());		
		
	}
	
	
	
	@Override
	public ServiceDefinition getServiceDefinition(String name) {
		ServiceDefinition result = null;
		
		ListaServiceDefinition lista = ((ListaServiceDefinition) getPrevaylerController().getPrevayler().prevalentSystem());
		
		for (int i=0; i < lista.size(); i++) {
			ServiceDefinition pd = lista.get(i);
			if (pd.getName().equals(name)) {
				result = pd;
				break;
			}
		}
		
		return result;
	}



	@Override
	public ClassDefinition getClassDefinition(String name) {
		
		ClassDefinition result = null;
		
		ListaServiceDefinition lista = ((ListaServiceDefinition) getPrevaylerController().getPrevayler().prevalentSystem());
		
		for (int i=0; i < lista.size(); i++) {
			ServiceDefinition pd = lista.get(i);
			
			ClassDefinition mainClass = pd.getMainClass();
			
			//TODO: esta condicao devera ser corrigida
			//if (name.contains(cd.getName())) {
			if (name.equals(mainClass.getName())) {
				result = mainClass;
				break;
			}
			
		}		
		
		return result;
	}	
	
	
	
	


	@Override
	public List<ServiceDefinition> getServices() {
		List<ServiceDefinition> result = new ArrayList<ServiceDefinition>();
		
		ListaServiceDefinition lista = ((ListaServiceDefinition) getPrevaylerController().getPrevayler().prevalentSystem());
		
		for (int i=0; i < lista.size(); i++) {
			ServiceDefinition pd = lista.get(i);
			result.add(pd);
		}
		
		return result;

	}




}
