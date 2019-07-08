import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IConnector } from 'app/shared/model/connector.model';
import { getEntities as getConnectors } from 'app/entities/connector/connector.reducer';
import { getEntity, updateEntity, createEntity, reset } from './connexion.reducer';
import { IConnexion } from 'app/shared/model/connexion.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IConnexionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IConnexionUpdateState {
  isNew: boolean;
  conxUserId: string;
  connectorId: string;
}

export class ConnexionUpdate extends React.Component<IConnexionUpdateProps, IConnexionUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      conxUserId: '0',
      connectorId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getUsers();
    this.props.getConnectors();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { connexionEntity } = this.props;
      const entity = {
        ...connexionEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/connexion');
  };

  render() {
    const { connexionEntity, users, connectors, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="reportingFullStackApp.connexion.home.createOrEditLabel">Create or edit a Connexion</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : connexionEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="connexion-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    Name
                  </Label>
                  <AvField id="connexion-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label id="userLabel" for="user">
                    User
                  </Label>
                  <AvField id="connexion-user" type="text" name="user" />
                </AvGroup>
                <AvGroup>
                  <Label id="passwordLabel" for="password">
                    Password
                  </Label>
                  <AvField id="connexion-password" type="text" name="password" />
                </AvGroup>
                <AvGroup>
                  <Label id="sslLabel" check>
                    <AvInput id="connexion-ssl" type="checkbox" className="form-control" name="ssl" />
                    Ssl
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="portLabel" for="port">
                    Port
                  </Label>
                  <AvField id="connexion-port" type="text" name="port" />
                </AvGroup>
                <AvGroup>
                  <Label id="hostnameLabel" for="hostname">
                    Hostname
                  </Label>
                  <AvField id="connexion-hostname" type="text" name="hostname" />
                </AvGroup>
                <AvGroup>
                  <Label id="currentDatabaseLabel" for="currentDatabase">
                    Current Database
                  </Label>
                  <AvField id="connexion-currentDatabase" type="text" name="currentDatabase" />
                </AvGroup>
                <AvGroup>
                  <Label for="conxUser.login">Conx User</Label>
                  <AvInput id="connexion-conxUser" type="select" className="form-control" name="conxUser.id">
                    <option value="" key="0" />
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="connector.id">Connector</Label>
                  <AvInput id="connexion-connector" type="select" className="form-control" name="connector.id">
                    <option value="" key="0" />
                    {connectors
                      ? connectors.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/connexion" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  users: storeState.userManagement.users,
  connectors: storeState.connector.entities,
  connexionEntity: storeState.connexion.entity,
  loading: storeState.connexion.loading,
  updating: storeState.connexion.updating,
  updateSuccess: storeState.connexion.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
  getConnectors,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ConnexionUpdate);
