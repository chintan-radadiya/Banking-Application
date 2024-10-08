package org.BankingManagementApp;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public boolean account_exist(String email) {

        try {

            String acCheckQuery = "SELECT ACC_NO FROM accounts WHERE EMAIL = ?";
            PreparedStatement acCheckStatement = connection.prepareStatement(acCheckQuery);
            acCheckStatement.setString(1, email);
            ResultSet resultset = acCheckStatement.executeQuery();

            if (resultset.next()) {
                acCheckStatement.close();
                resultset.close();
                return true;
            } else {
                acCheckStatement.close();
                resultset.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public long createAccount(String email) {

        try {

            if (!account_exist(email)) {

                System.out.println();
                String OpenACquery = "INSERT INTO accounts (ACC_NO,FULL_NAME,EMAIL,BALANCE,SECURITY_PIN)" +
                        " VALUES (?,?,?,?,?);";

                System.out.print("Enter your NAME: ");
                String name = scanner.nextLine();

                System.out.print("EMAIL: ");
                String ACemail = scanner.nextLine();

                System.out.print("Set some Initial BALANCE: ");
                long balance = scanner.nextLong();

                System.out.print("Enter a 4 digit Security PIN : ");
                int Pin = scanner.nextInt();
                long account_number = generateACnumber();

                PreparedStatement openACstatement = connection.prepareStatement(OpenACquery);

                openACstatement.setLong(1, account_number);
                openACstatement.setString(2, name);
                openACstatement.setString(3, ACemail);
                openACstatement.setLong(4, balance);
                openACstatement.setInt(5, Pin);

                int row = openACstatement.executeUpdate();

                if (row > 0) {
                    openACstatement.close();
                    return account_number;
                } else {
                    openACstatement.close();
                    System.out.println("Account creation Failed!!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Account already Exist!!");
    }

    private long generateACnumber() {
        try {
            String query = "SELECT ACC_NO from accounts ORDER BY ACC_NO DESC LIMIT 1";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
            long last_ac = resultSet.getLong("ACC_NO");

            statement.close();
            resultSet.close();

                return last_ac + 1;
            } else {

                statement.close();
                resultSet.close();

                return 84440001;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 84440001;
    }

    public long getACnumber(String email){
        try{
            String getACquery = "SELECT ACC_NO from accounts WHERE EMAIL = ? ";

            PreparedStatement getACstatement = connection.prepareStatement(getACquery);
            getACstatement.setString(1,email);
            ResultSet resultSet = getACstatement.executeQuery();

            if (resultSet.next()){
            long ac = resultSet.getLong("ACC_NO");

                getACstatement.close();
                resultSet.close();

                return ac;
            }else{
                System.out.println("Account not found!!!");
                getACstatement.close();
                resultSet.close();
            }
        }catch (SQLException e){
            e.getMessage();
        }
        throw new RuntimeException("Account Not Found!!!");

    }
}
