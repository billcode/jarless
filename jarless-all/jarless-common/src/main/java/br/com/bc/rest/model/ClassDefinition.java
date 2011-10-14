package br.com.bc.rest.model;

import java.io.Serializable;

public class ClassDefinition implements Serializable {

	private static final long serialVersionUID = -6112677460345121563L;

	private byte[] data;

	private String source;
	
	private String name;

	public ClassDefinition() {
		
	}
	
	public ClassDefinition(String name, String source, byte[] data) {
		this.name = name;
		this.source = source;
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String newName) {
		name = newName;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] newData) {
		data = newData;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String newSource) {
		source = newSource;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}