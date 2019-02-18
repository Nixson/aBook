package ru.nixson;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/usr")
public class Usr {

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addBook(Book book){
        if(book.getIdentifier()==0)
            AddressBook.create(book);
        else
            AddressBook.update(book);
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response list(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String token,
            @QueryParam("find") String find){
        if(!AddressBook.checkToken(token)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.ok().entity(AddressBook.find(find)).build();
    }


}
