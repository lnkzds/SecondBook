package com.example.secondbook.db;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Comment extends LitePalSupport implements Serializable {

    int id;
    String accountimage;
    String accountname;
    String time;
    String text;
    String account;
    int bookId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountimage() {
        return accountimage;
    }

    public void setAccountimage(String accountimage) {
        this.accountimage = accountimage;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}
