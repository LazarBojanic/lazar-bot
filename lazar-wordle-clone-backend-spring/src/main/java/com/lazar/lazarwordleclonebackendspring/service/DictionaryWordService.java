package com.lazar.lazarwordleclonebackendspring.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.lazar.lazarwordleclonebackendspring.model.DictionaryWord;
import com.lazar.lazarwordleclonebackendspring.model.SimpleDictionaryWord;
import com.lazar.lazarwordleclonebackendspring.model.SimpleMeaning;
import com.lazar.lazarwordleclonebackendspring.repository.DictionaryWordRepository;

@Service
public class DictionaryWordService {
    @Autowired
    private DictionaryWordRepository dictionaryWordRepository;

    public DictionaryWord getDictionaryWordByWord(String word){
        return dictionaryWordRepository.findByWord(word.toLowerCase()).get();
    }
    public SimpleDictionaryWord getSimpleDictionaryWordByWord(String word){
        return new SimpleDictionaryWord(dictionaryWordRepository.findByWord(word.toLowerCase()).get());
    }
    public Resource getDictionaryWordImageByWord(String word) {
        SimpleDictionaryWord simpleDictionaryWord = getSimpleDictionaryWordByWord(word);
        List<SimpleMeaning> meanings = simpleDictionaryWord.getMeanings();
        int imageWidth = 800;
        int imageHeight = 200 + meanings.size() * 50; // Adjust height based on the number of meanings
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, imageWidth, imageHeight);
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        g2d.setColor(Color.BLACK);
        g2d.drawString("Word: " + word, 10, 30);

        // Draw meanings
        int y = 60; // Initial Y position for meanings
        for (SimpleMeaning meaning : meanings) {
            g2d.drawString("Part of Speech: " + meaning.getPart_of_speech(), 20, y);
            y += 20; // Increase Y position for definitions
            for (String definition : meaning.getDefinitions()) {
                g2d.drawString("Definition: " + definition, 40, y);
                y += 20; // Adjust spacing between definitions
            }
            y += 20; // Adjust spacing between meanings
        }

        g2d.dispose();

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            return new ByteArrayResource(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return new ByteArrayResource(new byte[0]);
        }
    }
    
}
