import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Execution from './execution';
import ExecutionDetail from './execution-detail';
import ExecutionUpdate from './execution-update';
import ExecutionDeleteDialog from './execution-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ExecutionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ExecutionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ExecutionDetail} />
      <ErrorBoundaryRoute path={match.url} component={Execution} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ExecutionDeleteDialog} />
  </>
);

export default Routes;
