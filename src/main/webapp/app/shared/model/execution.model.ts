import { Moment } from 'moment';
import { IQuery } from 'app/shared/model/query.model';

export interface IExecution {
  id?: number;
  exDate?: Moment;
  status?: boolean;
  query?: IQuery;
}

export const defaultValue: Readonly<IExecution> = {
  status: false
};
