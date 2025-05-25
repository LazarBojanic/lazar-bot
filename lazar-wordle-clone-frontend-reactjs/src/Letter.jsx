import React from 'react';

const Letter = ({ letter, status, onClick, isOnKeyboard}) => {
  // Define CSS classes based on the status and smallSquare prop
  let letterBackgroundColor = 'white';
  let squareSize = '50px'; // Default square size
  const letterStyle = {
    width: squareSize,
    height: squareSize,
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    cursor: 'pointer',
    color: 'black',
    textAlign: 'center'
  };
  if(isOnKeyboard){
    letterStyle.borderRadius = '100%';
  }
  // Define background colors based on the status
  switch (status) {
    case 'GREY':
      letterBackgroundColor = 'grey';
      break;
    case 'GREEN':
      letterBackgroundColor = 'green';
      break;
    case 'YELLOW':
        letterBackgroundColor = 'yellow';
      break;
    default:
        letterBackgroundColor = 'white';
  }

  letterStyle.backgroundColor = letterBackgroundColor;

  return (
    <div style={letterStyle} onClick={() => onClick(letter)}>
      {letter}
    </div>
  );
};

export default Letter;
