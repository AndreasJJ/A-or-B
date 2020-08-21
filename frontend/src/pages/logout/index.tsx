import React, { useEffect } from 'react';
import styled from 'styled-components';
import { useKeycloak } from '@react-keycloak/web';

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
export const Logout: React.FC = () => {
  // Getting Keycloak instance
  const { keycloak } = useKeycloak();

  useEffect(() => {
    keycloak.logout();
  });

  return (
    <Wrapper>Logger deg ut...</Wrapper>
  );
};
