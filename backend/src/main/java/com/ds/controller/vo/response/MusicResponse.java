package com.ds.controller.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MusicResponse {

    @Setter
    @Getter
    @JsonProperty("id")
    private Long id;

    @Setter
    @Getter
    @JsonProperty("title")
    private String title;

    @Setter
    @Getter
    @JsonProperty("name")
    private String name;

    @Setter
    @Getter
    @JsonProperty("artist")
    private String artist;

    @Setter
    @Getter
    @JsonProperty("fileUrl")
    private String fileUrl;
}
