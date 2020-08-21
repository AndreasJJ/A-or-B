import React from 'react';
import styled from 'styled-components';

const Wrapper = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    width: 100%;
    font-size: xxx-large;
`;

interface PlayProps {
  pin: string;
}

/**
 * Login component that redirects to keycloak for login
 */
export const Play: React.FC<PlayProps> = (props) => {

  return (
    <Wrapper>
        
    </Wrapper>
  );
};