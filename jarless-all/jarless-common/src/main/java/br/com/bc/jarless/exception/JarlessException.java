package br.com.bc.jarless.exception;

/**
 * The base Exception for all Jarless exceptions.
 *  
 * @author roger camargo
 */
public class JarlessException extends RuntimeException {

	private static final long serialVersionUID = 2182137500395598103L;

	public JarlessException(Throwable cause) {
        super(cause);
    }

    public JarlessException(String message) {
        super(message);
    }

    public JarlessException(String message, Throwable cause) {
        super(message, cause);
    }
}
