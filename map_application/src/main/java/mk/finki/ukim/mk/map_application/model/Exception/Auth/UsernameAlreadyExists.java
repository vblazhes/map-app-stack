package mk.finki.ukim.mk.map_application.model.Exception.Auth;

public class UsernameAlreadyExists extends RuntimeException {
    public UsernameAlreadyExists(String errorMessage){
        super(errorMessage);
    }
}
