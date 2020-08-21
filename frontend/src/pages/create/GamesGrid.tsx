import React from 'react';
import styled from 'styled-components';

import { GameTile } from './GameTile';

const Grid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, 200px);
  grid-template-rows: repeat(auto-fill, 200px);
  grid-row-gap: 10px;
  grid-column-gap: 10px;
  justify-content: space-between;
  height: 100%;
  width: 100%;
  box-sizing: border-box;
  overflow: auto;
  padding: 2px;
`;

const None = styled.span`
  font-size: xxx-large;
  font-weight: bolder;
  color: dimgray;
`;

interface GamesGridProps {
  games: Array<{
    id: string;
    title: string;
    thumbnail: string;
  }>;
}

/**
 * Component 
 */
export const GamesGrid: React.FC<GamesGridProps> = (props) => {
  return (
    <>
      {props.games && props.games.length > 0 ? (
        <Grid>
          {props.games.map((game) => <GameTile key={game.id} {...game} />)}
        </Grid>
      ) : (
        <None>No Games...</None>
      )}
    </>
  );
};