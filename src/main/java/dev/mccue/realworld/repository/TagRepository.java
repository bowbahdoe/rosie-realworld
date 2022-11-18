package dev.mccue.realworld.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class TagRepository {
    record Tag(
            long tagId,
            String name
    ) {}

    private static final String SELECT_FIELDS = String.join(", ", List.of(
       "tag.tag_id",
       "tag.name"
    ));

    private static Tag tagFromRow(ResultSet rs) throws SQLException {
        return new Tag(
                rs.getLong(1),
                rs.getString(2)
        );
    }

    public List<Tag> all(Connection conn) {
        try (var stmt = conn.prepareStatement(
                // language=SQL
                """
                SELECT %s
                FROM tag
                """.formatted(SELECT_FIELDS))) {
            var tags = new ArrayList<Tag>();
            var rs = stmt.executeQuery();
            while (rs.next()) {
                tags.add(tagFromRow(rs));
            }
            return List.copyOf(tags);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
