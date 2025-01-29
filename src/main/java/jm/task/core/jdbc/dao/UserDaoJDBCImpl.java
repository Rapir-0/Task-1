package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import lombok.extern.slf4j.Slf4j;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {
    }

    private final Connection connection = Util.getConnection();
    SQLQueries sqlQueries = new SQLQueries();

    @Override
    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlQueries.CREATE_USERS_TABLE);
            log.info("Таблица создана");
        } catch (Exception e) {
            log.error("Ошибка создания таблицы", e);
        }
    }

    @Override
    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlQueries.DROP_USERS_TABLE);
            log.info("Таблица удалена");
        } catch (Exception e) {
            log.error("Ошибка удаления таблицы", e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQueries.INSERT_USER)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            log.info("User с именем {} добавлен", name);
        } catch (Exception e) {
            log.error("Ошибка добавления пользователя с именем {}", name, e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQueries.DELETE_USER_BY_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            log.info("User с ID {} удалён", id);
        } catch (Exception e) {
            log.error("Ошибка удаления пользователя с ID {}", id, e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQueries.SELECT_ALL_USERS)) {
            while (resultSet.next()) {
                User user = new User(resultSet.getString("name"),
                        resultSet.getString("last_name"),
                        resultSet.getByte("age"));
                user.setId(resultSet.getLong("id"));
                users.add(user);
            }
            log.info("Получен список всех пользователей: {}", users);
        } catch (Exception e) {
            log.error("Ошибка получения списка пользователей", e);
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlQueries.TRUNCATE_USERS_TABLE);
            log.info("Таблица очищена");
        } catch (Exception e) {
            log.error("Ошибка очистки таблицы", e);
        }
    }
}
