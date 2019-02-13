package ru.nixson;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Array;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;


@Controller
@RequestMapping(value = "/auth")
public class Auth {

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity post(
            @RequestBody String uri
    ) throws NoSuchAlgorithmException {
        HashMap<String, String> map = QueryBuilder.parse(uri);
        String login = map.get("login");
        String password = map.get("password");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        Book resp;
        if(login.equals("nixson") && password.equals("456123")){
            Book admin = new Book();
            admin.setLogin(login);
            admin.setPassword(password);
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
