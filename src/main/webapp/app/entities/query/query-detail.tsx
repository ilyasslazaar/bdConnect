import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './query.reducer';
import { IQuery } from 'app/shared/model/query.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IQueryDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class QueryDetail extends React.Component<IQueryDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { queryEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Query [<b>{queryEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="type">Type</span>
            </dt>
            <dd>{queryEntity.type}</dd>
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{queryEntity.name}</dd>
            <dt>
              <span id="statment">Statment</span>
            </dt>
            <dd>{queryEntity.statment}</dd>
            <dt>
              <span id="created_at">Created At</span>
            </dt>
            <dd>
              <TextFormat value={queryEntity.created_at} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="database">Database</span>
            </dt>
            <dd>{queryEntity.database}</dd>
            <dt>Connexion</dt>
            <dd>{queryEntity.connexion ? queryEntity.connexion.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/query" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/query/${queryEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ query }: IRootState) => ({
  queryEntity: query.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(QueryDetail);
