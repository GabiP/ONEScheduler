/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
