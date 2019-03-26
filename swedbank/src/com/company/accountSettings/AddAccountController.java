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
    private List<AccountTypeList> accountTypeList;


    @FXML
    void addAccount() {
        if (letterValidation(accountNameBox.getText(),
                "kontonamnet måste minst vara 3 och högst 9 bokstäver!") == true) {
            if (accountNameBox.getLength() <= 2 || accountNameBox.getLength() >= 10) {
                setErrorMessageBox("");
            } else {
                if (accountTypeValidation()==true){
                    if (validation(accountNumberBox.getText(),
                            "kontonumret kan inte innehålla några tecken eller vara tom!") == true) {
                        if (accountNumberBox.getLength() != 12) {
                            setErrorMessageBox("Kontonumret måste vara 12 siffror!");
                        } else {
                            if (accountNumberValidation()==true){
                                if (validation(amountToNewAccountBox.getText(),
                                        "Beloppet kan inte innehålla några tecken eller vara tom!") == true) {
                                    DB.addAccountToDB(user.getUserID(),
                                            accountNameBox.getText(), accountNumberBox.getText(), amountToNewAccountBox.getText());
                                    logInController.switchScene("/com/company/home/homeWindow.fxml");
                                }
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

    boolean validation(String amount, String errorMessage) {
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
    boolean accountTypeValidation(){
        accountTypeList = DB.getAllAccountsTypeFromDB();
        for (AccountTypeList accountType: accountTypeList) {
            if (accountType.getKontoType().equals(accountNameBox.getText())) {
                setErrorMessageBox("Kontonamnet finns redan!");
                return false;
            }
        }return true;
    }

    @FXML
    boolean accountNumberValidation(){
        allAccountNumbers = DB.getAllAccountNumbersFromDB();
        for (MyAccount accountNumber : allAccountNumbers) {
            if (accountNumber.getKontoNumber().equals(accountNumberBox.getText())) {
                setErrorMessageBox("Kontonumret finns redan!");
                return false;
            }
        }return true;
    }
}
