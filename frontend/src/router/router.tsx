import React from 'react';
import {
  Switch, Route, Router, Redirect,
} from 'react-router-dom';
import { useKeycloak } from '@react-keycloak/web';

import { Loading } from '../sharedComponents/Loading';

import { AuthenticatedRoute } from './AuthenticatedRoute';
import { Websocket } from '../pages/websocket';
import { Login } from '../pages/login';
import { Registration } from '../pages/registration';
import { Logout } from '../pages/logout';
import { Game } from '../pages/game';
import { Create } from '../pages/create';
import { NewGame } from '../pages/create/NewGame';

// History
import { historyObj } from './historyObj';

const RouterComponent: React.FC = () => {
  const [, initialized] = useKeycloak();

  if (!initialized) {
    return <Loading />;
  }

  return (
    <Router history={historyObj}>
      <Switch>
        <AuthenticatedRoute exact path="/" not={<Redirect to="/login" />} is={<Websocket />} />
        <Route path="/game/:pin?" component={Game} />
        <AuthenticatedRoute path="/create/new" not={<Redirect to="/" />} is={<NewGame />} />
        <AuthenticatedRoute path="/create" not={<Redirect to="/" />} is={<Create />} />
        <AuthenticatedRoute path="/login" not={<Login />} is={<Redirect to="/" />} />
        <AuthenticatedRoute path="/register" not={<Registration />} is={<Redirect to="/" />} />
        <AuthenticatedRoute path="/logout" not={<Redirect to="/" />} is={<Logout />} />
      </Switch>
    </Router>
  );
};

export {
  RouterComponent as Router,
};
