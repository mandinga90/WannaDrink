package com.wanna_drink.wannadrink.firebase;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by Maxim on 12/10/2017.
 */

@IgnoreExtraProperties
public class Chat {
    public String user1Id;
    public String user2Id;
    public String user1Name;
    public String user2Name;
    public Date startTime;
    public Date endTime;
    public String lastMessage;
    public Date lastMessageTime;

    public Chat(){};

    public Chat(String user1Id, String user2Id, String user1Name, String user2Name, Date startTime, Date endTime, String lastMessage, Date lastMessageTime) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.user1Name = user1Name;
        this.user2Name = user2Name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }
}
