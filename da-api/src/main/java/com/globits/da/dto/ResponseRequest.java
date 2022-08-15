package com.globits.da.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseRequest<T> {
    private  int statusCode;
    private String messageError;
    private T data;

}
