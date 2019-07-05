import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Connexion from './connexion';
import ConnexionDetail from './connexion-detail';
import ConnexionUpdate from './connexion-update';
import ConnexionDeleteDialog from './connexion-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ConnexionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ConnexionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ConnexionDetail} />
      <ErrorBoundaryRoute path={match.url} component={Connexion} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ConnexionDeleteDialog} />
  </>
);

export default Routes;
