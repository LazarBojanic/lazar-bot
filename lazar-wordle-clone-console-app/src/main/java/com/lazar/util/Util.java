package com.lazar.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazar.model.*;
import org.fusesource.jansi.Ansi;

public class Util {
    public static final String RED = "R";
    public static final String YELLOW = "Y";
    public static final String GREEN = "G";
    public static final String WHITE = "W";
    public static StringBuilder stringBuilder;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static void printMessage(String message){
        System.out.println(message);
        System.out.print("> ");
    }
    public static String objectToJsonString(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
    public static <T> T jsonStringToObject(String json, Class<T> className) throws JsonProcessingException {
        return objectMapper.readValue(json, className);
    }
    public static String getValidatedWordString(Word validatedWord){
        stringBuilder = new StringBuilder();
        for (Letter letter : validatedWord.getLetters()) {
            stringBuilder.append(getColoredLetter(letter));
        }
        return stringBuilder.toString();
    }
    public static String getUserBoardString(UserBoard userBoard){
        String userBoardString = "";
        for (int i = 0; i < userBoard.getUserTryList().size(); i++) {
            UserTry userTry = userBoard.getUserTryList().get(i);
            userBoardString += getValidatedWordString(userTry.getValidatedWord());
            if(i < userBoard.getUserTryList().size() - 1){
                userBoardString += "\n";
            }
        }
        return userBoardString;
    }
    public static String getUserKeyboardString(UserKeyboard userKeyboard){
        stringBuilder = new StringBuilder();
        for(int i = 0; i < userKeyboard.getLetterStatusList().size(); i++){
            LetterStatus letterStatus = userKeyboard.getLetterStatusList().get(i);
            stringBuilder.append(getColoredLetter(letterStatus));
            if((i + 1) % 8 == 0){
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }
    public static String getColoredLetter(Letter letter){
        String coloredLetter = "";
        if (letter.getStatus().equalsIgnoreCase(Util.RED)) {
            coloredLetter = String.valueOf(Ansi.ansi().bgRed().a(letter.getLetter()).reset());
        }
        else if (letter.getStatus().equalsIgnoreCase(Util.YELLOW)) {
            coloredLetter = String.valueOf(Ansi.ansi().bgYellow().a(letter.getLetter()).reset());
        }
        else if (letter.getStatus().equalsIgnoreCase(Util.GREEN)) {
            coloredLetter = String.valueOf(Ansi.ansi().bgGreen().a(letter.getLetter()).reset());
        }
        else if (letter.getStatus().equalsIgnoreCase(Util.WHITE)) {
            coloredLetter = String.valueOf(Ansi.ansi().bgRgb(64, 64, 64).a(letter.getLetter()).reset());
        }
        else{
            coloredLetter = String.valueOf(Ansi.ansi().reset());
        }
        return coloredLetter;
    }
    public static String getColoredLetter(LetterStatus letterStatus) {
        String coloredLetter = "";
        if (letterStatus.getStatus().equalsIgnoreCase(Util.RED)) {
            coloredLetter = String.valueOf(Ansi.ansi().bgRed().a(letterStatus.getLetter()).reset());
        }
        else if (letterStatus.getStatus().equalsIgnoreCase(Util.YELLOW)) {
            coloredLetter = String.valueOf(Ansi.ansi().bgYellow().a(letterStatus.getLetter()).reset());
        }
        else if (letterStatus.getStatus().equalsIgnoreCase(Util.GREEN)) {
            coloredLetter = String.valueOf(Ansi.ansi().bgGreen().a(letterStatus.getLetter()).reset());
        }
        else if (letterStatus.getStatus().equalsIgnoreCase(Util.WHITE)) {
            coloredLetter = String.valueOf(Ansi.ansi().bgRgb(64, 64, 64).a(letterStatus.getLetter()).reset());
        }
        else{
            coloredLetter = String.valueOf(Ansi.ansi().reset());
        }
        return coloredLetter;
    }
    public static String getSimpleDictionaryWordString(SimpleDictionaryWord simpleDictionaryWord){
        stringBuilder = new StringBuilder(String.format("Word: %s\n", simpleDictionaryWord.getWord()));
        for (SimpleMeaning simpleMeaning : simpleDictionaryWord.getMeanings()){
            stringBuilder.append(String.format("\tPart of Speech: %s\n", simpleMeaning.getPartOfSpeech()));
            for(String definition : simpleMeaning.getDefinitions()){
                stringBuilder.append(String.format("\t\tDefinition: %s\n", definition));
            }
        }
        return stringBuilder.toString();
    }
    public static void clearConsole() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls")
                        .inheritIO()
                        .start()
                        .waitFor();
            }
            else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        }
        catch (Exception e) {
            for (int i = 0; i < 100; i++) {
                System.out.println();
            }
        }
    }
    public static String getWordleLogo(){
        return """
                 .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.\s
                | .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |
                | | _____  _____ | || |     ____     | || |  _______     | || |  ________    | || |   _____      | || |  _________   | |
                | ||_   _||_   _|| || |   .'    `.   | || | |_   __ \\    | || | |_   ___ `.  | || |  |_   _|     | || | |_   ___  |  | |
                | |  | | /\\ | |  | || |  /  .--.  \\  | || |   | |__) |   | || |   | |   `. \\ | || |    | |       | || |   | |_  \\_|  | |
                | |  | |/  \\| |  | || |  | |    | |  | || |   |  __ /    | || |   | |    | | | || |    | |   _   | || |   |  _|  _   | |
                | |  |   /\\   |  | || |  \\  `--'  /  | || |  _| |  \\ \\_  | || |  _| |___.' / | || |   _| |__/ |  | || |  _| |___/ |  | |
                | |  |__/  \\__|  | || |   `.____.'   | || | |____| |___| | || | |________.'  | || |  |________|  | || | |_________|  | |
                | |              | || |              | || |              | || |              | || |              | || |              | |
                | '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |
                 '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'\s
                """;
    }
}
