package org.roadmap.weatherapp.exceptions;

public class UserAlreadyExistException extends Exception {

    @Override
    public String getMessage() {
        return "This email is already registered";
    }
}
