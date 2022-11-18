package dev.mccue.realworld.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SettableParameter {
    void setParameter(PreparedStatement statement, int i) throws SQLException;
}
