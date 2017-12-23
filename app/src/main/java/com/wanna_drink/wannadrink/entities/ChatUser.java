package com.wanna_drink.wannadrink.entities;

import android.graphics.Bitmap;
import android.graphics.drawable.Icon;

import com.github.bassaer.chatmessageview.model.IChatUser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChatUser implements IChatUser {
    private String id;
    private String name;
    private Bitmap icon;

    public ChatUser(String id, String name, Bitmap icon) {
        this.name = name;
        this.id = id;
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Nullable
    @Override
    public Bitmap getIcon() {
        return icon;
    }

    @NotNull
    @Override
    public String getId() {
        return id;
    }

    @Nullable
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setIcon(Bitmap bitmap) {
        icon = bitmap;
    }
}
