import React, { useState } from 'react';
import styled from 'styled-components';

import { Button } from './Button';

const Wrapper = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    width: 100%;
    box-sizing: border-box;
    
    &:not(:last-child) {
      margin-bottom: 20px;
    }
`;

const InputsWrapper = styled.div`
    flex: 1;
    display: flex;
    justify-content: space-between;
`;

const Input = styled.input`
  height: 30px;
  font-weight: bold;
  font-size: 16px;
  flex: 1;
  margin-right: 30px;
`;

interface NewRoundProps {
  title: string;
  link: string;
  index: number;
  delete: (index: number) => void;
  onChange: (index: number, round: {title: string; link: string;}) => void;
}

/**
 * Component 
 */
export const NewRound: React.FC<NewRoundProps> = (props) => {
  const onDelete = () => {
    props.delete(props.index);
  }

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.persist();
    const value = e.currentTarget.value;
    const name = e.currentTarget.name;
    switch(name) {
      case "title": {
        props.onChange(props.index, {title: value, link: props.link});
        break;
      }
      case "link": {
        props.onChange(props.index, {title: props.title, link: value});
        break;
      }
    }
  }

  return (
    <Wrapper>
      <InputsWrapper>
        <Input type="text" name="title" placeholder="Round title" value={props.title} onChange={onChange} />
        <Input type="url" name="link" placeholder="Image or youtube video link" value={props.link} onChange={onChange} />
      </InputsWrapper>
      <Button text="Delete" type="error" onClick={onDelete} />
    </Wrapper>
  );
};