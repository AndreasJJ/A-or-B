import React from 'react';
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
export const NewRound: React.FC<NewRoundProps> = ({
  title, link, index, delete: deleteRound, onChange,
}) => {
  const onDelete = () => {
    deleteRound(index);
  };

  const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.persist();
    const { value } = e.currentTarget;
    const { name } = e.currentTarget;
    switch (name) {
      case 'title': {
        onChange(index, { title: value, link });
        break;
      }
      case 'link': {
        onChange(index, { title, link: value });
        break;
      }
      default: {
        break;
      }
    }
  };

  return (
    <Wrapper>
      <InputsWrapper>
        <Input type="text" name="title" placeholder="Round title" value={title} onChange={onInputChange} />
        <Input type="url" name="link" placeholder="Image or youtube video link" value={link} onChange={onInputChange} />
      </InputsWrapper>
      <Button text="Delete" type="error" onClick={onDelete} />
    </Wrapper>
  );
};
