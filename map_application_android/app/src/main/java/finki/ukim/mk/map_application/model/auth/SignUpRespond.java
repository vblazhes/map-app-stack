package finki.ukim.mk.map_application.model.auth;

public class SignUpRespond {
    Boolean success;
    String message;

    public SignUpRespond(){}

    public SignUpRespond(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
