package com.harshul.taskoo.util;

import androidx.room.TypeConverter;

import com.harshul.taskoo.model.Priority;

import java.util.Date;

public class TypeConvertor {

    @TypeConverter
    public static Date timeStampToDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimeStamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String fromPriority(Priority priority) {
        return priority == null ? null : priority.name();
    }

    @TypeConverter
    public static Priority toPriority(String priority) {
        return priority == null ? null : Priority.valueOf(priority);
    }


}
