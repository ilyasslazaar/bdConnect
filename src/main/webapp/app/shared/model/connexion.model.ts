import { IQuery } from 'app/shared/model/query.model';
import { IUser } from 'app/shared/model/user.model';
import { IConnector } from 'app/shared/model/connector.model';

export interface IConnexion {
  id?: number;
  name?: string;
  user?: string;
  password?: string;
  ssl?: boolean;
  port?: string;
  hostname?: string;
  currentDatabase?: string;
  queries?: IQuery[];
  conxUser?: IUser;
  connector?: IConnector;
}

export const defaultValue: Readonly<IConnexion> = {
  ssl: false
};
