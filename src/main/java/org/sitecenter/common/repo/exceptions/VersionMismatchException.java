package org.sitecenter.common.repo.exceptions;
 
public class VersionMismatchException
    extends RuntimeException {

    public VersionMismatchException() {}

    public VersionMismatchException(String msg)
    {
        super(msg);
    }
}