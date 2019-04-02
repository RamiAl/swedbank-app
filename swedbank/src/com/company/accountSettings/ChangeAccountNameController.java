package com.company.accountSettings;

import com.company.Entities.User;
import com.company.db.DB;
import com.company.home.MyAccount;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.company.login.*;

import java.util.List;

public class ChangeAccountNameController {
    @FXML
    public ComboBox dropDownAccountsList;
    @FXML
    public TextField newAccountNameBox;
    @FXML
    public Label errorMessageBox;
    User user = LogInController.getUser();
    private List<MyAccount> myAccountsList;
    LogInController logInController = new LogInController();
    private MyAccount getSelectedItem;
    private List<AccountTypeList> accountTypeList;

    @FXML
    void initialize() {
        myAccountsList = DB.getMyAccountsInfo(user.getUserID());
        for (MyAccount myAccount : myAccountsList) {
            dropDownAccountsList.getItems().add(myAccount);
        }
    }

    @FXML
    void changeAccountName() {
        getSelectedItem = (MyAccount) dropDownAccountsList.getSelectionModel().getSelectedItem();
        if (dropDownAccountsList == null) {
            setErrorMessageBox("Du måste välja konto!");
        } else {
            if (getSelectedItem.getKontoType().equals("kortkonto")){
                setErrorMessageBox("Du får inte ändra namn på konton som andra överföra till!");
            } else {
                if (accountTypeValidation() == true) {
                    if (getSelectedItem.getKontoType().equals("kortkonto") ||
                            getSelectedItem.getKontoType().equals("kortkonto")) {
                        setErrorMessageBox("Du får inte ändra namn på konton som andra överföra till!");
                    } else {
                        DB.changeAccountName(getSelectedItem.getKontoNumber(), newAccountNameBox.getText());
                        logInController.switchScene("/com/company/home/homeWindow.fxml");
                    }
                }
            }
        }
    }

    @FXML
    boolean accountTypeValidation(){
        accountTypeList = DB.getAllAccountsType();
        for (AccountTypeList accountType: accountTypeList) {
            if (accountType.getKontoType().equals(newAccountNameBox.getText())) {
                setErrorMessageBox("Kontonamnet finns redan!");
                return false;
            }
        }return true;
    }

    @FXML
    public void setErrorMessageBox(String errorMessage ){
        Platform.runLater(() -> errorMessageBox.setText(errorMessage));
    }

    @FXML
    void goHome(){
        logInController.switchScene("/com/company/home/homeWindow.fxml");
    }
}
