package com.company.home;

import com.company.login.*;
import com.company.db.DB;
import com.company.Entities.User;
import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.Comparator;
import java.util.List;

import java.util.Timer;
import java.util.TimerTask;


public class HomeController {

    @FXML
    public ListView myAccountsList, transactionList;
    @FXML
    public Label userNameHomePage;
    @FXML
    public Label errorMessageBox;
    User user = LogInController.getUser();
    MyAccount myAccount;
    LogInController logInController = new LogInController();
    List<Transaction> transactions;


    @FXML
    void initialize(){
        userNameHomePage.setText(user.getUserName().toUpperCase());
        displayMyAccounts(user.getUserID());
        //performCardPayment(500);
    }

    @FXML
    public void goToTransactions() {
        int morTransactions = 10;
        transactions = displayMyTransactions(morTransactions);
    }

    @FXML
    void displayMyAccounts(long userID){
        List<MyAccount> myAccounts = DB.getMyAccountsInfo(userID);
        for (MyAccount myAccount : myAccounts) {
            myAccountsList.getItems().add(myAccount);
        }
    }

    @FXML
    List<Transaction> displayMyTransactions(int numberOftransactions) {
        int iRaknare =0;
        if ((myAccountsList.getSelectionModel().getSelectedItem()) ==null ){
            setErrorMessageBox("Du måste först välja konto");
        }else {
            setErrorMessageBox("");
            myAccount = (MyAccount) myAccountsList.getSelectionModel().getSelectedItem();
            transactionList.getItems().clear();

            List<Transaction> transactions =
                    DB.getTransactionsFromAccount(myAccount.getKontoNumber());

            transactions.addAll(
                    DB.getTransactionsToAccount(myAccount.getKontoNumber())
            );
            transactions.sort(Comparator.comparingLong(Transaction::getTime).reversed());

            if (transactions.size() <= 10) {
                for (Transaction transaction : transactions) {
                    transactionList.getItems().add(transaction);
                }
            } else {
                for (int i = numberOftransactions; transactions.size() + i > transactions.size(); i--) {
                    if (transactions.get(iRaknare) != null) {
                        transactionList.getItems().add(transactions.get(iRaknare));
                        iRaknare++;
                    }
                }
            } return transactions;
        } return null;
    }

    @FXML
    void visaMerTransaktioner(){
        if ((myAccountsList.getSelectionModel().getSelectedItem()) ==null ){
            setErrorMessageBox("Du måste först välja konto");
        }else {
            transactionList.getItems().clear();
            for (Transaction transaction : transactions) {
                transactionList.getItems().add(transaction);
            }
        }
    }

    @FXML
    void performCardPayment(int amount){
        MyAccount userAccountNumber = DB.getUsersAccountNumber(user.getUserID(),"kortkonto");
        DB.setNewAmountToAccount("-", amount,userAccountNumber.getKontoNumber() );
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
            if (myAccount.getKontoType().equals("kortkonto") || myAccount.getKontoType().equals("lönekonto")){
                setErrorMessageBox("Du får inte ta bort konto som andra överföra till!");
            }else {
                setErrorMessageBox("");
                DB.deleteAccountFromTransactions(myAccount.getKontoNumber());
                DB.deleteAccountFromAllAccounts( myAccount.getKontoNumber());
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
                DB.setSalaryToMyLonekonto(25000, user.getUserID(), "lönekonto" );
            }
        }, 0, 2629743); //user will get the salary every a month
    }

    @FXML
    public void setErrorMessageBox(String errorMessage ) {
        Platform.runLater(() -> errorMessageBox.setText(errorMessage));
    }

}

















