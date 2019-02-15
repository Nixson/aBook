package ru.nixson;


import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;

@Entity
@Table(name = "token")
public class Token {

    @Id
    @Generated(value = GenerationTime.INSERT)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",updatable = false, nullable = false)
    private int identifier;

    @Column(name = "bid")
    private int bookId;

    @Column(name = "dtime")
    private int dtime;

    @Column(name = "token")
    private String token;

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getDtime() {
        return dtime;
    }

    public void setDtime(int dtime) {
        this.dtime = dtime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
