package mk.finki.ukim.mk.map_application.model.Exception.Auth;

public class UserEmailAlreadyExists extends RuntimeException {
    public UserEmailAlreadyExists(String errorMessage){
        super(errorMessage);
    }
}
