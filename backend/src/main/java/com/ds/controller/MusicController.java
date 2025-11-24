package com.ds.controller;

import com.ds.controller.vo.request.MusicRequest;
import com.ds.controller.vo.response.ApiBaseResponse;
import com.ds.controller.vo.response.MusicResponse;
import com.ds.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "/api/music", produces = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE
})
//@CrossOrigin(origins = "http://localhost:5173")
//@CrossOrigin(origins = "http://localhost:30303")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @PostMapping("/queryMusic")
    public ApiBaseResponse<List<MusicResponse>> queryMusic() {
        return musicService.queryMusic();
    }

    @PostMapping("/queryComponent")
    public ApiBaseResponse<List<MusicResponse>> getCaseBackground(
            @RequestBody MusicRequest request) {
        return musicService.getSongComponent(request);
    }
}
