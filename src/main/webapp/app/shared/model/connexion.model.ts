import { IQuery } from 'app/shared/model/query.model';
import { IUser } from 'app/shared/model/user.model';

export interface IConnexion {
  id?: number;
  type?: string;
  name?: string;
  driver?: string;
  user?: string;
  password?: string;
  ssl?: boolean;
  port?: string;
  hostname?: string;
  queries?: IQuery[];
  conxUser?: IUser;
}

export const defaultValue: Readonly<IConnexion> = {
  ssl: false
};
