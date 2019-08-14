package edu.uta.leanmed.pojo;

/**
 * Created by Vaibhav's Console on 4/21/2019.
 */

public class UserResponse {
    private int status;
    private String message;
    private String token;
    private User user;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
