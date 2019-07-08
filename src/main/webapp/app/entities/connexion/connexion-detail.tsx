import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './connexion.reducer';
import { IConnexion } from 'app/shared/model/connexion.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IConnexionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ConnexionDetail extends React.Component<IConnexionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { connexionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Connexion [<b>{connexionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{connexionEntity.name}</dd>
            <dt>
              <span id="user">User</span>
            </dt>
            <dd>{connexionEntity.user}</dd>
            <dt>
              <span id="password">Password</span>
            </dt>
            <dd>{connexionEntity.password}</dd>
            <dt>
              <span id="ssl">Ssl</span>
            </dt>
            <dd>{connexionEntity.ssl ? 'true' : 'false'}</dd>
            <dt>
              <span id="port">Port</span>
            </dt>
            <dd>{connexionEntity.port}</dd>
            <dt>
              <span id="hostname">Hostname</span>
            </dt>
            <dd>{connexionEntity.hostname}</dd>
            <dt>
              <span id="currentDatabase">Current Database</span>
            </dt>
            <dd>{connexionEntity.currentDatabase}</dd>
            <dt>Conx User</dt>
            <dd>{connexionEntity.conxUser ? connexionEntity.conxUser.login : ''}</dd>
            <dt>Connector</dt>
            <dd>{connexionEntity.connector ? connexionEntity.connector.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/connexion" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/connexion/${connexionEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ connexion }: IRootState) => ({
  connexionEntity: connexion.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ConnexionDetail);
