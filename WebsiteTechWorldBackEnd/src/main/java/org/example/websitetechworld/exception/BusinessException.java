package org.example.websitetechworld.exception;

public class BusinessException extends RuntimeException {

    private final String messageCode;
    private final Object[] args;

    public BusinessException(String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = new Object[0];
    }

    public BusinessException(String messageCode, Object... args) {
        super(messageCode); // dùng messageCode làm message tạm
        this.messageCode = messageCode;
        this.args = args;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public Object[] getArgs() {
        return args;
    }
}

