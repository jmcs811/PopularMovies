package com.jcaseydev.popularmovies.model;

/**
 * Created by justi on 5/17/2016.
 */
public class Reviews {
    private String author;
    private String Content;

    public Reviews(String author, String content){
        this.author = author;
        this.Content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return Content;
    }
}
