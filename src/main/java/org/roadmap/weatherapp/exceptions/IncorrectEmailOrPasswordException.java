package org.roadmap.weatherapp.exceptions;

public class IncorrectEmailOrPasswordException extends Exception {

    @Override
    public String getMessage() {
        return "Incorrect email or password";
    }
}
