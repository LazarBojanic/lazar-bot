package com.lazar.lazarwordleclonebackendspring.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

import com.lazar.lazarwordleclonebackendspring.model.Letter;
import com.lazar.lazarwordleclonebackendspring.model.LetterStatus;
import com.lazar.lazarwordleclonebackendspring.model.UserTry;
import com.lazar.lazarwordleclonebackendspring.model.Word;
import com.lazar.lazarwordleclonebackendspring.repository.UserTryRepository;
import com.util.Util;

@Service
public class UserTryService {
    @Autowired
    private UserTryRepository userTryRepository;

    public UserTry addTryForUser(String username, Word validated_word){
        UserTry userTry = new UserTry(username, validated_word);
        return userTryRepository.insert(userTry);
    }
    public List<UserTry> getUserTries(String username){
        return userTryRepository.findAllByUsername(username).get();
    }
    public void deleteTriesForUser(String username){
        userTryRepository.deleteByUsername(username);
    }
    public Resource getKeyboardForUser(String username) {
        List<UserTry> userTries = getUserTries(username);
        int width = 420;
        int height = 220;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        int keySize = 40;
        int keySpacing = 10;
        Map<String, Color> colors = Map.of(
            "R", new Color(209, 31, 52),
            "Y", new Color(232, 221, 16),
            "G", new Color(48, 219, 18),
            "W", new Color(255, 255, 255)
        );
        int fontSize = 25;
        Font font = new Font("SansSerif", Font.PLAIN, fontSize);
        g2d.setFont(font);
        int keysPerRow = width / (keySize + keySpacing);
        int keysInCurrentRow = 0;
        int x = 10;
        int y = 10;
        List<LetterStatus> letterStatuses = getLetterStatuses(username, userTries);
        for (LetterStatus letterStatus : letterStatuses) {
            Color color = colors.getOrDefault(letterStatus.getStatus(), Color.WHITE);
            g2d.setColor(color);
            g2d.fillRect(x, y, keySize, keySize);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, keySize, keySize);
            FontMetrics metrics = g2d.getFontMetrics();
            int textWidth = metrics.stringWidth(letterStatus.getLetter());
            int textHeight = metrics.getHeight();
            int textX = x + (keySize - textWidth) / 2;
            int textY = y + (keySize - textHeight) / 2 + metrics.getAscent();
            g2d.drawString(letterStatus.getLetter(), textX, textY);
            x += keySize + keySpacing;
            keysInCurrentRow++;
            if (keysInCurrentRow >= keysPerRow) {
                y += keySize + keySpacing;
                x = 10; 
                keysInCurrentRow = 0;
            }
        }
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            ByteArrayResource keyboardImageByteArrayResource = new ByteArrayResource(imageBytes);
            return keyboardImageByteArrayResource;
        } catch (IOException e) {
            e.printStackTrace();
            return new ByteArrayResource(new byte[0]);
        } finally {
            g2d.dispose();
        }
    }
    public List<LetterStatus> getLetterStatuses(String username, List<UserTry> userTries){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        List<LetterStatus> letterStatuses = new ArrayList<>();
        for(int i = 0; i < alphabet.length(); i++){
            String alphabetLetter = String.valueOf(alphabet.charAt(i));
            letterStatuses.add( new LetterStatus(alphabetLetter, Util.WHITE));
        }
        for(UserTry userTry : userTries){
            for(Letter letter : userTry.getValidated_word().getLetters()){
                Integer letterIndex = getLetterIndex(letterStatuses, letter.getLetter());
                if(letter.getStatus().equalsIgnoreCase(Util.WHITE)){
                    if(!letterStatuses.get(letterIndex).getStatus().equalsIgnoreCase(Util.WHITE)){
                        letterStatuses.set(letterIndex, new LetterStatus(letter.getLetter(), letter.getStatus()));
                    }
                }
                else if(letter.getStatus().equalsIgnoreCase(Util.RED)){
                    if(letterStatuses.get(letterIndex).getStatus().equalsIgnoreCase(Util.WHITE)){
                        letterStatuses.set(letterIndex, new LetterStatus(letter.getLetter(), letter.getStatus()));
                    }
                }
                else if(letter.getStatus().equalsIgnoreCase(Util.YELLOW)){
                    if(letterStatuses.get(letterIndex).getStatus().equalsIgnoreCase(Util.WHITE)){
                        letterStatuses.set(letterIndex, new LetterStatus(letter.getLetter(), letter.getStatus()));
                    }
                    else if(letterStatuses.get(letterIndex).getStatus().equalsIgnoreCase(Util.RED)){
                        letterStatuses.set(letterIndex, new LetterStatus(letter.getLetter(), letter.getStatus()));
                    }
                }
                else if(letter.getStatus().equalsIgnoreCase(Util.GREEN)){
                    if(letterStatuses.get(letterIndex).getStatus().equalsIgnoreCase(Util.WHITE)){
                        letterStatuses.set(letterIndex, new LetterStatus(letter.getLetter(), letter.getStatus()));
                    }
                    else if(letterStatuses.get(letterIndex).getStatus().equalsIgnoreCase(Util.RED)){
                        letterStatuses.set(letterIndex, new LetterStatus(letter.getLetter(), letter.getStatus()));
                    }
                    else if(letterStatuses.get(letterIndex).getStatus().equalsIgnoreCase(Util.YELLOW)){
                        letterStatuses.set(letterIndex, new LetterStatus(letter.getLetter(), letter.getStatus()));
                    }
                }
            }
        }
        return letterStatuses;
    }
    public Resource getBoardForUser(String username) {
        List<UserTry> userTries = getUserTries(username);
        int keySize = 40;
        int keySpacing = 10;
        int lettersPerRow = 5;
        int numOfTriesDevider = 7;
        int numRows = userTries.size() % numOfTriesDevider;
        int height = (numRows * (keySize + keySpacing)) + keySpacing;
        int width = lettersPerRow * (keySize + keySpacing) + keySpacing;
         Map<String, Color> colors = Map.of(
            "R", new Color(209, 31, 52),
            "Y", new Color(232, 221, 16),
            "G", new Color(48, 219, 18),
            "W", new Color(255, 255, 255)
        );
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        int x = 10;
        int y = 10;
        List<LetterStatus> letterStatuses = getLetterStatusesForBoard(username, userTries);
        
        for (LetterStatus letterStatus : letterStatuses) {
            Color color = colors.getOrDefault(letterStatus.getStatus(), Color.WHITE);
            g2d.setColor(color);
            g2d.fillRect(x, y, keySize, keySize);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, keySize, keySize);
            FontMetrics metrics = g2d.getFontMetrics();
            int textWidth = metrics.stringWidth(letterStatus.getLetter());
            int textHeight = metrics.getHeight();
            int textX = x + (keySize - textWidth) / 2;
            int textY = y + (keySize - textHeight) / 2 + metrics.getAscent();
            g2d.drawString(letterStatus.getLetter(), textX, textY);
            x += keySize + keySpacing;
                if (x >= width) {
                x = 10;
                y += keySize + keySpacing;
            }
        }
    
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            ByteArrayResource boardImageByteArrayResource = new ByteArrayResource(imageBytes);
            return boardImageByteArrayResource;
        } catch (IOException e) {
            e.printStackTrace();
            return new ByteArrayResource(new byte[0]);
        } finally {
            g2d.dispose();
        }
    }
    public List<LetterStatus> getLetterStatusesForBoard(String username, List<UserTry> userTries){
        List<LetterStatus> letterStatuses = new ArrayList<>();
        for(UserTry userTry : userTries){
            for(Letter letter : userTry.getValidated_word().getLetters()){
                letterStatuses.add(new LetterStatus(letter.getLetter(), letter.getStatus()));
            }
        }
        return letterStatuses;
    }
    public Integer getLetterIndex(List<LetterStatus> letterStatuses, String letter){
        for(int i = 0; i < letterStatuses.size(); i++){
            if(letterStatuses.get(i).getLetter().equalsIgnoreCase(letter)){
                return i;
            }
        }
        return -1;
    }
    public List<LetterStatus> getLetterStatusesForUser(String username){
        return getLetterStatuses(username, getUserTries(username));
    }
}
