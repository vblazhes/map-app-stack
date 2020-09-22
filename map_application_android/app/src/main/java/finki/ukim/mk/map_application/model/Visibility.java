package finki.ukim.mk.map_application.model;

import java.io.Serializable;

public enum Visibility implements Serializable {
    PUBLIC("public"),
    PRIVATE("private");

    Visibility(String map_visibility) {
        this.map_visibility = map_visibility;
    }

    private String map_visibility;
}
