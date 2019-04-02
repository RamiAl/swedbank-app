package com.company.login;

import com.company.db.DB;
import com.company.Entities.User;
import com.company.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogInController {
    @FXML
    public TextField logInNamebox, logInPasswordbox;
    @FXML
    public Label errorMessageBox;
    private static User user = null;
    public static User getUser() { return user; }

    @FXML
    void initialize() {
        System.out.println("initialize com.company.login");
    }

    @FXML
    public void logInButtonPressed(){
        validation();
    }

    public void tabToPasswordBox(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            validation();
        }
    }

    @FXML
    public void setErrorMessageBox(String errorMessage ){
        Platform.runLater(() -> errorMessageBox.setText(errorMessage));
    }

    @FXML
    void checkUserInDB (){
        user = DB.getMatchingUser(logInNamebox.getText(), logInPasswordbox.getText());
        if (user !=null){
            switchScene("/com/company/home/homeWindow.fxml");
        }
        else {
            setErrorMessageBox("Kontrollera användarnamnet eller lösenordet: Användare finns inte!");
        }
    }

    @FXML
    void validation() {
        Platform.runLater(() -> errorMessageBox.setText(""));
        Pattern userNamePattern = Pattern.compile("[a-zA-Z ]{2,10}+");
        Matcher userNameMatcher = userNamePattern.matcher(logInNamebox.getText());
        Pattern passwordPattern = Pattern.compile(".{2,20}");
        Matcher passwordMatcher = passwordPattern.matcher(logInPasswordbox.getText());

        if (userNameMatcher.matches()) {
            if(passwordMatcher.matches()){
                checkUserInDB();
            }else{
                setErrorMessageBox("Kontrollera lösenordet: Minst 2 tecken Max 20");
            }
        } else {
            setErrorMessageBox("Kontrollera användarnamnet: Endast bokstäver, minst 2 max 10");
        }
    }

    public void switchScene(String pathname){
        try {
            Parent root = FXMLLoader.load(getClass().getResource(pathname));
            Scene scene = new Scene(root, 600, 396);
            Main.stage.setScene(scene);
            Main.stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
