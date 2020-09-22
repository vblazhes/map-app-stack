package finki.ukim.mk.map_application.model;

import finki.ukim.mk.map_application.model.auth.AuthenticatedUser;

import java.util.List;

public class MapInsertAsyncTaskDTO {
    Map map;
    List<Pin> pins;
    AuthenticatedUser authenticatedUser;

    public MapInsertAsyncTaskDTO(){

    }

    public MapInsertAsyncTaskDTO(Map map, List<Pin> pins, AuthenticatedUser authenticatedUser) {
        this.map = map;
        this.pins = pins;
        this.authenticatedUser = authenticatedUser;
    }

    public Map getMap() {
        return map;
    }

    public List<Pin> getPins() {
        return pins;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void setPins(List<Pin> pins) {
        this.pins = pins;
    }

    public AuthenticatedUser getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }
}
