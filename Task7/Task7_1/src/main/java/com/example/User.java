package com.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty
    private String name;
    @JsonProperty
    private int favoriteNumber;
    @JsonProperty
    private String favoriteColor;

    @JsonProperty
    private String hobby;

    public User() {}

    public User(String name, int favoriteNumber, String favoriteColor, String hobby) {
        this.name = name;
        this.favoriteNumber = favoriteNumber;
        this.favoriteColor = favoriteColor;
        this.hobby = hobby;
    }

    public String getName() { return name; }
    public int getFavoriteNumber() { return favoriteNumber; }
    public String getFavoriteColor() { return favoriteColor; }
    public String getHobby() { return hobby; }

    @Override
    public String toString() {
        return String.format(
                "User[name=%s, age=%d, favoriteColor=%s, hobby=%s]",
                name, favoriteNumber, favoriteColor, hobby);
    }
}