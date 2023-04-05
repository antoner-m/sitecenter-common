package org.sitecenter.common.repo.exceptions;
 
public class NoSuchElementExistsException
    extends RuntimeException {
 
    public NoSuchElementExistsException() {}
 
    public NoSuchElementExistsException(String msg)
    {
        super(msg);
    }
}