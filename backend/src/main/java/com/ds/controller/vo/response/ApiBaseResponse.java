package com.ds.controller.vo.response;

import com.ds.enums.SystemMsgCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ApiBaseResponse<T> extends ResponseEntity<ApiBaseRespBody> {

    // [建構式]: 一般
    public ApiBaseResponse(HttpStatus status,
                           SystemMsgCode code, String message, T data) {
        super(ApiBaseRespBody.builder()
                .code(code)
                .message(message)
                .data(data)
                .build(), status);
    }

    // [建構式]: 標頭
    public ApiBaseResponse(MultiValueMap<String, String> headers,
                           HttpStatus status,
                           SystemMsgCode code, String message, T data) {
        super(headers, status);
        ResponseEntity.ok().body(ApiBaseRespBody.builder()
                .code(code)
                .message(message)
                .data(data)
                .build());
    }


    // [Setter]: code、message、data(without HttpHeaders)
    public static <T> ApiBaseResponse result(HttpStatus status,
                                             SystemMsgCode code, String message, T data) {
        return new ApiBaseResponse(status, code, message, data);
    }

    // [Setter]: code、message、data(with HttpHeaders)
    public static <T> ApiBaseResponse result(MultiValueMap<String, String> headers,
                                             HttpStatus status,
                                             SystemMsgCode code, String message, T data) {
        return new ApiBaseResponse(headers, status, code, message, data);
    }

}