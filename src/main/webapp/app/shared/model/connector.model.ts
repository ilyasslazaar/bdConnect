import { IConnexion } from 'app/shared/model/connexion.model';

export interface IConnector {
  id?: number;
  type?: string;
  driver?: string;
  connexions?: IConnexion[];
}

export const defaultValue: Readonly<IConnector> = {};
