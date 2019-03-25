package com.company.transfer;

import com.company.db.DB;
import com.company.Entities.User;
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

public class TransferToMyAccountController {
    @FXML
    public ComboBox fromDropDownAccountsList, toDropDownAccountsList;
    @FXML
    public TextField amountBox;
    @FXML
    public Label errorMessageBox;
    User user = LogInController.getUser();
    private List<MyAccount> myAccountsList1;
    private List<MyAccount> myAccountsList2;
    LogInController logInController = new LogInController();
    private MyAccount fromSelectedItem;
    private MyAccount toSelectedItem;



    @FXML
    void initialize(){
        System.out.println(user.getUserID());
        myAccountsList1 = DB.getMyAccountsFromDB(user.getUserID());
        for (MyAccount myAccount : myAccountsList1) {
            fromDropDownAccountsList.getItems().add(myAccount);
        }

        myAccountsList2 = DB.getMyAccountsFromDB(user.getUserID());
        for (MyAccount myAccount : myAccountsList2) {
            toDropDownAccountsList.getItems().add(myAccount);
        }
    }

    @FXML
    void goHome(){
        logInController.switchScene("/com/company/home/homeWindow.fxml");
    }

    @FXML
    void sendAmountToOtherAccount(){
        fromSelectedItem = (MyAccount) fromDropDownAccountsList.getSelectionModel().getSelectedItem();
        toSelectedItem = (MyAccount) toDropDownAccountsList.getSelectionModel().getSelectedItem();

        if (fromSelectedItem == null || toSelectedItem == null) {
            setErrorMessageBox("Du måste välja konto och belopp innan du kan gå vidare!");
        }else {
            if (fromSelectedItem.getKontoNumber().equals(toSelectedItem.getKontoNumber())) {
                setErrorMessageBox("Du kan inte välja samma konto");
            }else
                if (amountBox.getText().equals("")){
                    setErrorMessageBox("Du måsta ange belopp");
                }else {
                    if (validation() == true) {
                        if (Integer.parseInt(amountBox.getText()) == 0){
                            setErrorMessageBox("Beloppet måste minst vara 1kr");
                        }
                        else if (Integer.parseInt(fromSelectedItem.getCurrentAmount()) <
                                Integer.parseInt(amountBox.getText())) {
                            setErrorMessageBox("Du har inte tillräcklig saldo");
                        } else {
                            DB.setNewAmountToAccount(
                                    "-", amountBox.getText(), fromSelectedItem.getKontoNumber());
                            DB.setNewTransaction(
                                    amountBox.getText(),
                                    toSelectedItem.getKontoNumber(), fromSelectedItem.getKontoNumber());

                            DB.setNewAmountToAccount("+", amountBox.getText(), toSelectedItem.getKontoNumber());
                            DB.setNewTransaction(
                                    "-" + amountBox.getText(),
                                    fromSelectedItem.getKontoNumber(), toSelectedItem.getKontoNumber());
                            logInController.switchScene("/com/company/home/homeWindow.fxml");
                        }
                    }
                }
            }
        }

    @FXML
    public void setErrorMessageBox(String errorMessage ){
        Platform.runLater(() -> errorMessageBox.setText(errorMessage));
    }

    @FXML
    boolean validation() {
        Platform.runLater(() -> errorMessageBox.setText(""));
        Pattern userNamePattern = Pattern.compile("^[\\d]{1,}");
        Matcher userNameMatcher = userNamePattern.matcher(amountBox.getText());
        if (userNameMatcher.matches()) {
            return true;
        }
        else{
            setErrorMessageBox("Beloppet för inte innehålla några tecken eller vara tom!");
            return false;
        }
    }
}
