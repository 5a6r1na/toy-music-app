package com.ds.controller.vo.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "請求物件!")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class MusicRequest {
    private String songId;
    private String name;
    private String filepath;
}
