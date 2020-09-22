package mk.finki.ukim.mk.map_application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {

    CHURCH("Church"),
    HOTEL("Hotel");

    private String category_name;
}
