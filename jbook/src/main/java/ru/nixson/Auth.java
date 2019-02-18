package ru.nixson;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
public class Auth {

    @POST
    @Path("/token")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authResp(AuthPair ap){
        String login = ap.getLogin();
        String password = ap.getPass();
        Book book;

        if(login.equals("nixson") && password.equals("456123")){
            book = new Book();
            book.setLogin("nixson");
        } else {
            try {
                book = AddressBook.auth(login, password);
            } catch (Exception ex){
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            if(book == null)
                return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.ok().header(HttpHeaders.AUTHORIZATION,AddressBook.getToken(book)).entity(book).build();
    }
}
