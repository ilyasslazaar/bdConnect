import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './execution.reducer';
import { IExecution } from 'app/shared/model/execution.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IExecutionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ExecutionDetail extends React.Component<IExecutionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { executionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Execution [<b>{executionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="exDate">Ex Date</span>
            </dt>
            <dd>
              <TextFormat value={executionEntity.exDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="status">Status</span>
            </dt>
            <dd>{executionEntity.status ? 'true' : 'false'}</dd>
            <dt>Query</dt>
            <dd>{executionEntity.query ? executionEntity.query.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/execution" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/execution/${executionEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ execution }: IRootState) => ({
  executionEntity: execution.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ExecutionDetail);
