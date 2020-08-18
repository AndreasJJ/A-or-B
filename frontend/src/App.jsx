import React from 'react';
import { KeycloakProvider } from '@react-keycloak/web';
import keycloak from './keycloak';

import { GlobalStyle } from './global-styles';
import { Router } from './router/router';

function App() {
  return (
    <KeycloakProvider keycloak={keycloak}>
      <Router />
      <GlobalStyle />
    </KeycloakProvider>
  );
}

export default App;