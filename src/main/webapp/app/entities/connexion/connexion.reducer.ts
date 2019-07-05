import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IConnexion, defaultValue } from 'app/shared/model/connexion.model';

export const ACTION_TYPES = {
  FETCH_CONNEXION_LIST: 'connexion/FETCH_CONNEXION_LIST',
  FETCH_CONNEXION: 'connexion/FETCH_CONNEXION',
  CREATE_CONNEXION: 'connexion/CREATE_CONNEXION',
  UPDATE_CONNEXION: 'connexion/UPDATE_CONNEXION',
  DELETE_CONNEXION: 'connexion/DELETE_CONNEXION',
  RESET: 'connexion/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IConnexion>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ConnexionState = Readonly<typeof initialState>;

// Reducer

export default (state: ConnexionState = initialState, action): ConnexionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CONNEXION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CONNEXION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CONNEXION):
    case REQUEST(ACTION_TYPES.UPDATE_CONNEXION):
    case REQUEST(ACTION_TYPES.DELETE_CONNEXION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CONNEXION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CONNEXION):
    case FAILURE(ACTION_TYPES.CREATE_CONNEXION):
    case FAILURE(ACTION_TYPES.UPDATE_CONNEXION):
    case FAILURE(ACTION_TYPES.DELETE_CONNEXION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONNEXION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONNEXION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CONNEXION):
    case SUCCESS(ACTION_TYPES.UPDATE_CONNEXION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CONNEXION):
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

const apiUrl = 'api/connexions';

// Actions

export const getEntities: ICrudGetAllAction<IConnexion> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CONNEXION_LIST,
  payload: axios.get<IConnexion>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IConnexion> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CONNEXION,
    payload: axios.get<IConnexion>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IConnexion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CONNEXION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IConnexion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CONNEXION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IConnexion> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CONNEXION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
