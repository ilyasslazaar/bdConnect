import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './connexion.reducer';
import { IConnexion } from 'app/shared/model/connexion.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IConnexionProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Connexion extends React.Component<IConnexionProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { connexionList, match } = this.props;
    return (
      <div>
        <h2 id="connexion-heading">
          Connexions
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Connexion
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Type</th>
                <th>Name</th>
                <th>Driver</th>
                <th>User</th>
                <th>Password</th>
                <th>Ssl</th>
                <th>Port</th>
                <th>Hostname</th>
                <th>Conx User</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {connexionList.map((connexion, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${connexion.id}`} color="link" size="sm">
                      {connexion.id}
                    </Button>
                  </td>
                  <td>{connexion.type}</td>
                  <td>{connexion.name}</td>
                  <td>{connexion.driver}</td>
                  <td>{connexion.user}</td>
                  <td>{connexion.password}</td>
                  <td>{connexion.ssl ? 'true' : 'false'}</td>
                  <td>{connexion.port}</td>
                  <td>{connexion.hostname}</td>
                  <td>{connexion.conxUser ? connexion.conxUser.login : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${connexion.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${connexion.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${connexion.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ connexion }: IRootState) => ({
  connexionList: connexion.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Connexion);
