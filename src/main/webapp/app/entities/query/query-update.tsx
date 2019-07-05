import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IConnexion } from 'app/shared/model/connexion.model';
import { getEntities as getConnexions } from 'app/entities/connexion/connexion.reducer';
import { getEntity, updateEntity, createEntity, reset } from './query.reducer';
import { IQuery } from 'app/shared/model/query.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IQueryUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IQueryUpdateState {
  isNew: boolean;
  connexionId: string;
}

export class QueryUpdate extends React.Component<IQueryUpdateProps, IQueryUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      connexionId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getConnexions();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { queryEntity } = this.props;
      const entity = {
        ...queryEntity,
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
    this.props.history.push('/entity/query');
  };

  render() {
    const { queryEntity, connexions, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="reportingFullStackApp.query.home.createOrEditLabel">Create or edit a Query</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : queryEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="query-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="typeLabel" for="type">
                    Type
                  </Label>
                  <AvField
                    id="query-type"
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
                  <AvField id="query-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label id="statmentLabel" for="statment">
                    Statment
                  </Label>
                  <AvField id="query-statment" type="text" name="statment" />
                </AvGroup>
                <AvGroup>
                  <Label id="created_atLabel" for="created_at">
                    Created At
                  </Label>
                  <AvField id="query-created_at" type="date" className="form-control" name="created_at" />
                </AvGroup>
                <AvGroup>
                  <Label for="connexion.id">Connexion</Label>
                  <AvInput id="query-connexion" type="select" className="form-control" name="connexion.id">
                    <option value="" key="0" />
                    {connexions
                      ? connexions.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/query" replace color="info">
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
  connexions: storeState.connexion.entities,
  queryEntity: storeState.query.entity,
  loading: storeState.query.loading,
  updating: storeState.query.updating,
  updateSuccess: storeState.query.updateSuccess
});

const mapDispatchToProps = {
  getConnexions,
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
)(QueryUpdate);
