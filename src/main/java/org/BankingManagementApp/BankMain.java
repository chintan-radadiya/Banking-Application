package org.BankingManagementApp;

import java.sql.DriverManager;
import java.util.Scanner;
import java.sql.SQLException;
import java.sql.Connection;


public class BankMain {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/bank_DB";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Chintan@2003";


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Drivers loaded successfully!!!");

        try {
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            System.out.println("Connection with DB Successfull!!!");

            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);


            System.out.println();
            System.out.println("**********  WELCOME TO BANK OF JAVA  **********");
            System.out.println();

            System.out.println("Enter your choice:");
            System.out.println("1.) Register");
            System.out.println("2.) Login");
            System.out.println("3.) Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            long account_number;

            switch (choice) {

                case 1:
                    user.register(connection, scanner);
                    break;

                case 2:
                    String email = user.login();
                    if (email != null) {
                        System.out.println("User LoggedIn successfully!!!");

                        if (!accounts.account_exist(email)) {
                            System.out.println();
                            System.out.println("1.) Create New Account");
                            System.out.println("2.) Exit");
                            int choice2 = scanner.nextInt();
                            scanner.nextLine();

                            if (choice2 == 1) {
                                long acc_number = accounts.createAccount(email);
                                System.out.println("Your Account created successfullyyy!!!!");
                                System.out.println("Account number is: " + acc_number);
                            } else {
                                break;
                            }
                        }
                     account_number = accounts.getACnumber(email);
                    int choice3 = 0;

                    while(choice3!=5){
                        System.out.println();
                        System.out.println("1. Debit Money");
                        System.out.println("2. Credit Money");
                        System.out.println("3. Transfer Money");
                        System.out.println("4. Check Balance");
                        System.out.println("5. Log Out");
                        System.out.println("Enter your choice: ");
                        int choice4 = scanner.nextInt();
                        scanner.nextLine();

                        switch(choice4){
                            case 1:
                                accountManager.debitMoney(account_number);
                                break;
                            case 2:
                                accountManager.creditMoney(account_number);
                                break;
                            case 3:
                                accountManager.transferMoney(account_number);
                                break;
                            case 4:
                                accountManager.checkBalance(account_number);
                                break;
                            case 5:
                                return;

                            default:
                            {
                                System.out.println("Invalid Input!!!");
                                break;
                            }
                        }
                    }
                    }else{
                    System.out.println("             Wrong credentials!!");
                    System.out.println("             OR U are not registered!!!");
                }
                    break;

                case 3:
                    try {
                        System.out.println();
                        System.out.print("Closing the application.");
                        int count = 3;
                        while (count > 0) {
                            System.out.print(".");
                            Thread.sleep(500);
                            count--;
                        }
                        System.out.println();
                        System.out.println("Thanks for using Application!!");
                        System.exit(1);
                        scanner.close();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("Invalid input!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
