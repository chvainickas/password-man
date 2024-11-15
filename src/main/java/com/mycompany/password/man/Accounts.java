/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.password.man;

/**
 *
 * @author Jodie Boylan
 * x20515659
 */
public class Accounts {
    private String service;
    private String login;
    private String password;

    public Accounts(String service, String login, String password) {
        this.service = service;
        this.login = login;
        this.password = password;
    }

    public String getService() {
        return service;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}

