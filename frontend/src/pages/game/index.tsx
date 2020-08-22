import React from 'react';
import styled from 'styled-components';
import { useParams } from 'react-router-dom';

import { Code } from './Code';
import { Play } from './Play';
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
  const { code } = useParams();
  const name = false;

  return (
    <Wrapper>
      {
        (!code && <Code title="A or B" />) || (code && !name && <Join title="A or B" />) || (name && <Play code={code} />)
      }
    </Wrapper>
  );
};
