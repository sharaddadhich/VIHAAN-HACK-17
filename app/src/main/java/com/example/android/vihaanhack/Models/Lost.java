package com.example.android.vihaanhack.Models;

/**
 * Created by ishaandhamija on 28/10/17.
 */

public class Lost {

    String lostName;
    String lostAge;
    String lostClothes;
    String lostImage;

    public Lost() {}

    public Lost(String lostName, String lostAge, String lostClothes, String lostImage) {
        this.lostName = lostName;
        this.lostAge = lostAge;
        this.lostClothes = lostClothes;
        this.lostImage = lostImage;
    }

    public String getLostName() {
        return lostName;
    }

    public void setLostName(String lostName) {
        this.lostName = lostName;
    }

    public String getLostAge() {
        return lostAge;
    }

    public void setLostAge(String lostAge) {
        this.lostAge = lostAge;
    }

    public void setLostClothes(String lostClothes) {
        this.lostClothes = lostClothes;
    }

    public void setLostImage(String lostImage) {
        this.lostImage = lostImage;
    }

    public String getLostClothes() {
        return lostClothes;
    }

    public String getLostImage() {
        return lostImage;
    }
}