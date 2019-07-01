package com.example.hussnain.girovado;

import android.graphics.Bitmap;

public class FoundLeisure {
    private int leisureId;
    private String text;
    private String url;

    public FoundLeisure(int leisureId, String text, String url) {
        this.leisureId = leisureId;
        this.text = text;
        this.url = url;
    }

    public int getLeisureId() {
        return leisureId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
