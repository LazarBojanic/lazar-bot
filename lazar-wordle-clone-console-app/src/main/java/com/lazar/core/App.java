package com.lazar.core;

import com.lazar.exception.RequestException;
import com.lazar.exception.InvalidCommandException;
import com.lazar.model.*;
import com.lazar.request.UserTryRequest;
import com.lazar.response.UserTryResponse;
import com.lazar.util.ApiClient;
import com.lazar.util.Util;
import io.github.cdimascio.dotenv.Dotenv;
import org.fusesource.jansi.AnsiConsole;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class App {
    private UserTryRequest userTryRequest;
    private UserTryResponse userTryResponse;
    private BufferedReader reader;
    private String username;
    private String guessWord;
    private boolean running;
    private String command;
    private String[] splitCommand;
    private State currentState;
    private Dotenv dotenv;
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;
    private ApiClient apiClient;

    public App() {
        try {
            AnsiConsole.systemInstall();
            userTryRequest = new UserTryRequest();
            dotenv = Dotenv.load();
            apiClient = new ApiClient(Objects.requireNonNull(dotenv.get("SERVER_IP")));
            command = "";
            splitCommand = new String[]{};
            running = true;
            reader = new BufferedReader(new InputStreamReader(System.in));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //TODO game over vs main menu
    public void run() {
        try {
            Util.clearConsole();
            System.out.println(Util.getWordleLogo());
            Util.printMessage("Welcome to the wordle game. Please use the command newGame <username> to start the game.");
            currentState = State.MAIN_MENU;
            while (running) {
                try {
                    command = reader.readLine().trim();
                    splitCommand = command.split(" ");
                    if (splitCommand.length <= 2) {
                        switch (currentState) {
                            case MAIN_MENU:
                                if (splitCommand[0].equalsIgnoreCase("newGame")) {
                                    username = splitCommand[1];
                                    userTryRequest.setUsername(username);
                                    currentState = State.PLAYING;
                                    httpResponse = apiClient.get(String.format("/game/new?username=%s", userTryRequest.getUsername()));
                                    if (httpResponse.statusCode() == 200) {
                                        Util.printMessage("You have started the game. Please guess a word:");
                                    }
                                    else {
                                        throw new RequestException("Request failed.");
                                    }
                                }
                                else if (splitCommand[0].equalsIgnoreCase("exit")) {
                                    currentState = State.GAME_OVER;
                                    running = false;
                                }
                                else {
                                    throw new InvalidCommandException(splitCommand[0]);
                                }
                                break;
                            case PLAYING:
                                if (splitCommand[0].equalsIgnoreCase("guess")) {
                                    guessWord = splitCommand[1];
                                    userTryRequest.setWord(guessWord);
                                    httpResponse = apiClient.post("/solutions/guess", userTryRequest);
                                    String userTryResponseJson = httpResponse.body().toString();
                                    userTryResponse = Util.jsonStringToObject(userTryResponseJson, UserTryResponse.class);
                                    if (httpResponse.statusCode() == 200) {
                                        if (userTryResponse.getSolutionIsValid()) {
                                            System.out.println("------------------------------");
                                            System.out.println("Your guess: " + Util.getValidatedWordString(userTryResponse.getValidatedWord()) + " = " + userTryResponse.getReason().split("_")[1].toUpperCase());
                                            System.out.println("------------------------------");
                                            httpResponse = apiClient.get(String.format("/game/getBoardForUserRaw?username=%s", userTryRequest.getUsername()));
                                            String userBoardJson = httpResponse.body().toString();
                                            UserBoard userBoard = Util.jsonStringToObject(userBoardJson, UserBoard.class);
                                            String userBoardString = Util.getUserBoardString(userBoard);
                                            System.out.println(userBoardString);
                                            System.out.println("------------------------------");
                                            httpResponse = apiClient.get(String.format("/game/getKeyboardForUserRaw?username=%s", userTryRequest.getUsername()));
                                            String userKeyboardJson = httpResponse.body().toString();
                                            UserKeyboard userKeyboard = Util.jsonStringToObject(userKeyboardJson, UserKeyboard.class);
                                            System.out.println(Util.getUserKeyboardString(userKeyboard));
                                            System.out.println("------------------------------");
                                            httpResponse = apiClient.get(String.format("/game/checkGameStatus?username=%s", userTryRequest.getUsername()));
                                            String userSessionJson = httpResponse.body().toString();
                                            UserSession userSession = Util.jsonStringToObject(userSessionJson, UserSession.class);
                                            System.out.println(String.format("Remaining tries: %s", userSession.getRemainingTries()));
                                            if (userSession.getStatus().equalsIgnoreCase("solved")) {
                                                httpResponse = apiClient.get(String.format("/dictionaryWords/getSimpleByWord?word=%s", userSession.getWord()));
                                                String simpleDictionaryWordJson = httpResponse.body().toString();
                                                SimpleDictionaryWord simpleDictionaryWord = Util.jsonStringToObject(simpleDictionaryWordJson, SimpleDictionaryWord.class);
                                                Util.printMessage(Util.getSimpleDictionaryWordString(simpleDictionaryWord));
                                                Util.printMessage("Congratulations! You have guessed the word correctly. Start a new game with newGame or go back with menu.");
                                                userSession.setStatus("game_over");
                                                currentState = State.GAME_OVER;
                                            }
                                            else{
                                                if(userSession.getRemainingTries() <= 0){
                                                    httpResponse = apiClient.get(String.format("/dictionaryWords/getSimpleByWord?word=%s", userSession.getWord()));
                                                    String simpleDictionaryWordJson = httpResponse.body().toString();
                                                    SimpleDictionaryWord simpleDictionaryWord = Util.jsonStringToObject(simpleDictionaryWordJson, SimpleDictionaryWord.class);
                                                    Util.printMessage(Util.getSimpleDictionaryWordString(simpleDictionaryWord));
                                                    Util.printMessage("Game over. Start a new game with newGame or go back with menu.");
                                                    userSession.setStatus("game_over");
                                                    currentState = State.GAME_OVER;
                                                }
                                                else{
                                                    Util.printMessage("Guess again:");
                                                }
                                            }
                                        }
                                        else{
                                            Util.printMessage("Guess not valid. Try again:");
                                        }
                                    }
                                    else {
                                        throw new RequestException("Request failed.");
                                    }
                                }
                                else if (splitCommand[0].equalsIgnoreCase("menu")) {
                                    currentState = State.MAIN_MENU;
                                }
                                else {
                                    throw new InvalidCommandException(splitCommand[0]);
                                }
                                break;
                            case GAME_OVER:
                                Util.printMessage("Game over. Start a new game with newGame or go back with menu.");
                                currentState = State.MAIN_MENU;
                                break;
                        }
                    }
                    else {
                        throw new InvalidCommandException(splitCommand[0]);
                    }
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

