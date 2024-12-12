package com.ess.api.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    private String resource;
    private String sunResource;

    private String fieldValue;

    public ResourceNotFoundException(String resource, String sunResource, String fieldValue) {
        super(String.format("%s not found with %s : %s", resource, sunResource, fieldValue));
        this.resource = resource;
        this.sunResource = sunResource;
    }
}
