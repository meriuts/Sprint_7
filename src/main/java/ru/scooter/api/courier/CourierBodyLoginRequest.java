package ru.scooter.api.courier;

public class CourierBodyLoginRequest {
    private String login;
    private String password;

    public CourierBodyLoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public CourierBodyLoginRequest() {

    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
