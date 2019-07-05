import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Execution from './execution';
import Query from './query';
import Connexion from './connexion';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/execution`} component={Execution} />
      <ErrorBoundaryRoute path={`${match.url}/query`} component={Query} />
      <ErrorBoundaryRoute path={`${match.url}/connexion`} component={Connexion} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
