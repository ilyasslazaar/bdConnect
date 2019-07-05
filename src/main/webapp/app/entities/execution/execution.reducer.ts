import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IExecution, defaultValue } from 'app/shared/model/execution.model';

export const ACTION_TYPES = {
  FETCH_EXECUTION_LIST: 'execution/FETCH_EXECUTION_LIST',
  FETCH_EXECUTION: 'execution/FETCH_EXECUTION',
  CREATE_EXECUTION: 'execution/CREATE_EXECUTION',
  UPDATE_EXECUTION: 'execution/UPDATE_EXECUTION',
  DELETE_EXECUTION: 'execution/DELETE_EXECUTION',
  RESET: 'execution/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IExecution>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ExecutionState = Readonly<typeof initialState>;

// Reducer

export default (state: ExecutionState = initialState, action): ExecutionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_EXECUTION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_EXECUTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_EXECUTION):
    case REQUEST(ACTION_TYPES.UPDATE_EXECUTION):
    case REQUEST(ACTION_TYPES.DELETE_EXECUTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_EXECUTION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_EXECUTION):
    case FAILURE(ACTION_TYPES.CREATE_EXECUTION):
    case FAILURE(ACTION_TYPES.UPDATE_EXECUTION):
    case FAILURE(ACTION_TYPES.DELETE_EXECUTION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_EXECUTION_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_EXECUTION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_EXECUTION):
    case SUCCESS(ACTION_TYPES.UPDATE_EXECUTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_EXECUTION):
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

const apiUrl = 'api/executions';

// Actions

export const getEntities: ICrudGetAllAction<IExecution> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_EXECUTION_LIST,
    payload: axios.get<IExecution>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IExecution> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_EXECUTION,
    payload: axios.get<IExecution>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IExecution> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_EXECUTION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IExecution> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_EXECUTION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IExecution> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_EXECUTION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
