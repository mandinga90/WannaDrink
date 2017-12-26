package com.wanna_drink.wannadrink.entities;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

public class MessageInformation {
    String authorId;
    String text;
    Object timestamp;

    public MessageInformation(){};

    public MessageInformation(String authorId, String text){
        this.authorId = authorId;
        this.text = text;
        this.timestamp = ServerValue.TIMESTAMP;    };

    public String getText() {
        return text;
    }

    public String getAuthorId() {
        return authorId;
    }

    // This prevents this getter from Json serialization
    @Exclude
    public Long getTimestampLong() {
        return (long)timestamp;
    }

    public Object getTimestamp() {
        return timestamp;
    }

}
