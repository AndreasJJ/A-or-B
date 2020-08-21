import React, { useEffect, useState, ReactNode } from 'react';
import styled from 'styled-components';
import { useHistory } from 'react-router-dom';
import { Fade } from './Fade';

const Title = styled.h1`
  font-size: 1em;
  margin-bottom: 0.4em;
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
`;

const Input = styled.input`
  box-sizing: border-box;
  height: 45px;
  width: 200px;
  text-align: center;
  font-size: 1.25rem;
  font-weight: bold;
  border-width: 0.125rem;

  margin-bottom: 20px;
`;

const Button = styled.button`
  height: 45px;
  width: 200px;
  font-size: 1.25rem;
  font-weight: bold;
  color: #ffffff;
  background-color: #212121;
`;

interface PinProps {
  title?: string | React.ReactNode;
}

/**
 * Pin component
 */
export const Pin: React.FC<PinProps> = (props) => {
  const history = useHistory();
  
  const [pin, setPin] = useState('');

  const onPinChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.persist();
    setPin(e.currentTarget.value);
  };

  const onSubmit = (e: React.SyntheticEvent) => {
    e.preventDefault();

    if (pin) {
      // TODO: Do some check to see if the pin exists
      history.push(`/game/${pin}`);
    }
  }

  return (
    <Fade>
        {props.title && typeof props.title == "string" ? <Title>{props.title}</Title> : props.title}
        <Form onSubmit={onSubmit}>
          <Input type="tel" placeholder="Pin" value={pin} onChange={onPinChange} />
          <Button>Join</Button>
        </Form>
    </Fade>
  );
};

