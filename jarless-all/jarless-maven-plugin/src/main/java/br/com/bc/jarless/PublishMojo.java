package br.com.bc.jarless;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import br.com.bc.json.JsonConverter;

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
	
	private String server = "http://localhost:8080";
	
	private static final int ERROR_READ_BUFFER_SIZE = 1024;

    public void execute() throws MojoExecutionException {
    	
    	int separator = file.lastIndexOf(FOLDER_SEPARATOR);
    	
    	if (separator > 0) {
    		service = file.substring(separator + 1);
    	}
    	
		display(">>>>>>>>>>>>> JARLESS - PUBLISH");
		display("  file    : " + file);
		display("  service : " + service);
		
		try {
			
			JsonConverter jsonConverter = new JsonConverter();
			jsonConverter.setBinFolder("target/classes/");
			jsonConverter.setBinExtension(".class");
			
			String content = jsonConverter.geraJson(file, "", "");
			display("  json ok");
			
			//connecting to server
			String uri = server + "/jarless-core/service/publish";
			URL url;
			
			display("  connecting to server: " + server);
			url = new URL(uri);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("PUT");  
			conn.setRequestProperty("Content-Type", "application/text");  
			conn.setDoOutput(true);  
			conn.getOutputStream().write(content.getBytes());
			
			int responseCode = conn.getResponseCode();  
			if (HttpURLConnection.HTTP_OK != responseCode
					&& HttpURLConnection.HTTP_NO_CONTENT != responseCode) {  
				ByteArrayOutputStream errorBuffer = new ByteArrayOutputStream();  
				
				int read;  
				byte[] readBuffer = new byte[ERROR_READ_BUFFER_SIZE];  
				InputStream errorStream = conn.getErrorStream();  
				while (-1 != (read = errorStream.read(readBuffer))) {  
					errorBuffer.write(readBuffer, 0, read);  
				}  
				
				display("Request failed, HTTP " + responseCode + ": " + conn.getResponseMessage());  
			}  
			
			display("  publishing ok");
			
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
