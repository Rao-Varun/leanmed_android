package edu.uta.leanmed.pojo;

import java.util.List;

public class RequestResponse {
    private int status;
    private String message;
    private List<Request> requests;

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

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    @Override
    public String toString() {
        return "RequestResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", requests=" + requests +
                '}';
    }
}
