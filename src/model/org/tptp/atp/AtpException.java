package org.tptp.atp;

public class AtpException extends RuntimeException {
    public AtpException(String msg) { super(msg);}
    public AtpException(String msg, Throwable t) { super(msg, t);}
    public AtpException(Throwable t) { super(t); }
}