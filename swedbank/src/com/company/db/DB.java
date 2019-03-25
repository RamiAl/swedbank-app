package com.company.db;

import com.company.Entities.User;
import com.company.Entities.UserList;
import com.company.accountSettings.AccountTypeList;
import com.company.home.Transaction;
import com.company.home.MyAccount;

import java.sql.*;
import java.util.List;

public abstract class DB {

    public static PreparedStatement prep(String SQLQuery){
        return Database.getInstance().prepareStatement(SQLQuery);
    }

    public static User getMatchingUser(String username, String password){
        User result = null;
        PreparedStatement ps = prep("SELECT * FROM users WHERE userName = ? AND password = ?");
        try {
            ps.setString(1, username);
            ps.setString(2, password);
            result = (User)new ObjectMapper<>(User.class).mapOne(ps.executeQuery());
        } catch (Exception e) { e.printStackTrace(); }
        return result; // return User;
    }

    public static List <UserList> getUsersFromDB(long userID){
        String query =
                "SELECT userName, user_ID FROM users WHERE user_ID =?";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setLong(1, userID);
            return (List<UserList>)(List<?>)new ObjectMapper<>(UserList.class).map(stmt.executeQuery());
        } catch (Exception ex){ ex.printStackTrace(); }
        return null;
    }

    public static List <AccountTypeList> getAllAccountsTypeFromDB(){
        String query =
                "SELECT kontoType FROM myAccounts";
        try {
            PreparedStatement stmt = prep(query);
            return (List<AccountTypeList>)(List<?>)new ObjectMapper<>(AccountTypeList.class).map(stmt.executeQuery());
        } catch (Exception ex){ ex.printStackTrace(); }
        return null;
    }

    public static List <Transaction> getMyTransaktionerFromDB(String kontoNumber, int numberOftransactions){
            String query =
                    "SELECT amount, from_to FROM transactions WHERE kontoNumber =? LIMIT ? OFFSET 0";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setString(1, kontoNumber);
            stmt.setInt(2, numberOftransactions);
            return (List<Transaction>)(List<?>)new ObjectMapper<>(Transaction.class).map(stmt.executeQuery());
        } catch (Exception ex){ ex.printStackTrace(); }
        return null;
    }

    public static void setNewTransaction(String amount, String kontoNumber, String fromOrTo){
        String query =
                "INSERT INTO transactions SET amount = ?, kontoNumber = ?, from_to = ?";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setString(1, amount);
            stmt.setString(2, kontoNumber);
            stmt.setString(3, fromOrTo);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static MyAccount getUsersAccountFromDB(long userID, String kontoType){
        MyAccount result = null;
        String query =
                "SELECT kontoNumber FROM myAccounts WHERE user_ID =? AND kontoType = ? ";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setLong(1, userID);
            stmt.setString(2, kontoType);
            result = (MyAccount)new ObjectMapper<>(MyAccount.class).mapOne(stmt.executeQuery());
        } catch (Exception e) { e.printStackTrace(); }
        return result; // return list of accountNumbers;
    }

    public static List <MyAccount> getAllAccountNumbersFromDB(){
        String query =
                "SELECT kontoNumber FROM myAccounts";
        try {
            PreparedStatement stmt = prep(query);
            return (List<MyAccount>)(List<?>)new ObjectMapper<>(MyAccount.class).map(stmt.executeQuery());
        } catch (Exception e) { e.printStackTrace(); }
        return null; // return list of accountNumbers;
    }

    public static List<MyAccount> getMyAccountsFromDB(long userID){
            String query =
                    "SELECT kontoType, currentAmount, kontoNumber FROM myAccounts WHERE user_ID = ?";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setLong(1, userID);
            return (List<MyAccount>)(List<?>)new ObjectMapper<>(MyAccount.class).map(stmt.executeQuery());
        } catch (Exception ex){ ex.printStackTrace(); }
        return null;
    }

    public static void setNewAmountToAccount(String sign, String amount, String kontoNumber){
        String query =
                "UPDATE myAccounts SET currentAmount = currentAmount "+sign+" ? " +
                        "WHERE kontoNumber = ?";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setString(1, amount);
            stmt.setString(2, kontoNumber);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }


    public static void addAccountToDB(long userID, String accountName, String accountNumber, String amountToNewAccount){
        String query =
                "INSERT INTO myAccounts SET user_ID = ?, kontoType = ?, kontoNumber = ?, currentAmount = ?";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setLong(1, userID);
            stmt.setString(2, accountName);
            stmt.setString(3, accountNumber);
            stmt.setString(4, amountToNewAccount);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }

    }

    public static void deleteAccountFromDB(String kontoNumber){
        String query =
                "DELETE FROM myAccounts WHERE kontoNumber = ?";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setString(1, kontoNumber);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }

    }

    public static void deleteTransactionFromDB(String kontoNumber){
        String query =
                "DELETE FROM transactions WHERE kontoNumber = ?";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setString(1, kontoNumber);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }

    }

    public static void changeAccountNameInDB(String kontoNumber, String newAccountName){
        String query =
                "UPDATE myAccounts SET kontoType = ?"+
                        "WHERE kontoNumber = ?";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setString(1, newAccountName);
            stmt.setString(2, kontoNumber);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }

    }

    public static void setSalaryToMyLonekonto(String amount, long userID, String kontoType){
        String query =
                "UPDATE myAccounts SET currentAmount = currentAmount + ?"+
                        "WHERE kontoNumber = ?";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setString(1, amount);
            stmt.setLong(2, userID);
            stmt.setString(2, kontoType);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }

    }

}
