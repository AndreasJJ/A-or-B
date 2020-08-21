import React, { useEffect } from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';

const Tile = styled(Link)`
  height: 200px;
  width: 200px;
  box-shadow: rgba(0, 0, 0, 0.15) 0px 2px 4px 0px;
  border: 1px solid #8080802b;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;

  &:hover {
    border: 2px solid #B6D8F2;
    -webkit-box-shadow: 0px 0px 5px #B6D8F2;
    box-shadow: 0px 0px 5px #B6D8F2;
    cursor: pointer;
  }
`;

interface ImageProps {
  url: string;
}

const Image = styled.div<ImageProps>`
  flex: 1;
  background-image: url(${(props) => props.url});
  background-size: cover;
`;

const Description = styled.div`
  display: block;
  border-top: 1px solid darkslategray;
  padding: 5px 4px 0px 4px;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  text-align: center;
`;

interface GameTileprops {
  id: string;
  title: string;
  thumbnail: string;
}

/**
 * Component for 
 */
export const GameTile: React.FC<GameTileprops> = (props) => {
  return (
    <Tile to={`/create/${props.id}`}>
      <Image url={props.thumbnail} />
      <Description>
        {props.title}
      </Description>
    </Tile>
  );
};