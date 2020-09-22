package finki.ukim.mk.map_application.database.converters;

import androidx.room.TypeConverter;
import finki.ukim.mk.map_application.model.RoleName;
import finki.ukim.mk.map_application.model.Visibility;

public class RoleNameTypeConverter {
    @TypeConverter
    public RoleName fromString(String roleNameStr){
        return roleNameStr == null ? null : RoleName.valueOf(roleNameStr.toUpperCase());
    }

    @TypeConverter
    public String fromRoleName(RoleName roleNameObj){
        return roleNameObj == null ? null : roleNameObj.toString();
    }
}
