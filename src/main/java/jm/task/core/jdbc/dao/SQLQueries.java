package jm.task.core.jdbc.dao;

//класс с запросами
public class SQLQueries {
    public static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS users (" +
            "id SERIAL PRIMARY KEY, " +
            "name VARCHAR(50), " +
            "last_name VARCHAR(50), " +
            "age SMALLINT)";
    public static final String DROP_USERS_TABLE = "DROP TABLE IF EXISTS users";
    public static final String INSERT_USER = "INSERT INTO users (name, last_name, age) VALUES (?, ?, ?)";
    public static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?";
    public static final String SELECT_ALL_USERS = "SELECT * FROM users";
    public static final String TRUNCATE_USERS_TABLE = "TRUNCATE TABLE users";
}
