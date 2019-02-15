package ru.nixson;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class SF {
    //private static SF ourInstance;// = new SF();
    private static SessionFactory sf;

    static {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception x){
            System.err.println("No def mysql in classpath!");
        }

        Configuration cfg = new Configuration()
                .addAnnotatedClass(Book.class)
                .addAnnotatedClass(Token.class);

        cfg.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5439/platform");
        cfg.setProperty("hibernate.connection.username", "postgres");
        cfg.setProperty("hibernate.connection.password", "123456");
        cfg.setProperty("hibernate.hbm2ddl.auto","update");

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(cfg.getProperties());

        sf = cfg.buildSessionFactory(builder.build());
    }

    public static SessionFactory getInstance() {
        return sf;
    }

    private SF() {}
}
