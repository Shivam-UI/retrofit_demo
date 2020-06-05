package com.createdinam.glaucusdemo;

public class Emails {

    private int idtableEmail;
    private String tableEmailEmailAddress,tableEmailValidate;

    public Emails(String tableEmailEmailAddress, String tableEmailValidate) {
        this.tableEmailEmailAddress = tableEmailEmailAddress;
        this.tableEmailValidate = tableEmailValidate;
    }

    public Emails(int idtableEmail, String tableEmailEmailAddress, String tableEmailValidate) {
        this.idtableEmail = idtableEmail;
        this.tableEmailEmailAddress = tableEmailEmailAddress;
        this.tableEmailValidate = tableEmailValidate;
    }

    public int getIdtableEmail() {
        return idtableEmail;
    }

    public void setIdtableEmail(int idtableEmail) {
        this.idtableEmail = idtableEmail;
    }

    public String getTableEmailEmailAddress() {
        return tableEmailEmailAddress;
    }

    public void setTableEmailEmailAddress(String tableEmailEmailAddress) {
        this.tableEmailEmailAddress = tableEmailEmailAddress;
    }

    public String getTableEmailValidate() {
        return tableEmailValidate;
    }

    public void setTableEmailValidate(String tableEmailValidate) {
        this.tableEmailValidate = tableEmailValidate;
    }
}
