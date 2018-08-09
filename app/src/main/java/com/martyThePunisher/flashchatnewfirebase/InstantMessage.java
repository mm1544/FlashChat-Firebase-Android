package com.martyThePunisher.flashchatnewfirebase;

// MODEL CLASS

public class InstantMessage {

    private String message;
    private String author;

    public InstantMessage(String message, String author) {
        this.message = message;
        this.author = author;
    }

    public InstantMessage() {
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        if(author == null) author = "Name not saved";
        return author;
    }
}

