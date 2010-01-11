package org.tptp.model;

public class ModelException extends RuntimeException {
    public ModelException() {}
    public ModelException(String msg) { super(msg); }
    public ModelException(String msg, Throwable t) { super(msg, t); }
    public ModelException(Throwable t) { super(t); }
    
}