package org.thony.junit5app.example.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Bank {
    private String name;
    private List<Account> accounts;

    public Bank() {
        this.accounts = new ArrayList<>();
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param account
     */
    public void addAccount(Account account) {
        accounts.add(account);
        account.setBank(this);
    }

    /**
     * @param origin
     * @param destination
     * @param amount
     */
    public void transfer(Account origin, Account destination, BigDecimal amount) {
        origin.debit(amount);
        destination.credit(amount);
    }

}
