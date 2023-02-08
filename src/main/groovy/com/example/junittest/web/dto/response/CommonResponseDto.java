package com.example.junittest.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonResponseDto<T> {
    private int code;
    private String msg;
    private T body;

    @Builder
    public CommonResponseDto(int code, String msg, T body) {
        this.code = code;
        this.msg = msg;
        this.body = body;
    }
}
