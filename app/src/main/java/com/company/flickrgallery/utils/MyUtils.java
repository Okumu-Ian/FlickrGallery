package com.company.flickrgallery.utils;

import com.company.flickrgallery.models.Photo;

import java.util.List;

public class MyUtils {
    public static String flickrURL = "https://api.flickr.com/services/rest/?method=flickr.galleries.getPhotos&api_key=7bfdc8394de1a8d60ad4d89a2aae9d35&gallery_id=66911286-72157647277042064&format=json&nojsoncallback=1";
    public static String photos = "PHOTOS";
    public static String photosList = "PHOTOS_LIST";
    public static String photoID = "PHOTO_ID";

    public List<Photo> photoList;

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }
}
