package br.com.bc.rest.model;

import java.io.Serializable;
import java.util.List;

public class ServiceDefinition implements Serializable {

	private static final long serialVersionUID = 5602561739532314911L;

	private String name;
	
	private String request;

	private String response;

	private ClassDefinition mainClass;

	public ServiceDefinition() {
	}
	
	public ServiceDefinition(String name, String request, String response, ClassDefinition mainClass) {
		this.name = name;
		this.request = request;
		this.response = response;
		this.mainClass = mainClass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String newRequest) {
		request = newRequest;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String newResponse) {
		response = newResponse;
	}

	public ClassDefinition getMainClass() {
		return mainClass;
	}

	public void setMainClass(ClassDefinition mainClass) {
		this.mainClass = mainClass;
	}
	
}