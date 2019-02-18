package ru.nixson;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

@Provider
public class Filter implements ContainerRequestFilter {
    public ContainerRequest filter(
            ContainerRequest Req){
        String par = Req.getHeaderValue(HttpHeaders.AUTHORIZATION);
        return Req;
    }

}
