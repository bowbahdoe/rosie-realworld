package dev.mccue.realworld.domain;

public record FavoritedInfo(
        boolean favorited,
        int favoriteCount
) {}