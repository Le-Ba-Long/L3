package com.globits.da.dto;

import com.globits.da.common.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.globits.da.common.ErrorMessage.SUCCESS;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseData<T> {
    private Integer statusCode;
    private String messageError;
    private T data;

    public ResponseData(T data) {
        this.data = data;
        this.statusCode = SUCCESS.getCode();
        this.messageError = SUCCESS.getMessage();
    }

    public ResponseData(ErrorMessage errorMessage, T data) {
        this.data = data;
        this.statusCode = errorMessage.getCode();
        this.messageError = errorMessage.getMessage();
    }
}
