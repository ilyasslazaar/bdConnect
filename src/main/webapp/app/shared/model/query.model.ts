import { Moment } from 'moment';
import { IExecution } from 'app/shared/model/execution.model';
import { IConnexion } from 'app/shared/model/connexion.model';

export interface IQuery {
  id?: number;
  type?: string;
  name?: string;
  statment?: string;
  created_at?: Moment;
  executions?: IExecution[];
  connexion?: IConnexion;
}

export const defaultValue: Readonly<IQuery> = {};
