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
import { getEntity, updateEntity, createEntity, reset } from './connexion.reducer';
import { IConnexion } from 'app/shared/model/connexion.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IConnexionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IConnexionUpdateState {
  isNew: boolean;
  conxUserId: string;
}

export class ConnexionUpdate extends React.Component<IConnexionUpdateProps, IConnexionUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      conxUserId: '0',
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
    const { connexionEntity, users, loading, updating } = this.props;
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
                  <Label id="typeLabel" for="type">
                    Type
                  </Label>
                  <AvField
                    id="connexion-type"
                    type="text"
                    name="type"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    Name
                  </Label>
                  <AvField id="connexion-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label id="driverLabel" for="driver">
                    Driver
                  </Label>
                  <AvField id="connexion-driver" type="text" name="driver" />
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
  connexionEntity: storeState.connexion.entity,
  loading: storeState.connexion.loading,
  updating: storeState.connexion.updating,
  updateSuccess: storeState.connexion.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
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
