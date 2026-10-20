package app.persistence.login;
/*
import dto.UserAndAuthorsDTO;
import entities.Author;
import entities.User;
import exceptions.DatabaseException;*/

import app.entities.login.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserMapper2 {

    ConnectionPool connectionPool;
    private static final Logger logger =
            LoggerFactory.getLogger(UserMapper.class);

    public UserMapper2(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public User login(String userName, String password) throws DatabaseException {

        User user = getUserByUserName(userName);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        } else return null;
    }


  /*  public User createUser(User user) throws DatabaseException {
        String query = "INSERT INTO laaner (username, fornavn, efternavn, adresse, postnr, password) " +
                " VALUES ?, ?, ?, ?, ?, ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, user.getUserName());
            stm.setString(2, user.getFirstName());
            stm.setString(3, user.getLastName());
            stm.setString(4, user.getAddress());
            stm.setInt(5, user.getZip());
            stm.setString(6, user.getPassword());
            stm.executeUpdate();
            try (ResultSet rs = stm.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt("laaner_id"));
                } else throw new DatabaseException("Brugeren kunne ikke oprettes");
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DatabaseException("Brugeren blev ikke gemt ");
        }
        return user;
    }*/

    public User getUserByUserName(String userName) throws DatabaseException {
        User user = null;
        String query = "select * from users where username=? and password=?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {
            stm.setString(1, userName);
            try (ResultSet rs = stm.executeQuery();) {
                if (rs.next()) {
                    int id = rs.getInt("user_id");
                    String role = rs.getString("role");
                    String password = rs.getString("password");
                    user = new User(id, userName, password, role);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DatabaseException("Søgning efter brugeren fejlede");
        }
        return user;
    }
}