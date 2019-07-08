import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IConnector, defaultValue } from 'app/shared/model/connector.model';

export const ACTION_TYPES = {
  FETCH_CONNECTOR_LIST: 'connector/FETCH_CONNECTOR_LIST',
  FETCH_CONNECTOR: 'connector/FETCH_CONNECTOR',
  CREATE_CONNECTOR: 'connector/CREATE_CONNECTOR',
  UPDATE_CONNECTOR: 'connector/UPDATE_CONNECTOR',
  DELETE_CONNECTOR: 'connector/DELETE_CONNECTOR',
  RESET: 'connector/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IConnector>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ConnectorState = Readonly<typeof initialState>;

// Reducer

export default (state: ConnectorState = initialState, action): ConnectorState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CONNECTOR_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CONNECTOR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CONNECTOR):
    case REQUEST(ACTION_TYPES.UPDATE_CONNECTOR):
    case REQUEST(ACTION_TYPES.DELETE_CONNECTOR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CONNECTOR_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CONNECTOR):
    case FAILURE(ACTION_TYPES.CREATE_CONNECTOR):
    case FAILURE(ACTION_TYPES.UPDATE_CONNECTOR):
    case FAILURE(ACTION_TYPES.DELETE_CONNECTOR):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONNECTOR_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONNECTOR):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CONNECTOR):
    case SUCCESS(ACTION_TYPES.UPDATE_CONNECTOR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CONNECTOR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/connectors';

// Actions

export const getEntities: ICrudGetAllAction<IConnector> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CONNECTOR_LIST,
    payload: axios.get<IConnector>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IConnector> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CONNECTOR,
    payload: axios.get<IConnector>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IConnector> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CONNECTOR,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IConnector> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CONNECTOR,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IConnector> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CONNECTOR,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
