// Board.js

import React from 'react';
import Letter from './Letter';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import './Board.css'; // Import your custom CSS file

const Board = ({ userTries, currentWord }) => {
  return (
    <Container className="board">
      <Row className="justify-content-center"> {/* Center the board on the x-axis */}
        {userTries.map((userTry, tryIndex) => (
          <Row key={tryIndex} className="board-row">
            {userTry.validated_word.letters.map((letterObj, index) => (
              <Col key={index} xs={2} className="board-col">
                <Letter letter={letterObj.letter} status={letterObj.status} smallSquare className="letter-cell" />
              </Col>
            ))}
          </Row>
        ))}
        {currentWord && (
          <Row className="board-row current-word">
            {currentWord.split('').map((letter, index) => (
              <Col key={index} xs={2} className="board-col">
                <Letter letter={letter} smallSquare className="letter-cell" />
              </Col>
            ))}
          </Row>
        )}
      </Row>
    </Container>
  );
};

export default Board;
