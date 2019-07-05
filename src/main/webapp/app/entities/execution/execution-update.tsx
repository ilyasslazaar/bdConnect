import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IQuery } from 'app/shared/model/query.model';
import { getEntities as getQueries } from 'app/entities/query/query.reducer';
import { getEntity, updateEntity, createEntity, reset } from './execution.reducer';
import { IExecution } from 'app/shared/model/execution.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IExecutionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IExecutionUpdateState {
  isNew: boolean;
  queryId: string;
}

export class ExecutionUpdate extends React.Component<IExecutionUpdateProps, IExecutionUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      queryId: '0',
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

    this.props.getQueries();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { executionEntity } = this.props;
      const entity = {
        ...executionEntity,
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
    this.props.history.push('/entity/execution');
  };

  render() {
    const { executionEntity, queries, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="reportingFullStackApp.execution.home.createOrEditLabel">Create or edit a Execution</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : executionEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="execution-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="exDateLabel" for="exDate">
                    Ex Date
                  </Label>
                  <AvField id="execution-exDate" type="date" className="form-control" name="exDate" />
                </AvGroup>
                <AvGroup>
                  <Label id="statusLabel" check>
                    <AvInput id="execution-status" type="checkbox" className="form-control" name="status" />
                    Status
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label for="query.id">Query</Label>
                  <AvInput id="execution-query" type="select" className="form-control" name="query.id">
                    <option value="" key="0" />
                    {queries
                      ? queries.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/execution" replace color="info">
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
  queries: storeState.query.entities,
  executionEntity: storeState.execution.entity,
  loading: storeState.execution.loading,
  updating: storeState.execution.updating,
  updateSuccess: storeState.execution.updateSuccess
});

const mapDispatchToProps = {
  getQueries,
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
)(ExecutionUpdate);
