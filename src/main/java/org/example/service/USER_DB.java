package org.example.service;

import org.example.model.USER;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class USER_DB {
//    private String url = "jdbc:postgresql://localhost:5432/MYOTemp";
//    private String user_db = "postgres";
//    private String password = "nurskanyb";

    private String url = "jdbc:mysql://localhost:3306/myotemp";
    private String user_db = "root";
    private String password = "";

    public USER_DB() {
        createTable();
//        dropTable();
    }

    private void dropTable() {
        try (Connection connection = DriverManager.getConnection(url, user_db, password)) {
            String dropTableSQL = "DROP TABLE IF EXISTS users CASCADE";

            try (Statement statement = connection.createStatement()) {
                statement.execute(dropTableSQL);
                System.out.println("Table dropped successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        try (Connection connection = DriverManager.getConnection(url, user_db, password)) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, name VARCHAR(255), surname VARCHAR(255), gpa VARCHAR(255), group_manas VARCHAR(255))";
            try (Statement statement = connection.createStatement()) {
                statement.execute(createTableSQL);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public USER saveUser(USER user) {
//        mysql
        if (!userExists(user)) {
            try (Connection connection = DriverManager.getConnection(url, user_db, password)) {
                String insertSQL = "INSERT INTO users (name, surname, gpa, group_manas) VALUES (?, ?, ?, ?)";

                try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setString(1, user.getName());
                    preparedStatement.setString(2, user.getSurname());
                    preparedStatement.setString(3, user.getGpa());
                    preparedStatement.setString(4, user.getGroup());

                    int affectedRows = preparedStatement.executeUpdate();

                    if (affectedRows > 0) {
                        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                user.setId(generatedKeys.getLong(1));
                            } else {
                                throw new SQLException("Failed to get ID for the new user.");
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
//        posgre
//        if (!userExists(user)) {
//            try (Connection connection = DriverManager.getConnection(url, user_db, password)) {
//                String insertSQL = "INSERT INTO users (name, surname, gpa, group_manas) VALUES (?, ?, ?, ?) RETURNING id";
//
//                try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
//                    preparedStatement.setString(1, user.getName());
//                    preparedStatement.setString(2, user.getSurname());
//                    preparedStatement.setString(3, user.getGpa());
//                    preparedStatement.setString(4, user.getGroup());
//
//                    ResultSet resultSet = preparedStatement.executeQuery();
//                    if (resultSet.next()) {
//                        user.setId(resultSet.getLong("id"));
//                    }
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return user;
    }

    public USER updateUser(USER user) {
        try (Connection connection = DriverManager.getConnection(url, user_db, password)) {
            String updateSQL = "UPDATE users SET name = ?, surname = ?, gpa = ?, group_manas = ? WHERE id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getSurname());
                preparedStatement.setString(3, user.getGpa());
                preparedStatement.setString(4, user.getGroup());
                preparedStatement.setLong(5, user.getId());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public void deleteUser(Long userId) {
        try (Connection connection = DriverManager.getConnection(url, user_db, password)) {
            String deleteSQL = "DELETE FROM users WHERE id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
                preparedStatement.setLong(1, userId);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public USER getUser(Long userId) {
        USER user = null;

        try (Connection connection = DriverManager.getConnection(url, user_db, password)) {
            String selectSQL = "SELECT * FROM users WHERE id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
                preparedStatement.setLong(1, userId);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    user = new USER();
                    user.setId(resultSet.getLong("id"));
                    user.setName(resultSet.getString("name"));
                    user.setSurname(resultSet.getString("surname"));
                    user.setGpa(resultSet.getString("gpa"));
                    user.setGroup(resultSet.getString("group"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public List<USER> getAllUsers() {
        List<USER> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, user_db, password)) {
            String selectSQL = "SELECT * FROM users";

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectSQL)) {
                while (resultSet.next()) {
                    USER user = new USER();
                    user.setId(resultSet.getLong("id"));
                    user.setName(resultSet.getString("name"));
                    user.setSurname(resultSet.getString("surname"));
                    user.setGpa(resultSet.getString("gpa"));
                    user.setGroup(resultSet.getString("group_manas"));


                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    private boolean userExists(USER user) {
        try (Connection connection = DriverManager.getConnection(url, user_db, password)) {
            String selectSQL = "SELECT COUNT(*) FROM users WHERE name = ? AND surname = ? AND gpa = ? AND group_manas = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getSurname());
                preparedStatement.setString(3, user.getGpa());
                preparedStatement.setString(4, user.getGroup());

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
