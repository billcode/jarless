package br.com.bc.repository;

import java.util.Date;

import org.prevayler.Transaction;

import br.com.bc.rest.model.ClassDefinition;

public class AdicionarClassDefinition implements Transaction {

	private static final long serialVersionUID = 513311108956727790L;

	private String name;

	private String source;

	private byte[] data;

	public AdicionarClassDefinition(String name, String source, byte[] data) {
		super();
		this.name = name;
		this.source = source;
		this.data = data;
	}

	public void executeOn(Object system, Date arg1) {
		((ListaClassDefinition) system).add(new ClassDefinition(name, source, data));
	}
}
