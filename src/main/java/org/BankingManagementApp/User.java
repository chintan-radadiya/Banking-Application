package org.BankingManagementApp;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner scanner;

    public User(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register(Connection connection, Scanner scanner) {
        try {
            System.out.println();
            System.out.print("Email: ");
            String email = scanner.nextLine();

            if (user_exist(email)) {
                System.out.println();
                System.out.println("         User Already exist with this Email");
                System.out.println("                 Try another Email");
                return;
            }

            System.out.print("Name: ");
            String name = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();


            String queryRegister = "INSERT INTO user (EMAIL, FULL_NAME, PASSWORD) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(queryRegister);
            statement.setString(1, email);
            statement.setString(2, name);
            statement.setString(3, password);

            int row = statement.executeUpdate();
            if (row > 0) {
                System.out.println();
                System.out.println("User registered successfully!!");


            } else {
                System.out.println("Registration failed!!!");
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String login() throws SQLException {
        try {
            System.out.println();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            System.out.println();
            String loginQuery = "SELECT * FROM user WHERE EMAIL = ? AND PASSWORD = ?";

            PreparedStatement loginStatement = connection.prepareStatement(loginQuery);
            loginStatement.setString(1, email);
            loginStatement.setString(2, password);
            ResultSet resultSet = loginStatement.executeQuery();

            if (resultSet.next()) {
                loginStatement.close();
                resultSet.close();
                return email;
            } else {
                loginStatement.close();
                resultSet.close();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean user_exist(String email) {
        try {
            String queryCheck = "SELECT * FROM USER WHERE EMAIL = ?";

            PreparedStatement statement = connection.prepareStatement(queryCheck);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                statement.close();
                resultSet.close();
                return true;

            } else {
                statement.close();
                resultSet.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
