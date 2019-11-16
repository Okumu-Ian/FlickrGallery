package com.company.flickrgallery.models;

import java.io.Serializable;

public class Photo implements Serializable {
    String farm, id, server, secret;
    String photo_title;
    String image_url;

    public Photo() {
    }

    public String getPhoto_title() {
        return photo_title;
    }

    public void setPhoto_title(String photo_title) {
        this.photo_title = photo_title;
    }

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url() {
        this.image_url = "https://farm" +
                this.farm + ".staticflickr.com/" +
                this.server + "/" + this.id + "_" + this.secret + ".jpg";
    }
}
