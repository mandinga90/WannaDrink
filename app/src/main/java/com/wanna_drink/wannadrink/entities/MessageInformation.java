package com.wanna_drink.wannadrink.entities;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maxim on 12/13/2017.
 */

public class MessageInformation {
    String authorUId;
    String text;
    Object timestamp;

    public MessageInformation(){};

    public MessageInformation(String authorUId, String text){
        this.authorUId = authorUId;
        this.text = text;
        this.timestamp = ServerValue.TIMESTAMP;    };

    public String getText() {
        return text;
    }

    public String getAuthorUId() {
        return authorUId;
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
