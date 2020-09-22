package mk.finki.ukim.mk.map_application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Visibility {

    PUBLIC("public"),
    PRIVATE("private");

    private String map_visibility;
}
