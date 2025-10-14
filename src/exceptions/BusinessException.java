package exceptions;

public class BusinessException extends Exception {
    // Constructor with message parameter for business rule violations
    public BusinessException(String message) {
        super(message); // Call parent Exception class constructor
    }

    // Constructor with message and cause for chained exceptions
    public BusinessException(String message, Throwable cause) {
        super(message, cause); // Call parent Exception class constructor with cause
    }
}