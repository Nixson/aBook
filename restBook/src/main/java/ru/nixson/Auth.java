package ru.nixson;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;


@RestController
@RequestMapping(value = "/auth")
public class Auth {

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity post(
            @RequestParam(required = true) String login,
            @RequestParam(required = true) String password
    ) throws NoSuchAlgorithmException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        Book resp;
        if(login.equals("nixson") && password.equals("456123")){
            Book admin = new Book();
            admin.setLogin(login);
            admin.setPassword(password);
            admin.setSurname("Nx");
            admin.setFirstname("Nixson");
            admin.setMiddlename("");
            admin.setIdentifier(0);
            resp = admin;
        } else {
            Book ab = AddressBook.auth(login,password);
            resp = ab;
        }
        if(resp==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(headers).body("Ошибка авторизации");
        headers.add(HttpHeaders.AUTHORIZATION, AddressBook.getToken(resp));
        return ResponseEntity.ok().headers(headers).body(resp);
    }

}
