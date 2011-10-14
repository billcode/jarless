package br.com.bc.repository;

import java.io.Serializable;
import java.util.ArrayList;

import br.com.bc.rest.model.ServiceDefinition;

public class ListaServiceDefinition implements Serializable {

	private ArrayList listaServiceDefinition = new ArrayList();

	public void add(ServiceDefinition pes) {
		listaServiceDefinition.add(pes);
	}

	public void remove(ServiceDefinition pes) {
		listaServiceDefinition.remove(pes);
	}

	public ServiceDefinition get(int i) {
		return (ServiceDefinition) listaServiceDefinition.get(i);
	}

	public int size() {
		return listaServiceDefinition.size();
	}
	
}
