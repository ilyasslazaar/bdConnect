import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Connector from './connector';
import ConnectorDetail from './connector-detail';
import ConnectorUpdate from './connector-update';
import ConnectorDeleteDialog from './connector-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ConnectorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ConnectorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ConnectorDetail} />
      <ErrorBoundaryRoute path={match.url} component={Connector} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ConnectorDeleteDialog} />
  </>
);

export default Routes;
