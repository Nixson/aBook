package ru.nixson;

import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class AddressBook {
    public void create(Book book){
        Session sess = SF.getInstance().openSession();
        sess.save(book);
        sess.close();
    }

    public void update(Book book) {
        Session sess = SF.getInstance().openSession();
        sess.update(book);
        sess.close();
    }

    public void delete(int id){
        Session sess = SF.getInstance().openSession();
        Book book  = sess.byId(Book.class).load(id);
        sess.delete(book);
        sess.close();
    }
    public Book byId(int id){
        Session sess = SF.getInstance().openSession();
        Book book  = sess.byId(Book.class).load(id);
        sess.close();
        return book;
    }
    public boolean auth(String login, String pass) throws NoSuchAlgorithmException {
        Session sess = SF.getInstance().openSession();
        Query q = sess.createQuery("FROM book where login = :login and password = :password");
        q.setParameter("login",login);
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(pass.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        q.setParameter("password",myHash);
        int cnt = q.getFetchSize();
        if(cnt > 0){
            return true;
        }
        return false;
    }
    public List<Book> find(String attr){
        Session sess = SF.getInstance().openSession();
        Query query = sess.createQuery("from book where surname like '%:attr%' or" +
                " firstname like '%:attr%' or " +
                " middlename like '%:attr%' or " +
                " email like '%:attr%' or " +
                " phone like '%:attr%'");
        query.setParameter("attr",attr);
        List<Book> bl = query.getResultList();
        sess.close();
        return bl;
    }
}
