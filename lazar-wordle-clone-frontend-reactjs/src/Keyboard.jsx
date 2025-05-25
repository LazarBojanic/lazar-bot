// Keyboard.js
import React from 'react';
import Letter from './Letter';
import './Keyboard.css'; // Add a custom CSS file for keyboard styling

function Keyboard({ onLetterClick, onBackspaceClick, letterStatuses }) {
  const alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

  return (
    <div className="keyboard-container">
      <div className="keyboard">
        <button onClick={onBackspaceClick}>
          Backspace
        </button>
        {alphabet.split('').map((letter, index) => (
          <Letter
            key={letter}
            letter={letter}
            status={letterStatuses[index]} // Pass the status to the Letter component
            onClick={() => onLetterClick(letter)}
            isOnKeyboard={true}
          />
        ))}
      </div>
    </div>
  );
}

export default Keyboard;
