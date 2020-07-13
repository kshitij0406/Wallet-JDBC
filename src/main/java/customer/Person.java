package customer;

import database.Server;

public class Person {
    private Integer mobileNumber;

    public Person(Integer mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void addMoney(Integer amount) {
        Server.addMoney(this.mobileNumber, amount);
    }

    public void withdrawMoney(Integer amount) {
        Server.withdrawMoney(this.mobileNumber, amount);
    }

    public void viewBalance() {
        Server.viewBalance(this.mobileNumber);
    }
    public void transactionHistory() {
        Server.transactionHistory(this.mobileNumber);
    }

    public void changeName(String name){
        Server.changeName(this.mobileNumber, name);
    }

}
