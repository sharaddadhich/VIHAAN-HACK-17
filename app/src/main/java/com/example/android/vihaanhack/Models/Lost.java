package com.example.android.vihaanhack.Models;

/**
 * Created by ishaandhamija on 28/10/17.
 */

public class Lost {

    String lostName;
    Integer lostAge;
    String lostClothes;
    String lostImage;

    public Lost() {}

    public Lost(String lostName, Integer lostAge, String lostClothes, String lostImage) {
        this.lostName = lostName;
        this.lostAge = lostAge;
        this.lostClothes = lostClothes;
        this.lostImage = lostImage;
    }

    public String getLostName() {
        return lostName;
    }

    public Integer getLostAge() {
        return lostAge;
    }

    public String getLostClothes() {
        return lostClothes;
    }

    public String getLostImage() {
        return lostImage;
    }
}