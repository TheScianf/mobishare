import {AuthEntity} from '../Contexts/AuthContext.tsx';
import axios, {AxiosError} from 'axios';
import sharedData from '../../shared-data.json';
import {headers, LoginError, RegisterError} from './Utils.ts';
import {isAdmin} from './Manager.ts';

const backend_core = sharedData.backend_core;

async function login(
  username: string,
  password: string,
): Promise<AuthEntity | LoginError> {
  try {
    const response = await axios.post(backend_core + '/auth/login', {
      username: username,
      password: password,
    });

    if (response.status === 200) {
      let token = response.data.token;

      try {
        const result = await axios.get(backend_core + '/auth/manager-login', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (result.status === 200) {
          //is manager or admin
          const amIAdmin = await isAdmin(token);
          if (amIAdmin) {
            return new AuthEntity(username, token, AuthEntity.ADMIN);
          } else {
            return new AuthEntity(username, token, AuthEntity.MANAGER);
          }
        } else {
          return new LoginError(
            'Unexpected error occurred, code: ' + result.status,
          );
        }
      } catch (result: any) {
        //is customer
        if (result.status === 403) {
          return new AuthEntity(username, token, AuthEntity.CUSTOMER);
        } else {
          return new LoginError(
            'An unexpected error occurred, contact developer',
          );
        }
      }
    } else {
      return new LoginError(
        'Unexpected error occurred, code: ' + response.status,
      );
    }
  } catch (error: any) {
    if (error.status === 401) {
      return new LoginError('Username o password non corretti');
    } else {
      return new LoginError(`Unexpected error occurred, code:${error.code}`);
    }
  }
}

async function checkToken(auth: AuthEntity): Promise<boolean> {
  if (auth.token == undefined) {
    return false;
  }
  const token = auth.token;
  try {
    const response = await axios.get(backend_core + '/auth/token-check', {
      headers: headers(token),
    });
    return response.status === 200;
  } catch (error: any) {
    return false;
  }
}

async function register(
  username: string,
  password: string,
  firstName: string,
  surname: string,
  gender: string,
  cf: string,
  email: string,
): Promise<undefined | RegisterError> {
  try {
    const response = await axios.post(backend_core + '/auth/register', {
      username: username,
      password: password,
      name: firstName,
      surname: surname,
      gender: gender,
      cf: cf,
      email: email,
    });

    if (response.status === 201) {
      return undefined;
    }
  } catch (error: any) {
    console.log((error as AxiosError).response?.data);
    if (error.status === 400) {
      return new RegisterError(
        `Dati non validi:\n ${Object.entries(
          (error as AxiosError).response?.data ?? {},
        )
          .map(entry => {
            return `\n${entry[0]}: ${entry[1]}`;
          })
          .concat()}`,
      );
    } else if (error.status === 409) {
      return new RegisterError(`Username già in uso`);
    } else {
      return new RegisterError(error);
    }
  }
}

export default {login, register, checkToken};
