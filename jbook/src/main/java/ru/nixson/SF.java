package ru.nixson;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class SF {
    //private static SF ourInstance;// = new SF();
    private static SessionFactory sf;

    static {
        Config conf = Config.getConfig("connection-config");


        try {
            Class.forName(conf.getParam(Config.AK_DB_FORNAME));
        } catch (Exception x){
            System.err.println("No def "+conf.getParam(Config.AK_DB_FORNAME)+" in classpath!");
        }

        Configuration cfg = new Configuration()
                .addAnnotatedClass(Book.class)
                .addAnnotatedClass(Token.class);

        cfg.setProperty("hibernate.connection.url", conf.getParam(Config.AK_DB_URL));
        cfg.setProperty("hibernate.connection.username", conf.getParam(Config.AK_DB_USER));
        cfg.setProperty("hibernate.connection.password", conf.getParam(Config.AK_DB_PASSWORD));
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
