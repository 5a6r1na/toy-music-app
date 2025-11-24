package com.ds.dao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "song_metadata")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SongEntity {
    @Id
    private Long id;

    private String title;

    private String artist;

    private String fileName;

}


