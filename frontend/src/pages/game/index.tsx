import React, { useEffect } from 'react';
import styled from 'styled-components';
import { useParams } from 'react-router-dom';

import { Pin } from './Pin';
import { Play } from './Play';
import { Join } from './Join';

const Wrapper = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    width: 100%;
    font-size: xxx-large;
`;

/**
 * Login component that redirects to keycloak for login
 */
export const Game: React.FC = () => {
  const { pin } = useParams();
  const name = false;

  return (
    <Wrapper>
      {
        (!pin && <Pin title="Trap or Fap" />) || (pin && !name && <Join title="Trap or Fap" />) || (name && <Play pin={pin} />)
      }
    </Wrapper>
  );
};