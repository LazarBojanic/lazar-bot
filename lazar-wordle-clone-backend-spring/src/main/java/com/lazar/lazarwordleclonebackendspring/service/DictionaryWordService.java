package com.lazar.lazarwordleclonebackendspring.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Optional;

import com.lazar.lazarwordleclonebackendspring.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
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

    public DictionaryWord getByWord(String word) {
        Optional<DictionaryWord> dictionaryWordOptional = dictionaryWordRepository.findByWord(word.toLowerCase());
        if(dictionaryWordOptional.isPresent()){
            return dictionaryWordOptional.get();
        }
        Util.logger.error("Dictionary Word not found: {}", word);
        return new DictionaryWord();
    }

    public SimpleDictionaryWord getSimpleByWord(String word) {
        Optional<DictionaryWord> dictionaryWordOptional = dictionaryWordRepository.findByWord(word.toLowerCase());
        if(dictionaryWordOptional.isPresent()){
            return new SimpleDictionaryWord(dictionaryWordOptional.get());
        }
        Util.logger.error("Simple Dictionary Word not found: {}", word);
        return new SimpleDictionaryWord();
    }

    public Resource getImageByWord(String word) {
        SimpleDictionaryWord simpleDictionaryWord = getSimpleByWord(word);
        List<SimpleMeaning> meanings = simpleDictionaryWord.getMeanings();
        int imageWidth = 800;
        int imageHeight = 200 + meanings.size() * 50;
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, imageWidth, imageHeight);
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        g2d.setColor(Color.BLACK);
        g2d.drawString("Word: " + word, 10, 30);
        int y = 60;
        for (SimpleMeaning meaning : meanings) {
            g2d.drawString("Part of Speech: " + meaning.getPartOfSpeech(), 20, y);
            y += 20;
            for (String definition : meaning.getDefinitions()) {
                g2d.drawString("Definition: " + definition, 40, y);
                y += 20;
            }
            y += 20;
        }
        g2d.dispose();
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            return new ByteArrayResource(imageBytes);
        }
        catch (IOException e) {
            Util.logger.error("Error creating image for word: {}", word, e);
            return new ByteArrayResource(new byte[0]);
        }
    }

    public List<SimpleDictionaryWord> getAllSimple() {
        List<DictionaryWord> dictionaryWordList = dictionaryWordRepository.findAll();
        List<SimpleDictionaryWord> simpleDictionaryWordList = new ArrayList<>();
        for (DictionaryWord dictionaryWord : dictionaryWordList) {
            simpleDictionaryWordList.add(new SimpleDictionaryWord(dictionaryWord));
        }
        return simpleDictionaryWordList;
    }

    public SimpleDictionaryWord getLongestSimple() {
        List<SimpleDictionaryWord> simpleDictionaryWordList = getAllSimple();
        int max = 0;
        SimpleDictionaryWord simpleDictionaryWordWithMaxLength = new SimpleDictionaryWord();
        for (SimpleDictionaryWord simpleDictionaryWord : simpleDictionaryWordList) {
            int currentLength = 0;
            currentLength += simpleDictionaryWord.getWord().length();
            for (SimpleMeaning simpleMeaning : simpleDictionaryWord.getMeanings()) {
                currentLength += simpleMeaning.getPartOfSpeech().length();
                for (String definition : simpleMeaning.getDefinitions()) {
                    currentLength += definition.length();
                }
            }
            if (currentLength >= max) {
                max = currentLength;
                simpleDictionaryWordWithMaxLength = simpleDictionaryWord;
            }
        }
        return simpleDictionaryWordWithMaxLength;
    }
}
