package io.github.zufarm.library.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean testConnection() {
        try {
        	jdbcTemplate.execute("SELECT 1");
        	System.out.println("Connect!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}