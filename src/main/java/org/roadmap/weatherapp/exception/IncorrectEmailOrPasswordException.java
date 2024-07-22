package org.roadmap.weatherapp.exception;

public class IncorrectEmailOrPasswordException extends Exception {

    @Override
    public String getMessage() {
        return "Incorrect email or password";
    }
}
