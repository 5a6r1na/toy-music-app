package com.ds.service;

import com.ds.controller.vo.request.MusicRequest;
import com.ds.controller.vo.response.ApiBaseResponse;
import com.ds.controller.vo.response.MusicResponse;
import com.ds.dao.MusicDao;
import com.ds.dao.entity.ComponentEntity;
import com.ds.dao.entity.SongEntity;
import com.ds.enums.SystemMsgCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MusicService {

    @Autowired
    private MusicDao musicDao;

    /**
     * [FUNCTION]: queryMusic() retrieves ACTIVE cases from the database
     *
     * @return ApiBaseResponse
     * */
    public ApiBaseResponse<List<MusicResponse>> queryMusic() {
        try {
            // [STEP]: Fetch data from the database
            List<SongEntity> musicEntities = musicDao.getMusic();
                List<MusicResponse> songList = musicEntities.stream()
                .map(entity -> MusicResponse.builder()
                        .id(entity.getId())
                        .title(entity.getTitle())
                        .artist(entity.getArtist())
                        .fileUrl("/midi/" + entity.getFileName())
                        .build())
                    .collect(Collectors.toList());

            // [CASE]: Success Response
            return new ApiBaseResponse<>(
                    HttpStatus.valueOf(200),
                    SystemMsgCode.SUCCESS,
                    "查詢成功",
                    songList);
        } catch (Exception e) {
            e.printStackTrace();
            // [CASE]: Error Response
            return new ApiBaseResponse<>(
                    HttpStatus.valueOf(200),
                    SystemMsgCode.E01010,
                    "查詢失敗",
                    null);
        }
    }


    /**
     * [FUNCTION]: getSongComponent() retrieves song components by song ID
     *
     * @param reqBody CaseRequest containing the song ID.
     * @return ApiBaseResponse
     * */
    public ApiBaseResponse<List<MusicResponse>> getSongComponent(MusicRequest reqBody) {
        try {
            // [STEP]: Convert the caseId from the request body into a Long
            Long id = Long.valueOf(reqBody.getSongId());

            // [STEP]: Fetch data from the database by caseId
            List<ComponentEntity> componentEntities = musicDao.getComponentsBySongId(id);

            // [CASE]: If case profile NOT found, return NULL response
            if (componentEntities == null) {
                return new ApiBaseResponse<>(
                        HttpStatus.valueOf(200),
                        SystemMsgCode.E01010,
                        "案件未找到",
                        null);
            }

            // [STEP]: Convert CaseEntity to CaseResponse.
            List<MusicResponse> caseHistoryList = componentEntities.stream()
                    .map(entity -> MusicResponse.builder()
                            .id(entity.getId())
                            .name(entity.getName())
                            .fileUrl("/midi/" + entity.getFileName())
                            .build()
                    )
                    .collect(Collectors.toList());

            // [CASE]: Success Response
            return new ApiBaseResponse<>(
                    HttpStatus.valueOf(200),
                    SystemMsgCode.SUCCESS,
                    "查詢成功",
                    caseHistoryList);

        } catch (Exception e) {
            e.printStackTrace();
            // [CASE]: Error Response
            return new ApiBaseResponse<>(
                    HttpStatus.valueOf(500),
                    SystemMsgCode.E01010,
                    "查詢失敗",
                    null);
        }
    }
}
