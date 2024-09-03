package by.petrovich.reminder.constant;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class Constant {
    public static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final Pattern ID_REMINDER_PATTERN = Pattern.compile("/api/v1/reminders/(\\d+)");

}
