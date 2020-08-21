import React from 'react';
import { KeycloakProvider } from '@react-keycloak/web';
import keycloak from './keycloak';

import { GlobalStyle } from './global-styles';
import { Router } from './router/router';

const App: React.FC = () => (
  <KeycloakProvider keycloak={keycloak}>
    <Router />
    <GlobalStyle />
  </KeycloakProvider>
);

export default App;
