package br.com.bc.repository;

import java.util.Date;
import java.util.List;

import org.prevayler.Transaction;

import br.com.bc.rest.model.ClassDefinition;
import br.com.bc.rest.model.ServiceDefinition;

public class AdicionarServiceDefinition implements Transaction {

	private static final long serialVersionUID = -7115676738849382918L;
	
	private String name;

	private String request;

	private String response;

	private List<ClassDefinition> classes;

	public AdicionarServiceDefinition(String name, String request, String response, List<ClassDefinition> classes) {
		super();
		this.name = name;
		this.request = request;
		this.response = response;
		this.classes = classes;
	}

	public void executeOn(Object system, Date arg1) {
		((ListaServiceDefinition) system).add(new ServiceDefinition(name, request, response, classes));
	}
}
