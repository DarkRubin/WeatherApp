package org.roadmap.weatherapp.exception;

public class UserAlreadyRegisteredException extends RuntimeException {

    @Override
    public String getMessage() {
        return "This email is already registered";
    }
}
