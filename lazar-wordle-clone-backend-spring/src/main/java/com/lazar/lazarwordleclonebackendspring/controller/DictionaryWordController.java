package com.lazar.lazarwordleclonebackendspring.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
@RequestMapping("/dictionaryWord")
public class DictionaryWordController {
	@Autowired
	private DictionaryWordService dictionaryWordService;
    @GetMapping("/getByWord")
	public ResponseEntity<DictionaryWord> getDictionaryWordByWord(@RequestParam String word) {
		return ResponseEntity.ok(dictionaryWordService.getDictionaryWordByWord(word));
	}
    @GetMapping("/getSimpleByWord")
	public ResponseEntity<SimpleDictionaryWord> getSimpleDictionaryWordByWord(@RequestParam String word) {
		return ResponseEntity.ok(dictionaryWordService.getSimpleDictionaryWordByWord(word));
	}
    @GetMapping("/getImageByWord")
	public ResponseEntity<Resource> getDictionaryWordImageByWord(@RequestParam String word) {
        Resource dictionaryWordImage = dictionaryWordService.getDictionaryWordImageByWord(word);
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
}
