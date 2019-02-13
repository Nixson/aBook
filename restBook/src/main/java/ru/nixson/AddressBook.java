package ru.nixson;

import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AddressBook {
    public static void create(Book book){
        try {
            Session sess = SF.getInstance().openSession();
            sess.save(book);
            sess.close();
        } catch (ExceptionInInitializerError err){
        }
    }

    public static void update(Book book) {
        try {
        Session sess = SF.getInstance().openSession();
        sess.update(book);
        sess.close();
        } catch (ExceptionInInitializerError err){
        }
    }

    public static void delete(int id){
        try {
        Session sess = SF.getInstance().openSession();
        Book book  = sess.byId(Book.class).load(id);
        sess.delete(book);
        sess.close();
        } catch (ExceptionInInitializerError err){
        }
    }
    public static Book byId(int id){
        try {
            Session sess = SF.getInstance().openSession();
            Book book  = sess.byId(Book.class).load(id);
            sess.close();
            return book;
        } catch (ExceptionInInitializerError err){
            return null;
        }
    }
    public static Book auth(String login, String pass) throws NoSuchAlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pass.getBytes());
            byte[] digest = md.digest();
            String myHash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
            System.out.println("Hash: "+myHash);
            Session sess = SF.getInstance().openSession();
            List q = sess.createQuery("FROM Book where login = :login and password = :password").
                    setParameter("login",login).
                    setParameter("password",myHash).list();
            int cnt = q.size();
            System.out.println("cnt: "+cnt);
            if(cnt > 0){
                return (Book)q.get(0);
            }
            return null;
        } catch (ExceptionInInitializerError err){
            return null;
        }
    }
    public static List<Book> find(String attr){
        try {
            Session sess = SF.getInstance().openSession();
            Query query = sess.createQuery("from Book where surname like '%:attr%' or" +
                    " firstname like '%:attr%' or " +
                    " middlename like '%:attr%' or " +
                    " email like '%:attr%' or " +
                    " phone like '%:attr%'");
            query.setParameter("attr",attr);
            List<Book> bl = query.getResultList();
            sess.close();
            return bl;
        } catch (ExceptionInInitializerError err){
            return null;
        }
    }
    public static boolean checkToken(String token){
        if(token == null || token.trim().length() == 0){
            return false;
        }
        try {
            Date now = new Date();
            Long longTime = new Long(now.getTime()/1000);
            Session sess = SF.getInstance().openSession();
            Query query = sess.createQuery("from token where token = :token and dtime >= :dtime");
            query.setParameter("token",token);
            query.setParameter("dtime",longTime.intValue());
            return query.getFetchSize() > 0;
        } catch (ExceptionInInitializerError err){
            return false;
        }
    }
    public static String getToken(Book ab){
        Token nt = new Token();
        nt.setBookId(ab.getIdentifier());
        Date now = new Date();
        Long longTime = new Long(now.getTime()/1000);
        nt.setDtime(longTime.intValue()+3600);
        nt.setToken(UUID.randomUUID().toString());
        try {
            Session sess = SF.getInstance().openSession();
            sess.save(nt);
            sess.close();
            return nt.getToken();
        } catch (ExceptionInInitializerError err){
            return "";
        }
    }
}
