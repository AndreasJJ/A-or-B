import React, { useEffect } from 'react';
import styled from 'styled-components';
import { useHistory } from 'react-router-dom';
import { useKeycloak } from '@react-keycloak/web';
import useSWR from 'swr';

import { Button } from './Button';
import { GamesGrid } from './GamesGrid';
import { fetcher } from '../../utils/Fetcher';

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

const H2 = styled.h2`
  margin: 0;
`;

const Content = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  width: 80%;
  box-sizing: border-box;
`;

/**
 * Component for gamee creation
 */
export const Create: React.FC = () => {
  const { keycloak } = useKeycloak();
  const history = useHistory();

  const newGame = () => {
    history.push('/create/new');
  };

  const { data, error, isValidating } = useSWR<Array<{
    id: string;
    title: string;
    thumbnail: string;
  }>>(['/api/game/list', keycloak.token], fetcher);
  const games = data || [];

  return (
    <Wrapper>
      <Header>
        <H2>My Games</H2>
        <Button text="New Game!" type="info" onClick={newGame} />
      </Header>
      <Content>
        <GamesGrid games={games} />
      </Content>
    </Wrapper>
  );
};