package com.ds.controller.vo.response;

import com.ds.enums.SystemMsgCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Schema(description = "回應物件!客製化API回傳BODY")
@Data
@SuperBuilder
public class ApiBaseRespBody {

    @Schema(description = "回應欄位!代碼")
    private SystemMsgCode code;

    @Schema(description = "回應欄位!訊息")
    private String message;

    @Schema(description = "回應欄位!資料")
    private Object data;

    public static ApiBaseRespBody custom(SystemMsgCode code, String message, Object data) {
        return ApiBaseRespBody.builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }
}
