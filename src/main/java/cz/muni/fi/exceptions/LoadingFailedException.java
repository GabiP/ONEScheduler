package cz.muni.fi.exceptions;

/**
 * The exception thrown when initializing the ApplicationContext.
 * 
 * @author Andras Urge
 */
public class LoadingFailedException extends Exception {

    public LoadingFailedException(String message) {
        super(message);
    }

    public LoadingFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
