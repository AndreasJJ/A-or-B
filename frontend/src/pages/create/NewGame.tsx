import React, { useState } from 'react';
import styled from 'styled-components';
import { useKeycloak } from '@react-keycloak/web';

import { Button } from './Button';
import { NewRound } from './NewRound';
import { PostToApi } from '../../utils/PostToApi';

const Wrapper = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100%;
    width: 100%;
    padding: 40px;
    box-sizing: border-box;
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 80%;
  margin-bottom: 20px;
`;

const Input = styled.input`
  height: 30px;
  flex: 1;
  margin-right: 30px;
  font-weight: bold;
  font-size: 16px;
`;

const Content = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  height: 100%;
  width: 80%;
  box-sizing: border-box;
  overflow: auto;
`;

/**
 * Component for
 */
export const NewGame: React.FC = () => {
  const { keycloak } = useKeycloak();

  const [title, setTitle] = useState("");
  const [leftText, setLeftText] = useState("");
  const [rightText, setRightText] = useState("");
  const [rounds, setRounds] = useState<Array<{title: string; link: string;}>>([]);

  const newRound = () => {
    setRounds((prev) => [...prev, {title: "", link: ""}]);
  };

  const onRoundChange = (index: number, round: {title: string; link: string;}) => {
    setRounds((prev) => [...prev.slice(0, index), round , ...prev.slice(index + 1)]);
  };

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.persist();
    const value = e.currentTarget.value;
    const name = e.currentTarget.name;
    switch(name) {
      case "title": {
        setTitle(value);
        break;
      }
      case "left_text": {
        setLeftText(value);
        break;
      }
      case "right_text": {
        setRightText(value);
        break;
      }
    }
  };

  const deleteRound = (index: number) => {
    setRounds((prev) => prev.filter((round, _index) => _index !== index));
  };

  const submitGame = () => {
    const data = {
      title: title,
      left_text: leftText,
      right_text: rightText,
      rounds: rounds,
    };

    PostToApi('/api/game/new', data, keycloak.token);
  }

  return (
    <Wrapper>
      <Header>
        <Input name="title" placeholder="Game title" value={title} onChange={onChange} />
        <Input name="left_text" placeholder="Left button text" value={leftText} onChange={onChange} />
        <Input name="right_text" placeholder="Right button text" value={rightText} onChange={onChange} />
        <Button text="New Round" type="info" onClick={newRound} />
      </Header>
      <Content>
        {rounds.map((round, index) => (
          <NewRound index={index} delete={deleteRound} onChange={onRoundChange} {...round} />
        ))}
      </Content>
      <Button text="Submit Game!" type="success" styling="width: 80%;" onClick={submitGame} />
    </Wrapper>
  );
};

