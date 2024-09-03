package by.petrovich.reminder.exception;

import java.time.DateTimeException;

public class InvalidDataTimeSearchCriteriaException extends RuntimeException{
    public InvalidDataTimeSearchCriteriaException(String data, DateTimeException e){
        super(data, e);
    }

}
