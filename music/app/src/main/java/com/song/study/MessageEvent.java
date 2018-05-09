package com.song.study;

public class MessageEvent {

    private String message;

    private Object object;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return this.object;
    }

    public void setData(Object object) {
        this.object = object;
    }
}