package com.company.home;

import com.company.login.*;
import com.company.db.DB;
import com.company.Entities.User;
import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.List;

import java.util.Timer;
import java.util.TimerTask;


public class HomeController {

    @FXML
    public ListView myAccountsList, transactionList, lastFiveTransactionsList;
    @FXML
    public Label userNameHomePage;
    @FXML
    public Label errorMessageBox;
    User user = LogInController.getUser();
    int numberOfTransactions = 10;
    MyAccount myAccount;
    LogInController logInController = new LogInController();

    @FXML
    void initialize(){
        userNameHomePage.setText(user.getUserName());
        displayMyLastFiveTransactions();
        displayMyAccounts(user.getUserID());
        //performCardPayment("300", "345364566565466");
    }

    @FXML
    public void goToTransactions() {
        displayMyTransactions(10);
    }

    @FXML
    void displayMyAccounts(long userID){
        List<MyAccount> myAccounts = DB.getMyAccountsFromDB(userID);
        for (MyAccount myAccount : myAccounts) {
            myAccountsList.getItems().add(myAccount);
        }
    }

    @FXML
    void displayMyTransactions(int numberOftransactions) {
        if ((myAccountsList.getSelectionModel().getSelectedItem()) ==null ){
            setErrorMessageBox("Du måste först välja konto");
        }else {
            setErrorMessageBox("");
            myAccount = (MyAccount) myAccountsList.getSelectionModel().getSelectedItem();
            transactionList.getItems().clear();

            List<Transaction> transactions = DB.getAccountsTransactionsFromDB(myAccount.getKontoType(), numberOftransactions);
            for (Transaction transaction : transactions) {
                transactionList.getItems().add(transaction);
            }
        }
    }

    @FXML
    void visaMerTransaktioner(){
        numberOfTransactions+=10;
        displayMyTransactions(numberOfTransactions);
    }

    @FXML
    void displayMyLastFiveTransactions(){
        List<Transaction> transactions = DB.getMyTransaktionerFromDB(user.getUserID());
        for (Transaction transaction : transactions) {
            lastFiveTransactionsList.getItems().add(transaction);
        }
    }

    @FXML
    void performCardPayment(String amount, String kontoNumber){
        DB.setNewAmountToAccount("-", amount, kontoNumber);
    }

    @FXML
    void sendAmountToMyOtherAccount(){
        logInController.switchScene("/com/company/transfer/transferAmountToMyOtherAccount.fxml");
    }

    @FXML
    void addAccount(){
        logInController.switchScene("/com/company/accountSettings/addAccount.fxml");
    }

    @FXML
    void sendAmountToOthersAccount(){
        logInController.switchScene("/com/company/transfer/transferAmountToOthersAccount.fxml");
    }

    @FXML
    void deleteAccount(){
        if ((myAccountsList.getSelectionModel().getSelectedItem()) ==null ){
            setErrorMessageBox("Du måste först välja konto");
        }else {
            myAccount = (MyAccount) myAccountsList.getSelectionModel().getSelectedItem();
            if (myAccount.getKontoType().equals("kortkonto")){
                setErrorMessageBox("Du får inte ta bort konto som andra överföra till!");
            }else {
                setErrorMessageBox("");
                DB.deleteFromDB("transactions", myAccount.getKontoNumber());
                DB.deleteFromDB("allAccounts", myAccount.getKontoNumber());
                myAccountsList.getItems().clear();
                displayMyAccounts(user.getUserID());
            }
        }
    }

    @FXML
    void changeAccountName() {
        logInController.switchScene("/com/company/accountSettings/changeAccountName.fxml");
    }

    @FXML
    void logOut(){
        logInController.switchScene("/com/company/login/logIn.fxml");
    }


    void setSalaryOnceAMonth(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                DB.setSalaryToMyLonekonto("30000", user.getUserID(), "lönekonto" );
            }
        }, 0, 2629743); //user will get the salary once a month
    }

    @FXML
    public void setErrorMessageBox(String errorMessage ) {
        Platform.runLater(() -> errorMessageBox.setText(errorMessage));
    }

}

















