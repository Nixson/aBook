package ru.nixson;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;


@Controller
@RequestMapping(value = "/auth", produces = "application/json")
public class Auth {

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity post(
            @RequestBody String login,
            @RequestBody String password
    ) throws NoSuchAlgorithmException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        Book ab = AddressBook.auth(login,password);
        if(ab==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(headers).body("Ошибка авторизации");
        headers.add(HttpHeaders.AUTHORIZATION, AddressBook.getToken(ab));
        return ResponseEntity.ok().headers(headers).body(ab);
    }

}
