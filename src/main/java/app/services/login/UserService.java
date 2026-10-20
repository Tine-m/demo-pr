package app.services.login;

import app.persistence.ConnectionPool;
import app.persistence.login.UserMapper2;
import app.entities.login.User;
import app.exceptions.DatabaseException;


public class UserService {
    private UserMapper2 userMapper;
    private ConnectionPool connectionPool;

    public UserService(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        userMapper = new UserMapper2(connectionPool);
    }

    public User getUserByUserName(String userName) throws DatabaseException {
        return userMapper.getUserByUserName(userName);
    }

    public User login(String userName, String password) throws DatabaseException {
        return userMapper.login(userName, password);
    }

}