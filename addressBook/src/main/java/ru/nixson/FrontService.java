package ru.nixson;


import javax.ws.rs.*;

@Path("/")
public interface FrontService {
    @GET
    @Path("auth")
    public String auth(
            @QueryParam("login") String login,
            @QueryParam("password") String password
    );
}
