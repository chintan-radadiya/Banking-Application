package org.BankingManagementApp;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner scanner;

    public AccountManager(Connection connection, Scanner scanner) throws SQLException {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void debitMoney(long ac) throws SQLException {
        System.out.print("Enter amount to debit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter Security Pin: ");
        int pin = scanner.nextInt();
        scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if (ac != 0) {
                String fetchQuery = "Select * From accounts Where ACC_NO = ? AND SECURITY_PIN = ?";

                PreparedStatement fetchData = connection.prepareStatement(fetchQuery);
                fetchData.setLong(1, ac);
                fetchData.setInt(2, pin);
                ResultSet resultSet = fetchData.executeQuery();

                if (resultSet.next()) {
                    double Curr_balance = resultSet.getDouble("BALANCE");
                    if (amount <= Curr_balance) {
                        String debitQuery = "UPDATE accounts SET BALANCE = BALANCE - ? WHERE ACC_NO = ?";
                        PreparedStatement debitStatement = connection.prepareStatement(debitQuery);
                        debitStatement.setDouble(1, amount);
                        debitStatement.setLong(2, ac);
                        int row = debitStatement.executeUpdate();
                        if (row > 0) {
                            System.out.println("Amount of " + amount + "Debited successfully!!!");
                            connection.commit();
                            connection.setAutoCommit(true);
                        } else {
                            System.out.println("Transaction failed!!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient balance.");
                    }
                }
            } else {
                System.out.println("Wrong credentials!!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void creditMoney(long acc_no) throws SQLException {

        System.out.println();
        System.out.print("Enter amount to add: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter Security Pin: ");
        int pin = scanner.nextInt();

        try {
            connection.setAutoCommit(false);
            if (acc_no != 0) {
                String fetchBalanceQuery = "SELECT BALANCE FROM accounts WHERE ACC_NO = ? AND SECURITY_PIN = ? ;";
                PreparedStatement fetchBalStatement = connection.prepareStatement(fetchBalanceQuery);
                fetchBalStatement.setDouble(1, acc_no);
                fetchBalStatement.setDouble(2, pin);
                ResultSet resultSet = fetchBalStatement.executeQuery();

                if (resultSet.next()) {
                    String creditQuery = "UPDATE accounts SET BALANCE = BALANCE + ? WHERE ACC_NO = ?;";

                    PreparedStatement creditStat = connection.prepareStatement(creditQuery);
                    creditStat.setDouble(1, amount);
                    creditStat.setLong(2, acc_no);
                    int rows = creditStat.executeUpdate();

                    if (rows > 0) {
                        System.out.println("Account has been credited with Amount: " + amount);
                        connection.commit();
                        connection.setAutoCommit(true);
                    } else {
                        System.out.println("Transaction Failed!!!");
                        connection.commit();
                        connection.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Data not available!!!");
                }
            } else {
                System.out.println("Wrong credentials!!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void transferMoney(long acc_no){

        System.out.println();
        System.out.println("Enter Beneficiery account No.: ");
        long beneficiery = scanner.nextLong();

        System.out.println("Enter amount: ");
        double amount = scanner.nextDouble();

        System.out.println("Enter Security Pin: ");
        int pin = scanner.nextInt();

        try{
            connection.setAutoCommit(false);
        PreparedStatement statement = connection.prepareStatement("Select ACC_NO FROM accounts WHERE ACC_NO = ?");
        statement.setLong(1,beneficiery);

        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){

            String debitQ = "UPDATE accounts SET BALANCE = BALANCE - ? WHERE ACC_NO = ?";
            String creditQ ="UPDATE accounts SET BALANCE = BALANCE + ? WHERE ACC_NO = ?";

            PreparedStatement debitStatement = connection.prepareStatement(debitQ);
            debitStatement.setDouble(1,amount);
            debitStatement.setLong(2,acc_no);

            PreparedStatement creditStatement = connection.prepareStatement(creditQ);
            creditStatement.setDouble(1,amount);
            creditStatement.setLong(2,beneficiery);

            int rowDebit = debitStatement.executeUpdate();
            int rowCredit = creditStatement.executeUpdate();

            if(rowDebit > 0 && rowCredit>0){
                connection.commit();
                System.out.println("Money Transferred!!!");
                connection.setAutoCommit(true);
            }else{
                connection.rollback();
                System.out.println("Money transfer Failed!!");
                connection.setAutoCommit(true);
            }
        }else{
            System.out.println("Invalid Account!!");
        }
    }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void checkBalance(long acc_no) throws SQLException, RuntimeException {
        try {
            System.out.println();
            System.out.println("Enter security pin:");
            int pin = scanner.nextInt();
            scanner.nextLine();

            String fetchBalanceQuery = "SELECT BALANCE FROM accounts WHERE ACC_NO = ? AND SECURITY_PIN = ? ;";
            PreparedStatement fetchBalStatement = connection.prepareStatement(fetchBalanceQuery);
            fetchBalStatement.setDouble(1, acc_no);
            fetchBalStatement.setInt(2, pin);
            ResultSet resultSet = fetchBalStatement.executeQuery();

            if (resultSet.next()) {
                double balance = resultSet.getDouble("BALANCE");
                System.out.println("Balance is : " + balance);
            } else {
                System.out.println("DATA not AVailbale!!!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
