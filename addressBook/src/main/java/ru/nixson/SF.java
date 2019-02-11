package ru.nixson;


import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class SF {
    //private static SF ourInstance;// = new SF();
    private static SessionFactory sf;

    static {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception x){
            System.err.println("No def mysql in classpath!");
        }


        Configuration cfg = new Configuration()
                .addAnnotatedClass(Book.class);

        ///.addAnnotatedClass...

        cfg.setProperty("hibernate.connection.url", "jdbc:mysql://80.92.31.23:3306/phonebook");
        cfg.setProperty("hibernate.connection.username", "phonebook");
        cfg.setProperty("hibernate.connection.password", "fUXUyPPI1S8kXWIr");

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(cfg.getProperties());

        sf = cfg.buildSessionFactory(builder.build());
    }

    public static SessionFactory getInstance() {
        return sf;
    }

    private SF() {}
}
