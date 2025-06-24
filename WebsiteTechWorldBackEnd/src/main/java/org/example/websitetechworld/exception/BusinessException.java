package org.example.websitetechworld.exception;

public class BusinessException extends RuntimeException{

    // xử lý lỗi ngoại lệ
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

}
