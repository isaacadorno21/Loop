package com.example.isaac.loop;


/**
 * Created by isaac on 4/9/2017.
 * 5/2/2017
 */

public class Message {

    private String message;
    private String author;
    private String time;
    public String uid;

    public Message() {}

    public Message(String message, String author, String uid) {
        this.message = message;
        this.author = author;
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() { return author; }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }
}
