package org.example.websitetechworld.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.example.websitetechworld.Dto.Response.ExceptionResponse.ApiErrorResponse;
import org.example.websitetechworld.Dto.Response.ExceptionResponse.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Database Constraint Violation",
                ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAllException(Exception e, WebRequest request) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                e.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }


//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
//        ApiErrorResponse error = new ApiErrorResponse(
//                HttpStatus.NOT_FOUND.value(),
//                "Not Found",
//                ex.getMessage(),
//                request.getDescription(false).replace("uri=", "")
//        );
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Lỗi Xác Thực",
                errors,
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }



    // cuong

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleValidationException(
//            MethodArgumentNotValidException ex, HttpServletRequest request) {
//
//        Map<String, String> fieldErrors = new HashMap<>();
//        ex.getBindingResult().getFieldErrors().forEach(error ->
//                fieldErrors.put(error.getField(), error.getDefaultMessage()));
//
//        ErrorResponse error = new ErrorResponse();
//        error.setStatus(HttpStatus.BAD_REQUEST.value());
//        error.setMessage("Dữ liệu không hợp lệ");
//        error.setErrors(fieldErrors);
//        error.setPath(request.getRequestURI());
//        error.setTimestamp(LocalDateTime.now());
//
//        return ResponseEntity.badRequest().body(error);
//    }
//
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest request) {
        ApiErrorResponse error = new ApiErrorResponse();
        error.setStatusCode(400);
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
//        error.setTimestamp(LocalDateTime.now());

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        ApiErrorResponse error = new ApiErrorResponse();
        error.setStatusCode(404);
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
//        error.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleServerError(Exception ex, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse();
//        error.setStatus(500);
//        error.setMessage("Lỗi hệ thống, vui lòng thử lại sau");
//        error.setPath(request.getRequestURI());
//        error.setTimestamp(LocalDateTime.now());
//
//        // Ghi log lỗi thực tế
//        ex.printStackTrace();
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//    }

}