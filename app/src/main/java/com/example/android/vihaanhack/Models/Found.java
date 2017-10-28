package com.example.android.vihaanhack.Models;

import android.net.Uri;

/**
 * Created by HP on 28-Oct-17.
 */

public class Found {
    Uri photoUri;
    Uri audioUri;
    String clothes;
    String description;

    public Found(Uri photoUri, Uri audioUri, String clothes, String description) {
        this.photoUri = photoUri;
        this.audioUri = audioUri;
        this.clothes = clothes;
        this.description = description;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    public Uri getAudioUri() {
        return audioUri;
    }

    public void setAudioUri(Uri audioUri) {
        this.audioUri = audioUri;
    }

    public String getClothes() {
        return clothes;
    }

    public void setClothes(String clothes) {
        this.clothes = clothes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
