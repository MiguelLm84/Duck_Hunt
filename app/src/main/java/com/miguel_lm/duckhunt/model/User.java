package com.miguel_lm.duckhunt.model;

public class User {

    private String nick;
    private int ducks;
    private String email;

    public User() {
    }

    public User(String nick, int ducks, String email) {
        this.nick = nick;
        this.ducks = ducks;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getDucks() {
        return ducks;
    }

    public void setDucks(int ducks) {
        this.ducks = ducks;
    }
}
