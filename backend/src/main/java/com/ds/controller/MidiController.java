package com.ds.controller;

import com.ds.config.MidiStorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequiredArgsConstructor
public class MidiController {

    private final MidiStorageProperties midiStorageProperties;

    @GetMapping("/midi/{filename}")
    public ResponseEntity<Resource> getMidiFile(@PathVariable String filename) {

        File file = new File(midiStorageProperties.getPath(), filename);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=" + filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
