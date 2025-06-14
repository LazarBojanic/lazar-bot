package com.lazar.lazarwordleclonebackendspring.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lazar.lazarwordleclonebackendspring.model.DictionaryWord;
import com.lazar.lazarwordleclonebackendspring.model.SimpleDictionaryWord;
import com.lazar.lazarwordleclonebackendspring.service.DictionaryWordService;

@RestController
@RequestMapping("/dictionary-words")
public class DictionaryWordController {
	@Autowired
	private DictionaryWordService dictionaryWordService;
    @GetMapping("/get-by-word")
	public ResponseEntity<DictionaryWord> getByWord(@RequestParam String word) {
		return ResponseEntity.ok(dictionaryWordService.getByWord(word));
	}
    @GetMapping("/get-simple-by-word")
	public ResponseEntity<SimpleDictionaryWord> getSimpleByWord(@RequestParam String word) {
		return ResponseEntity.ok(dictionaryWordService.getSimpleByWord(word));
	}
    @GetMapping("/get-image-by-word")
	public ResponseEntity<Resource> getImageByWord(@RequestParam String word) {
        Resource dictionaryWordImage = dictionaryWordService.getImageByWord(word);
        try {
            File tempFile = File.createTempFile(word, ".png");
            FileOutputStream fos = new FileOutputStream(tempFile);
            IOUtils.copy(dictionaryWordImage.getInputStream(), fos);
            fos.close();
            Resource savedResource = new FileSystemResource(tempFile);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(savedResource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
	}
    @GetMapping("/get-longest-simple")
	public ResponseEntity<SimpleDictionaryWord> getLongestSimple() {
		return ResponseEntity.ok(dictionaryWordService.getLongestSimple());
	}
}
