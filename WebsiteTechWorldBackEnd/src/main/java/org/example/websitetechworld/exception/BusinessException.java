package org.example.websitetechworld.exception;

public class BusinessException extends RuntimeException{

    private final String messageCode;

    public BusinessException(String messageCode) {
        super(messageCode); // tạm thời dùng code làm message
        this.messageCode = messageCode;
    }

    public BusinessException(String messageCode, Throwable cause) {
        super(messageCode, cause);
        this.messageCode = messageCode;
    }

    public String getMessageCode() {
        return messageCode;
    }

}
