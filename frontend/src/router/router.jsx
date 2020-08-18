import React from 'react';
import { Switch, Route, Router, Redirect } from 'react-router-dom';
import { useKeycloak } from '@react-keycloak/web';

import { Loading } from '../sharedComponents/Loading';

import { AuthenticatedRoute } from '../router/AuthenticatedRoute';
import { Websocket } from '../pages/websocket';
import { Login } from '../pages/login';
import { Registration } from '../pages/registration';
import { Logout } from '../pages/logout';

// History
import { history } from './history';

const RouterComponent = () =>  {
  const [, initialized] = useKeycloak();

  if (!initialized) {
    return <Loading />;
  }

  return (
    <Router history={history}>
      <Switch>
        <Route exact path="/" component={Websocket} />
        <AuthenticatedRoute exact={true} path="/" not={<Redirect to="/login" />} is={<Websocket />} />
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