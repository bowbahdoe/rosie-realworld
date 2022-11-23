package dev.mccue.realworld.service;

import dev.mccue.realworld.context.HasArticleService;
import dev.mccue.realworld.context.HasDB;
import org.sqlite.SQLiteDataSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TagService {
    private final SQLiteDataSource db;

    public <Ctx extends HasDB> TagService(Ctx ctx) {
        this.db = ctx.db();
    }

    public record Tag(
            long tagId,
            String name
    ) {}

    public List<Tag> all() {
        try (var conn = db.getConnection();
             var stmt = conn.prepareStatement("""
                     SELECT tag_id, name
                     FROM tag
                     """)) {
            var tags = new ArrayList<Tag>();
            var rs = stmt.executeQuery();
            while (rs.next()) {
                tags.add(new Tag(
                        rs.getLong(1),
                        rs.getString(2)
                ));
            }
            return Collections.unmodifiableList(tags);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
