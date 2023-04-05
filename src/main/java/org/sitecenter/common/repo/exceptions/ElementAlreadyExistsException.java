package org.sitecenter.common.repo.exceptions;
 
public class ElementAlreadyExistsException
    extends RuntimeException {

    public ElementAlreadyExistsException() {}

    public ElementAlreadyExistsException(String msg)
    {
        super(msg);
    }
}