package org.thony.junit5app.example.models;

import org.thony.junit5app.example.exceptions.InsufficientFunds;

import java.math.BigDecimal;

public class Account {
    private String person;
    private BigDecimal credit;
    private Bank bank;

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Account(String person, BigDecimal credit) {
        this.person = person;
        this.credit = credit;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public void debit(BigDecimal amount) {
        BigDecimal subtract = this.credit.subtract(amount);
        if (subtract.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFunds("Insufficient Funds");
        }
        this.credit = subtract;
    }

    /**
     * @param amount para aumentar el saldo
     */
    public void credit(BigDecimal amount) {
        this.credit = this.credit.add(amount);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Account)) {
            return false;
        }
        // cas
        Account account = (Account) obj;
        if (this.person == null) {
            return false;
        }

        return this.person.equals(account.getPerson())
               && this.credit.equals(account.credit);
    }
}
