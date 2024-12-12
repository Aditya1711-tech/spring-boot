package com.ess.api.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException{
    private String resource;
    private String subResource;

    private String fieldValue;

    public ResourceAlreadyExistsException(String resource, String sunResource, String fieldValue) {
        super(String.format("%s already exists %s : %s", resource, sunResource, fieldValue));
        this.resource = resource;
        this.subResource = sunResource;
    }
}
