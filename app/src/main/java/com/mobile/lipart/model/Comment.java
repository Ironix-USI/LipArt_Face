package com.mobile.lipart.model;

import com.google.firebase.database.IgnoreExtraProperties;

// [START comment_class]
@IgnoreExtraProperties
public class Comment {

    public String uid;
    public String author;
    public String text;
    public String color;

    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Comment(String uid, String author, String text, String color) {
        this.uid = uid;
        this.author = author;
        this.text = text;
        this.color = color;
    }

}
// [END comment_class]