package mk.finki.ukim.mk.map_application.payload.Response;

import lombok.Data;
import mk.finki.ukim.mk.map_application.model.Auth.User;

@Data
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private User user;

    public JwtAuthenticationResponse(String accessToken, User user) {
        this.accessToken = accessToken;
        this.user = user;
    }
}