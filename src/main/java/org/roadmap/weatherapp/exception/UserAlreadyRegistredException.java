package org.roadmap.weatherapp.exception;

public class UserAlreadyRegistredException extends Exception {

    @Override
    public String getMessage() {
        return "This email is already registered";
    }
}
