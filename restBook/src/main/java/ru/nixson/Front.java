package ru.nixson;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/usr")
public class Front {

    private boolean checkToken(String authorization){
        return !AddressBook.checkToken(authorization);
    }
    private ResponseEntity authError(){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(headers).body("Ошибка авторизации");
    }

    @RequestMapping(value= "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getBook(
            @RequestHeader("Authorization") String authorization,
            @PathVariable int id
    ){
        if(checkToken(authorization)){
            return authError();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        Book ab = AddressBook.byId(id);
        if(ab==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(headers).body("Нет такого метода");
        return ResponseEntity.ok().headers(headers).body(ab);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity putBook(
            @RequestHeader("Authorization") String authorization,
            @RequestBody String book
    ) throws Exception {
        if(checkToken(authorization)){
            return authError();
        }
        Book bk = QueryBuilder.parseToObj(book,Book.class);
        if(bk.getIdentifier()==0){
            AddressBook.create(bk);
        } else
            AddressBook.update(bk);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        return ResponseEntity.ok().headers(headers).body(book);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity postBook(
            @RequestHeader("Authorization") String authorization,
            @RequestParam String find
    ) {
        if(checkToken(authorization)){
            return authError();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        return ResponseEntity.ok().headers(headers).body(AddressBook.find(find));
    }

    // этот метод будет принимать время методом DELETE
    // и на его основе можно удалит объект
    @RequestMapping(value= "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteBook(
            @RequestHeader("Authorization") String authorization,
            @PathVariable int id
    ) {
        if(!checkToken(authorization)){
            AddressBook.delete(id);
        }
    }
}
