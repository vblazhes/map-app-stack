package finki.ukim.mk.map_application.model.auth;

public class LoginRequest {
   private String usernameOrEmail;
   private String password;

   public LoginRequest(){

   }

   public LoginRequest(String usernameOrEmail, String password){
       this.usernameOrEmail = usernameOrEmail;
       this.password = password;
   }

   //Getter
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    //Setter

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
