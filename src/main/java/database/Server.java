package database;

import java.sql.*;
import java.util.Scanner;

public class Server {
    static Connection dbCon;
    static PreparedStatement theStatement;

    public static void startConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_wallet?serverTimezone=UTC", "root", "");
            System.out.println("We are connected to the database now...");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Driver Not Found" + e);
        }

    }

    public static void closeConnection() {
        try {
            dbCon.close();
            System.out.println("Connection Closed.");
        } catch (SQLException e) {
            System.out.println("Could not close connection." + e);
        }
    }

    public static void addPerson() {
        Scanner scan = new Scanner(System.in);
        Integer mobileNumber = null;
        while (mobileNumber == null) {
            System.out.println("Mobile Number (Mandatory)");
            mobileNumber = scan.nextInt();
        }
        scan = new Scanner(System.in);
        System.out.println("Enter Name");
        String name = scan.nextLine();


        String qry = "Insert into person (Mobile_Number, Name, Balance) values (?, ?, ?)";

        try {
            theStatement = dbCon.prepareStatement(qry);
            theStatement.setInt(1, mobileNumber);
            theStatement.setString(2, name);
            theStatement.setInt(3, 0);

            if (theStatement.executeUpdate() > 0) {
                System.out.println("SignUp Successful. Login to continue. ");
            }

        } catch (SQLException e) {
            System.out.println("Wrong Query" + e);
        }
    }

    public static void viewBalance(Integer MobileNumber) {
        String qry = "Select * from Person where mobile_number = ? ";
        try {
            theStatement = dbCon.prepareStatement(qry);
            theStatement.setInt(1, MobileNumber);
            ResultSet rs = theStatement.executeQuery();
            while (rs.next()) {
                System.out.println("Mobile Number: " + rs.getInt("Mobile_Number") + " || Name: "
                        + rs.getString("Name") + " || Balance: " + rs.getInt("Balance"));
            }


        } catch (SQLException e) {
            System.out.println("Could not view balance " + e);
        }

    }
    public static void transactionHistory(Integer MobileNumber) {
        String qry = "Select * from Transactions where mobile_number = ? ";
        System.out.println("Inside Transaction History");
        try {
            theStatement = dbCon.prepareStatement(qry);
            theStatement.setInt(1, MobileNumber);
            ResultSet rs = theStatement.executeQuery();
            while (rs.next()) {
                System.out.println("Mobile Number: " + rs.getInt("Mobile_Number") + " || Date: "
                        + rs.getDate("Date_Time")  + " || Details: "
                        + rs.getString("Details") + " || Balance: " + rs.getInt("Balance"));
            }


        } catch (SQLException e) {
            System.out.println("Could not view balance " + e);
        }

    }
    public static void addTransaction(Integer mobileNumber, String details, Integer balance ) {
        System.out.println("Inside addTransaction");

        String qry = "Insert into transactions ( Mobile_Number, Details, Balance) values(?, ?, ?) ";
        try {
            theStatement = dbCon.prepareStatement(qry);
            System.out.println("Inside addTransaction");

            theStatement.setString(2,details);
            theStatement.setInt(3,balance);
            theStatement.setInt(1,mobileNumber);
            System.out.println("Inside addTransaction");

            if (theStatement.executeUpdate() > 0) {
                System.out.println("Transaction Added");
            }
            else{
                System.out.println("Could not add transaction");
            }

        } catch (SQLException e) {
            System.out.println("Wrong query" + e);
        }

    }



    public static Boolean verifyUser(Integer MobileNumber) {
        String qry = "Select * From Person where mobile_number = ?";

        try {
            theStatement = dbCon.prepareStatement(qry);
            theStatement.setInt(1, MobileNumber);
            ResultSet rs = theStatement.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("Fetching Failed" + e);
        }
        return false;
    }

    public static void addMoney(Integer mobileNumber, Integer amount) {
        String qry = "Update Person Set Balance = Balance + ? where mobile_number = ? ";

        try {
            theStatement = dbCon.prepareStatement(qry);
            theStatement.setInt(1, amount);
            theStatement.setInt(2, mobileNumber);

            if (theStatement.executeUpdate() > 0) {
                System.out.println("Balance Updated ");
                Integer balance = Server.checkBalance(mobileNumber);
                Server.addTransaction(mobileNumber,"Money Added to your account",balance);
            }

        } catch (SQLException e) {
            System.out.println("Wrong Query" + e);
        }
    }

    public static void withdrawMoney(Integer mobileNumber, Integer amount) {
        Integer balance = Server.checkBalance(mobileNumber);
        if (balance >= amount) {
            String qry = "Update Person Set Balance = Balance - ? where mobile_number = ?";

            try {
                theStatement = dbCon.prepareStatement(qry);
                theStatement.setInt(1, amount);
                theStatement.setInt(2, mobileNumber);

                if (theStatement.executeUpdate() > 0) {
                    System.out.println("Balance Updated ");
                    Server.addTransaction(mobileNumber,"Money Withdrawn from your account",balance-amount);
                }

            } catch (SQLException e) {
                System.out.println("Wrong Query" + e);
            }
        } else {
            System.out.println("Balance is less than " + amount);
        }

    }

    private static Integer checkBalance(Integer mobileNumber) {
        String qry = "Select Balance from Person where Mobile_Number = ? ";
        Integer balance= null;
        try {
            theStatement = dbCon.prepareStatement(qry);
            theStatement.setInt(1, mobileNumber);

            ResultSet rs = theStatement.executeQuery();
            if (rs.next()) {
                balance = rs.getInt("Balance");
            }
        } catch (SQLException e) {
            System.out.println("Wrong query for balance check" + e);
        }
        return balance;
    }

    public static void changeName(Integer MobileNumber, String name) {
        String qry = "Update Person Set name = ? where mobile_number = ?";

        try {
            theStatement = dbCon.prepareStatement(qry);
            theStatement.setString(1, name);
            theStatement.setInt(2, MobileNumber);

            if (theStatement.executeUpdate() > 0) {
                System.out.println("Name Updated");
            }

        } catch (SQLException e) {
            System.out.println("Wrong Query" + e);
        }
    }
}
