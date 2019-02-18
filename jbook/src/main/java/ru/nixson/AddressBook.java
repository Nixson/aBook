package ru.nixson;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AddressBook {
    private static void txCommit(Object el, String action){
        try {
            Session sess = SF.getInstance().openSession();
            Transaction tx = sess.beginTransaction();
            if(action.equals("save"))
                sess.save(el);
            else if(action.equals("update"))
                sess.saveOrUpdate(el);
            else if(action.equals("delete"))
                sess.delete(el);
            sess.flush();
            tx.commit();
            sess.close();
        } catch (ExceptionInInitializerError err){
        }
    }
    public static void create(Book book){
        txCommit(book,"save");
    }

    public static void update(Book book) {
        txCommit(book,"update");
    }

    public static void delete(int id){
        try {
            Session sess = SF.getInstance().openSession();
            Book book  = sess.byId(Book.class).load(id);
            sess.delete(book);
            sess.flush();
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
            List bl;
            if(attr==null) {
                bl = sess.createQuery("form Book").list();
            } else {
                attr = "%"+attr+"%";
                bl = sess.createQuery("from Book where surname like :attr1 or" +
                        " firstname like :attr2 or " +
                        " middlename like :attr3 or " +
                        " email like :attr4 or " +
                        " phone like :attr5")
                        .setParameter("attr1",attr)
                        .setParameter("attr2",attr)
                        .setParameter("attr3",attr)
                        .setParameter("attr4",attr)
                        .setParameter("attr5",attr)
                        .list();
            }
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
            List query = sess.createQuery("from Token where token = :token and dtime >= :dtime").
                    setParameter("token",token).
                    setParameter("dtime",longTime.intValue()).list();
            return query.size() > 0;
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
            Transaction tx = sess.beginTransaction();
            sess.save(nt);
            sess.flush();
            tx.commit();
            sess.close();
            return nt.getToken();
        } catch (ExceptionInInitializerError err){
            return "";
        }
    }
}
