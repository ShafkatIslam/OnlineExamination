package com.example.professt.onlineexamination;

public class Reminder {

    String cell,email,number;

    private static Reminder reminder= new Reminder();

    public Reminder() {
    }

    public static Reminder getInstance( ) {
        return reminder;
    }


    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
