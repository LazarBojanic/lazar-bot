import React, { useState, useEffect } from "react";
import "./App.css";
import SubmitButton from "./SubmitButton";
import Keyboard from "./Keyboard";
import Board from "./Board";

const ip = import.meta.env.VITE_SERVER_IP;

function App() {
  const [userTries, setUserTries] = useState([]);
  const [currentWord, setCurrentWord] = useState("");
  const [gameOver, setGameOver] = useState(false);
  const [gameMessage, setGameMessage] = useState("");
  const [remainingTries, setRemainingTries] = useState(6);
  const [canProceedToNextTry, setCanProceedToNextTry] = useState(true); // New state
  const [letterStatuses, setLetterStatuses] = useState(Array(26).fill("WHITE")); // Initialize with default status for all letters

  useEffect(() => {
    console.log(remainingTries);
    if (remainingTries === 0) {
      setGameOver(true);
      setCanProceedToNextTry(false);
      // Fetch the actual solution word from the API
      fetch(`${ip}/api/game/checkGameStatus?username=dumbe01`)
        .then((response) => response.json())
        .then((data) => {
          setGameMessage(`Game Over! The solution word was: ${data.word}`);
        });
    }
  }, [remainingTries]);

  const handleLetterClick = (letter) => {
    console.log(canProceedToNextTry);
    if (!canProceedToNextTry && gameOver) {
      return;
    }
    if (currentWord.length < 5) {
      setCurrentWord(currentWord + letter);
    }
  };

  const handleBackspaceClick = () => {
    if (!canProceedToNextTry && gameOver) {
      return;
    }
    setCurrentWord(currentWord.slice(0, -1));
  };

  const handleNewWord = async () => {
    console.log("1: " + remainingTries);
    // Reset the board and clear previous tries
    setLetterStatuses([]);
    setUserTries([]);
    setCurrentWord("");
    setGameMessage("");
    setGameOver(false);
    setRemainingTries(6);
    console.log("2: " + remainingTries);
    setCanProceedToNextTry(true); // Reset canProceedToNextTry

    // Fetch a new word from the API
    const response = await fetch(`${ip}/api/solutions/new`);
    const data = await response.json();
    console.log("New Word:", data.word);
  };

  const handleSubmit = async () => {
    setGameMessage("");
    if (gameOver) return;
    console.log("Current Word:", currentWord);
    const currentUserTry = {
      word: currentWord,
    };
    const apiResponse = await fetch(`${ip}/api/solutions/check`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(currentUserTry),
    });
    const userTry = await apiResponse.json();
    console.log("User try: " + JSON.stringify(userTry));
    if (userTry.reason === "solution_not_valid") {
      setCanProceedToNextTry(false);
      setGameMessage("Solution not valid!");
      console.log(remainingTries);
      return;
    } else if (userTry.reason === "solution_correct") {
      setGameMessage("You Win!");
      setGameOver(true);
      setCanProceedToNextTry(false);
    } else if (userTry.reason === "solution_incorrect") {
      setRemainingTries(remainingTries - 1);
      setGameMessage("");
      setCanProceedToNextTry(true);
    }
    setUserTries([...userTries, userTry]);
    const newLetterStatuses = [...letterStatuses];
    userTry.validated_word.letters.forEach((letterObj) => {
      const letterIndex = letterObj.letter.charCodeAt(0) - "A".charCodeAt(0);
      newLetterStatuses[letterIndex] = letterObj.status;
    });
    setLetterStatuses(newLetterStatuses);
    setCurrentWord("");
    //console.log(remainingTries)
  };

  return (
    <div className="App">
      <h2>{gameMessage}</h2>
      <Board userTries={userTries} currentWord={currentWord} />
      <br />
      <Keyboard
        onLetterClick={handleLetterClick}
        onBackspaceClick={handleBackspaceClick}
        letterStatuses={letterStatuses} // Pass the letterStatuses to the Keyboard component
      />
      <br />
      <SubmitButton onClick={handleSubmit} />
      <button onClick={handleNewWord}>New Word</button>
    </div>
  );
}

export default App;
