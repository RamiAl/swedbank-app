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
        PreparedStatement ps = prep("SELECT * FROM users WHERE firstName = ? AND password = ?");
        try {
            ps.setString(1, username);
            ps.setString(2, password);
            result = (User)new ObjectMapper<>(User.class).mapOne(ps.executeQuery());
        } catch (Exception e) { e.printStackTrace(); }
        return result; // return User;
    }

    public static List <UserList> getUsers(long userID){
        String query =
                "SELECT firstName, user_ID FROM users WHERE user_ID =?";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setLong(1, userID);
            return (List<UserList>)(List<?>)new ObjectMapper<>(UserList.class).map(stmt.executeQuery());
        } catch (Exception ex){ ex.printStackTrace(); }
        return null;
    }

    public static List <AccountTypeList> getAllAccountsType(){
        String query =
                "SELECT kontoType FROM allAccounts";
        try {
            PreparedStatement stmt = prep(query);
            return (List<AccountTypeList>)(List<?>)new ObjectMapper<>(AccountTypeList.class).map(stmt.executeQuery());
        } catch (Exception ex){ ex.printStackTrace(); }
        return null;
    }

    public static List <Transaction> getTransactionsFromAccount(String kontoNumber){
        String query =
                "SELECT amount, `time`, to_kontoNumber FROM transactions " +
                        "WHERE from_kontoNumber =?";
        // order by `time` desc
        try {
            PreparedStatement stmt = prep(query);
            stmt.setString(1, kontoNumber);
            var transactions = (List<Transaction>)(List<?>)new ObjectMapper<>(Transaction.class).map(stmt.executeQuery());
            transactions.forEach(t -> t.setAmount(-t.getAmount()));
            return  transactions;
        } catch (Exception ex){ ex.printStackTrace(); }
        return null;
    }

    public static List <Transaction> getTransactionsToAccount(String kontoNumber){
        String query =
                "SELECT from_kontoNumber, time, amount FROM transactions " +
                        "WHERE to_kontoNumber =?";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setString(1, kontoNumber);
            return (List<Transaction>)(List<?>)new ObjectMapper<>(Transaction.class).map(stmt.executeQuery());
        } catch (Exception ex){ ex.printStackTrace(); }
        return null;
    }

    public static void setNewTransaction(String amount, String kontoType, String fromOrTo, long userID){
        String query =
                "INSERT INTO transactions SET amount = ?, from_kontoNumber = ?, to_kontoNumber = ?, user_ID = ?";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setString(1, amount);
            stmt.setString(2, kontoType);
            stmt.setString(3, fromOrTo);
            stmt.setLong(4, userID);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static MyAccount getUsersAccountNumber(long userID, String kontoType){
        MyAccount result = null;
        String query =
                "SELECT kontoNumber FROM allAccounts WHERE user_ID =? AND kontoType = ? ";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setLong(1, userID);
            stmt.setString(2, kontoType);
            result = (MyAccount)new ObjectMapper<>(MyAccount.class).mapOne(stmt.executeQuery());
        } catch (Exception e) { e.printStackTrace(); }
        return result; // return accountNumber;
    }

    public static List <MyAccount> getAllAccountNumbers(){
        String query =
                "SELECT kontoNumber FROM allAccounts";
        try {
            PreparedStatement stmt = prep(query);
            return (List<MyAccount>)(List<?>)new ObjectMapper<>(MyAccount.class).map(stmt.executeQuery());
        } catch (Exception e) { e.printStackTrace(); }
        return null; // return list of accountNumbers;
    }

    public static List <MyAccount> getUserAccountNumbers(long userID){
        String query =
                "SELECT kontoNumber FROM allAccounts WHERE user_ID = ?";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setLong(1, userID);
            return (List<MyAccount>)(List<?>)new ObjectMapper<>(MyAccount.class).map(stmt.executeQuery());
        } catch (Exception e) { e.printStackTrace(); }
        return null; // return list of accountNumbers of a specific user;
    }

    public static List<MyAccount> getMyAccountsInfo(long userID){
            String query =
                    "SELECT kontoType, currentAmount, kontoNumber FROM allAccounts WHERE user_ID = ?";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setLong(1, userID);
            return (List<MyAccount>)(List<?>)new ObjectMapper<>(MyAccount.class).map(stmt.executeQuery());
        } catch (Exception ex){ ex.printStackTrace(); }
        return null;
    }

    public static void setNewAmountToAccount(String sign, int amount, String kontoNumber){
        String query =
                "UPDATE allAccounts SET currentAmount = currentAmount "+sign+" ? " +
                        "WHERE kontoNumber = ?";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setInt(1, amount);
            stmt.setString(2, kontoNumber);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }


    public static void addAccount(long userID, String accountName, String accountNumber, int amountToNewAccount){
        String query =
                "INSERT INTO allAccounts SET user_ID = ?, kontoType = ?, kontoNumber = ?, currentAmount = ?";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setLong(1, userID);
            stmt.setString(2, accountName);
            stmt.setString(3, accountNumber);
            stmt.setInt(4, amountToNewAccount);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }

    }

    public static void deleteAccountFromAllAccounts(String kontoNumber){
        String query =
                "DELETE FROM allAccounts WHERE kontoNumber = ?";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setString(1, kontoNumber);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }

    }

    public static void deleteAccountFromTransactions(String kontoNumber){
        String query =
                "DELETE FROM transactions WHERE from_kontoNumber = ? ";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setString(1, kontoNumber);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }

    }

    public static void changeAccountName(String kontoNumber, String newAccountName){
        String query =
                "UPDATE allAccounts SET kontoType = ?"+
                        "WHERE kontoNumber = ?";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setString(1, newAccountName);
            stmt.setString(2, kontoNumber);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }

    }

    public static void setSalaryToMyLonekonto(int amount, long userID, String kontoType){
        String query =
                "UPDATE allAccounts SET currentAmount = currentAmount + ?"+
                        "WHERE user_ID = ?, kontoType = ?";
        try {
            PreparedStatement stmt = prep(query);
            stmt.setInt(1, amount);
            stmt.setLong(2, userID);
            stmt.setString(3, kontoType);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }

    }

}
