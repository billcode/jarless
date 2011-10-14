package br.com.bc.jarless;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import br.com.bc.rest.ServiceEngine;
import br.com.bc.rest.model.ServiceDefinition;

/**
 * @goal publish
 */
public class PublishMojo extends AbstractMojo {
	
	private static final char FOLDER_SEPARATOR = '/';
	
	/**
	 * @required
	 * @parameter expression="${file}" 
	 */
	private String file;
	
	private String service;

    public void execute() throws MojoExecutionException {
    	
    	int separator = file.lastIndexOf(FOLDER_SEPARATOR);
    	
    	if (separator > 0) {
    		service = file.substring(separator + 1);
    	}
    	
    	file += ".json";
    	
		display(">>>>>>>>>>>>> JARLESS - PUBLISH");
		display("  file    : " + file);
		display("  service : " + service);
		
		try {
			
			ServiceEngine.getInstance().deployServiceJson(file);
			ServiceDefinition newPd = ServiceEngine.getInstance().getService(service);
			
			
			if (newPd != null) {
				display("  Publishing OK");
				display("  -------------");
				display("  name    : " + newPd.getName());
				display("  request : " + newPd.getRequest());
				display("  response: " + newPd.getResponse());
				display("  classes : " + newPd.getClasses());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new MojoExecutionException("Error publishing: " + e.getMessage());
		}

		

    }
    
    
	private void display(String message) {
		getLog().info(message);
	}
	
	//getters and setters
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}		
}
