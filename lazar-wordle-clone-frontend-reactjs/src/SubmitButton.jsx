import React from 'react';
import Button from 'react-bootstrap/Button'; // Import React Bootstrap's Button component

function SubmitButton({ onClick }) {
  return (
    <Button onClick={onClick} variant="primary">
      Submit
    </Button>
  );
}

export default SubmitButton;