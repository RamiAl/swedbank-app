package com.company.transfer;

import com.company.db.DB;
import com.company.Entities.User;
import com.company.Entities.UserList;
import com.company.home.MyAccount;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.company.login.LogInController;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransferToOthersAccountController {
    @FXML
    public ComboBox fromDropDownAccountsList, toDropDownAccountsList;
    @FXML
    public TextField amountBox;
    @FXML
    public Label errorMessageBox;
    User user = LogInController.getUser();
    private List<MyAccount> myAccountsList1;
    private List <UserList> otherUser;
    private MyAccount toUsersAccountNumber;
    LogInController logInController = new LogInController();


    @FXML
    void initialize(){
        User user = LogInController.getUser();
        if (user.getUserID() ==1) {
            displayUser(user.getUserID());
            displayOtherUser(2);
            displayOtherUser(3);
        }else if (user.getUserID() ==2) {
            displayUser(user.getUserID());
            displayOtherUser(1);
            displayOtherUser(3);
        }else {
            displayUser(user.getUserID());
            displayOtherUser(1);
            displayOtherUser(2);
            }
        }

    @FXML
    void goHome(){
        logInController.switchScene("/com/company/home/homeWindow.fxml");
    }

    @FXML
    void sendAmountToOthersAccount(){
        MyAccount fromSelectedItem = (MyAccount) fromDropDownAccountsList.getSelectionModel().getSelectedItem();
        UserList toSelectedItem = (UserList) toDropDownAccountsList.getSelectionModel().getSelectedItem();

        if (fromSelectedItem == null || toSelectedItem == null) {
            setErrorMessageBox("Du måste välja konto och belopp innan du kan gå vidare!");
        }else {
            if (amountBox.getText().equals("")){
                setErrorMessageBox("Du måsta ange belopp");
            }else {
                if (validation(amountBox.getText(),
                        "Beloppet för inte innehålla några tecken eller vara tom!") == true) {
                    if (Integer.parseInt(amountBox.getText()) == 0){
                        setErrorMessageBox("Beloppet måste minst vara 1kr");
                    }
                    else if (fromSelectedItem.getCurrentAmount() <
                            Integer.parseInt(amountBox.getText())) {
                        setErrorMessageBox("Du har inte tillräcklig saldo");
                    } else {
                        toUsersAccountNumber = DB.getUsersAccountNumber(toSelectedItem.getUserID(),
                                "kortkonto");
                        DB.setNewAmountToAccount(
                                "-", Integer.parseInt(amountBox.getText()), fromSelectedItem.getKontoNumber());

                        DB.setNewTransaction(
                                amountBox.getText(),
                                fromSelectedItem.getKontoNumber(),
                                toUsersAccountNumber.getKontoNumber(),
                                user.getUserID());

                        DB.setNewAmountToAccount(
                                "+", Integer.parseInt(amountBox.getText()), toUsersAccountNumber.getKontoNumber());
                        logInController.switchScene("/com/company/home/homeWindow.fxml");
                    }
                }
            }
        }
    }

    void displayUser (long userID){
        myAccountsList1 = DB.getMyAccountsInfo(userID);
        for (MyAccount myAccount : myAccountsList1) {
            fromDropDownAccountsList.getItems().add(myAccount);
        }
    }

    void displayOtherUser (int userID){
        otherUser = DB.getUsers(userID);
        for (UserList othersAccount : otherUser) {
            toDropDownAccountsList.getItems().add(othersAccount);
        }
    }

    @FXML
    public void setErrorMessageBox(String errorMessage ){
        Platform.runLater(() -> errorMessageBox.setText(errorMessage));
    }

    @FXML
    boolean validation(String amount, String errorMessage) {
        Pattern userNamePattern = Pattern.compile("^[\\d]{1,}");
        Matcher userNameMatcher = userNamePattern.matcher(amount);
        if (userNameMatcher.matches()) {
            return true;
        }
        else{
            setErrorMessageBox(errorMessage);
            return false;
        }
    }
}
