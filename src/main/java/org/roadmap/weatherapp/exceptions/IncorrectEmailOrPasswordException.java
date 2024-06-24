package org.roadmap.weatherapp.exceptions;

public class IncorrectEmailOrPasswordException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Incorrect email or password";
    }
}
