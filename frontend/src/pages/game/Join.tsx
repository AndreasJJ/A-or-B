import React, { useState } from 'react';
import styled from 'styled-components';
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

interface JoinProps {
  title?: string | React.ReactNode;
}

/**
 * Login component that redirects to keycloak for login
 */
export const Join: React.FC<JoinProps> = ({ title }) => {
  const [name, setName] = useState('');

  const onNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.persist();
    setName(e.currentTarget.value);
  };

  const onSubmit = (e: React.SyntheticEvent) => {
    e.preventDefault();
  };

  return (
    <Fade>
      {title && typeof title === 'string' ? <Title>{title}</Title> : title}
      <Form onSubmit={onSubmit}>
        <Input type="text" placeholder="Name" value={name} onChange={onNameChange} />
        <Button>Go!</Button>
      </Form>
    </Fade>
  );
};
