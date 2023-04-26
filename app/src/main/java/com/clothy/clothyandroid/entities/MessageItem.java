package com.clothy.clothyandroid.entities;

public class MessageItem {

    private String id;
    private String idR;
    private String name;
    private String content;
    private int count;
    private String picture;


    public MessageItem() {
    }

    public MessageItem(String id, String idR, String name, String content, int count, String picture) {
        this.id = id;
        this.idR = idR;
        this.name = name;
        this.content = content;
        this.count = count;
        this.picture = picture;
    }

    public String getId() {
        return id;
    }
    public String getIdR() {
        return idR;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public int getCount() {
        return count;
    }

    public String getPicture() {
        return picture;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setIdR(String idR) {
        this.idR = idR;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPicture(String id) {
        this.id = id;
    }
}

//myImageView.setImageResource(R.drawable.icon);