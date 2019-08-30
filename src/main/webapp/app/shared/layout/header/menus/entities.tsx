import React from 'react';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from '../header-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name="Entities" id="entity-menu">
    <DropdownItem tag={Link} to="/entity/execution">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Execution
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/query">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Query
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/connexion">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Connexion
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/connector">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Connector
    </DropdownItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
