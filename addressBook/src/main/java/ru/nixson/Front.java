package ru.nixson;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import java.security.NoSuchAlgorithmException;

@ApplicationScoped
@Path("/")
public class Front implements FrontService{
    @GET
    @Path("auth")
    @Override
    public String auth (
            @QueryParam("login") String login,
            @QueryParam("password") String password
    ) {
        try{
            Book ab = AddressBook.auth(login,password);
            if(ab==null){
                return "Ошибка авторизации";
            }
            return "";
        } catch (NoSuchAlgorithmException err){
            return "Ошибка";
        }
    }
}
