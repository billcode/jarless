package br.com.bc.jarless.repository.prevayler;

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

	private ClassDefinition mainClass;

	public AdicionarServiceDefinition(String name, String request, String response, ClassDefinition mainClass) {
		super();
		this.name = name;
		this.request = request;
		this.response = response;
		this.mainClass = mainClass;
	}

	public void executeOn(Object system, Date arg1) {
		((ListaServiceDefinition) system).add(new ServiceDefinition(name, request, response, mainClass));
	}
}
