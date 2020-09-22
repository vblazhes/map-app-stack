package finki.ukim.mk.map_application.database.converters;

import androidx.room.TypeConverter;
import finki.ukim.mk.map_application.model.Visibility;

public class VisibilityTypeConverter {
    @TypeConverter
    public Visibility fromString(String visibilityStr){
        return visibilityStr == null ? null : Visibility.valueOf(visibilityStr.toUpperCase());
    }

    @TypeConverter
    public String fromVisibility(Visibility visibilityObj){
        return visibilityObj == null ? null : visibilityObj.toString();
    }
}
