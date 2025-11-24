package com.ds.dao;

import com.ds.dao.entity.ComponentEntity;
import com.ds.dao.entity.SongEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MusicDao {

    /**
     * [READ]: Query all ACTIVE cases
     */
    @Select("""
        SELECT ID,
               TITLE,
               ARTIST,
               MIDI_FILE_PATH AS FILE_NAME
          FROM SONG_METADATA
         WHERE 1 = 1
         ORDER BY ID
        """)
    List<SongEntity> getMusic();



    /**
     * [READ]: Get components by SONG ID
     */
    @Select("""
        SELECT c.id,
               c.name,
               c.midi_file_path AS file_name
        FROM component_metadata c
        JOIN song_component sc
          ON c.id = sc.component_id
        WHERE sc.song_id = #{songId}
        """)
    List<ComponentEntity> getComponentsBySongId(@Param("songId") Long songId);
}