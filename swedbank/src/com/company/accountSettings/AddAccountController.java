package com.company.accountSettings;

import com.company.Entities.User;
import com.company.db.DB;
import com.company.home.MyAccount;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.company.login.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddAccountController {

    @FXML
    public TextField accountNameBox, accountNumberBox, amountToNewAccountBox;
    @FXML
    public Label errorMessageBox;
    User user = LogInController.getUser();
    private List<MyAccount> allAccountNumbers;
    LogInController logInController = new LogInController();
    private List<MyAccount> myAccountsList;

    @FXML
    void addAccount() {
        if (letterValidation(accountNameBox.getText(),
                "kontonamnet måste minst vara 3 och högst 9 bokstäver!") == true) {
            if (accountNameBox.getLength() <= 2 || accountNameBox.getLength() >= 10) {
                setErrorMessageBox("");
            } else {
                if (accountNameValidation()==true){
                    if (checkAccountNumber()==true) {
                        if (accountNumberValidation()==true){
                            if (errorvalidation(amountToNewAccountBox.getText(),
                                    "Beloppet kan inte innehålla några tecken eller vara tom!") == true)
                            { DB.addAccount(user.getUserID(),
                                    accountNameBox.getText(),
                                    accountNumberBox.getText(),
                                    Integer.parseInt(amountToNewAccountBox.getText()));
                                logInController.switchScene("/com/company/home/homeWindow.fxml");
                            }
                        }
                    }
                }
            }
        }
    }

    @FXML
    void goHome(){
        logInController.switchScene("/com/company/home/homeWindow.fxml");
    }

    @FXML
    public void setErrorMessageBox(String errorMessage ){
        Platform.runLater(() -> errorMessageBox.setText(errorMessage));
    }

    boolean errorvalidation(String amount, String errorMessage) {
        Pattern numberPattern = Pattern.compile("^[\\d]{1,}");
        Matcher numberMatcher = numberPattern.matcher(amount);
        if (numberMatcher.matches()) {
            return true;
        }
        else{
            setErrorMessageBox(errorMessage);
            return false;
        }
    }

    boolean letterValidation(String amount, String errorMessage) {
        Pattern userNamePattern = Pattern.compile("[a-zA-Z]+");
        Matcher userNameMatcher = userNamePattern.matcher(amount);
        if (userNameMatcher.matches()) {
            return true;
        }
        else{
            setErrorMessageBox(errorMessage);
            return false;

        }
    }

    @FXML
    boolean checkAccountNumber(){
        Pattern userNamePattern = Pattern.compile("([0-9]{4}[,]{1}[0-9]{1}[\\-][0-9]{4}[ ]{1}[0-9]{4})");
        Matcher userNameMatcher = userNamePattern.matcher(accountNumberBox.getText());
        if (userNameMatcher.matches()) {
            return true;
        }
        else{
            setErrorMessageBox("kontonumret måste innehålla 15 tecken och enligt exemplet!");
            return false;

        }
    }

    @FXML
    boolean accountNameValidation(){
        myAccountsList = DB.getMyAccountsInfo(user.getUserID());
        for (MyAccount accountType: myAccountsList) {
            if (accountType.getKontoType().equals(accountNameBox.getText())) {
                setErrorMessageBox("Kontonamnet finns redan!");
                return false;
            }
        }return true;
    }

    @FXML
    boolean accountNumberValidation(){
        allAccountNumbers = DB.getAllAccountNumbers();
        for (MyAccount accountNumber : allAccountNumbers) {
            if (accountNumber.getKontoNumber().equals(accountNumberBox.getText())) {
                setErrorMessageBox("Kontonumret finns redan!");
                return false;
            }
        }return true;
    }
}
