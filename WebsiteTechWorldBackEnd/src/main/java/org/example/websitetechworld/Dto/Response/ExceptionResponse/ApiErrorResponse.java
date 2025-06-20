package org.example.websitetechworld.Dto.Response.ExceptionResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
    private int statusCode;
    private String error;
    private Object message;
    private String path;
}
