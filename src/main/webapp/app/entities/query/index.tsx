import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Query from './query';
import QueryDetail from './query-detail';
import QueryUpdate from './query-update';
import QueryDeleteDialog from './query-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={QueryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={QueryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={QueryDetail} />
      <ErrorBoundaryRoute path={match.url} component={Query} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={QueryDeleteDialog} />
  </>
);

export default Routes;
