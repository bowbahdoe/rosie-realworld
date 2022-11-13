package dev.mccue.realworld.context;

import org.sqlite.SQLiteDataSource;

public interface HasDB {
    SQLiteDataSource db();
}
