package finki.ukim.mk.map_application.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import finki.ukim.mk.map_application.database.converters.RoleNameTypeConverter;
import finki.ukim.mk.map_application.database.converters.VisibilityTypeConverter;

import java.io.Serializable;

@Entity
@TypeConverters(RoleNameTypeConverter.class)
public class Role implements Serializable {

    @PrimaryKey
    private Long id;

    private RoleName name;

    public Role(){}

    public Role(Long id,RoleName name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public RoleName getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(RoleName name) {
        this.name = name;
    }
}