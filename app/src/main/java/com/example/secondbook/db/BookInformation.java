package com.example.secondbook.db;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class BookInformation extends LitePalSupport implements Serializable {
    int id;
    String account;
    String accountName;
    String accountImagePath="null";
    String bookName;
    String bookAuthor;
    String bookPrice;
    String bookpublishing;
    String bookSellIntroduction;
    String currentTime;
    String Imagepaths;
    String isCollection;
    String CollectiorList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountImagePath() {
        return accountImagePath;
    }

    public void setAccountImagePath(String accountImagePath) {
        this.accountImagePath = accountImagePath;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookpublishing() {
        return bookpublishing;
    }

    public void setBookpublishing(String bookpublishing) {
        this.bookpublishing = bookpublishing;
    }

    public String getBookSellIntroduction() {
        return bookSellIntroduction;
    }

    public void setBookSellIntroduction(String bookSellIntroduction) {
        this.bookSellIntroduction = bookSellIntroduction;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(String isCollection) {
        this.isCollection = isCollection;
    }

    public String getImagepaths() {
        return Imagepaths;
    }

    public void setImagepaths(String imagepaths) {
        Imagepaths = imagepaths;
    }

    public String getCollectiorList() {
        return CollectiorList;
    }

    public void setCollectiorList(String collectiorList) {
        CollectiorList = collectiorList;
    }
}
