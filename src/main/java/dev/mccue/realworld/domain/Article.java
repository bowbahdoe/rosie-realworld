package dev.mccue.realworld.domain;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.time.LocalDateTime;

@RecordBuilder
public record Article(
        long articleId,
        ExternalId externalId,
        String title,
        String description,
        String body,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        long userId
) {
    public ArticleSlug slug() {
        return new ArticleSlug(externalId, title);
    }
}
