package start;

import customer.*;
import database.*;

import java.util.Scanner;

public class Start {
    public static void main(String[] args) {

        Server.startConnection();
        Integer mobileNumber;
        int choice, amount, option;
        String name;

        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.println(" 1. Signup \n 2. Login \n 3. EXIT");
            choice = scan.nextInt();

            if (choice == 1) {
                Server.addPerson();
            } else if (choice == 2) {
                System.out.println("Enter Mobile Number to login......");
                mobileNumber = scan.nextInt();
                if (!Server.verifyUser(mobileNumber)) {
                    System.out.println("Could not verify user");
                    continue;
                }

                Person p = new Person(mobileNumber);


                while (true) {
                    System.out.println(" 1. Add Money to Wallet \n 2. Withdraw Money from Wallet \n " +
                            "3. View Balance \n 4. Change Name \n 5. Transaction History \n 6. LOGOUT");
                    option = scan.nextInt();
                    if (option == 1) {
                        System.out.println("Enter the amount you want to add.");
                        amount = scan.nextInt();
                        p.addMoney(amount);
                    } else if (option == 2) {
                        System.out.println("Enter the amount you want to withdraw.");
                        amount = scan.nextInt();
                        p.withdrawMoney(amount);
                    } else if (option == 3) {
                        p.viewBalance();
                    } else if (option == 4) {
                        scan = new Scanner(System.in);
                        System.out.println("Enter the new name");
                        name = scan.nextLine();
                        p.changeName(name);
                    } else if (option == 5) {
                        p.transactionHistory();

                    } else if (option == 6) {

                        break;
                    }else {
                        System.out.println("Choose from above options");
                    }
                }

            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Enter correct Input");
            }

        }
        System.out.println();

        Server.closeConnection();

    }
}

