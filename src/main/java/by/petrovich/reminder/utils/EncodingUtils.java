package by.petrovich.reminder.utils;

import lombok.experimental.UtilityClass;

import java.util.Base64;

@UtilityClass
public class EncodingUtils {
    public static String encodeBase64(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    public static String decodeBase64(String data) {
        return new String(Base64.getDecoder().decode(data));
    }
}
