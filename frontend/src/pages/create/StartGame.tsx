import React from 'react';
import styled from 'styled-components';
import { useParams } from 'react-router-dom';
import { useKeycloak } from '@react-keycloak/web';
import useSWR from 'swr';

import { Loading } from '../../sharedComponents/Loading';
import { Button } from './Button';
import { fetcher } from '../../utils/Fetcher';
import { RoundGallery } from './RoundGallery';

const Wrapper = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100%;
    width: 100%;
    padding: 40px;
    box-sizing: border-box;
    
    &:not(:last-child) {
      margin-bottom: 20px;
    }
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 80%;
  margin-bottom: 20px;
  background-color: white;
  padding: 15px;
`;

const H2 = styled.h2`
  margin: 0;
`;

const Content = styled.div`
  display: flex;
  justify-content: space-evenly;
  align-items: center;
  height: 100%;
  width: 80%;
  box-sizing: border-box;
`;

/**
 * Component
 */
export const StartGame: React.FC = () => {
  const { id } = useParams();
  const { keycloak } = useKeycloak();

  const { data: game } = useSWR<{
    id: string;
    title: string;
    left_text: string;
    right_text: string;
    rounds: Array<{ title: string; link: string }>;
  }>([`/api/game/${id}`, keycloak.token], fetcher);

  if (!game) {
    return <Loading />;
  }

  return (
    <Wrapper>
      <Header>
        <H2>{game.title}</H2>
        <Button text="Create Game Session!" type="info" />
      </Header>
      <Content>
        <Button text={game.left_text || ""} type="success" />
        <RoundGallery rounds={game.rounds} />
        <Button text={game.right_text || ""} type="error" />
      </Content>
    </Wrapper>
  );
};
