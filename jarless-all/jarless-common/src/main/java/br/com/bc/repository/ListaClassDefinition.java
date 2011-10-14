package br.com.bc.repository;

import java.io.Serializable;
import java.util.ArrayList;

import br.com.bc.rest.model.ClassDefinition;

public class ListaClassDefinition implements Serializable {

	private ArrayList listaServiceDefinition = new ArrayList();

	public void add(ClassDefinition pes) {
		listaServiceDefinition.add(pes);
	}

	public void remove(ClassDefinition pes) {
		listaServiceDefinition.remove(pes);
	}

	public ClassDefinition get(int i) {
		return (ClassDefinition) listaServiceDefinition.get(i);
	}

	public int size() {
		return listaServiceDefinition.size();
	}
	
}
