import axios from 'axios';
import {headers} from './Utils.ts';
import {backend_core} from '../../shared-data.json';
import {User, Suspension} from '../Screens/Manager/ListaUtentiSospesi.tsx';
import {Manager} from '../Screens/Manager/ManageManager.tsx';

export async function getAllSuspendedUsers(token: string): Promise<User[]> {
  try {
    const result = await axios.get(`${backend_core}/suspensions?active=true`, {
      headers: headers(token),
    });
    return result.data.map((suspension: any): User => {
      return {
        username: suspension.username,
        name: suspension.name + ' ' + suspension.surname,
        email: suspension.email,
        suspendedAt: new Date(suspension.start).toLocaleString(),
        toPay: (suspension.paid ? '0' : suspension.value.toString()) + '€',
      };
    }) as User[];
  } catch (error: any) {
    console.log(error);
    return [] as User[];
  }
}

export async function setUserLastSuspensionState(
  token: string,
  username: string,
  status: 'rejected' | 'accepted',
): Promise<void> {
  console.log(token);
  try {
    await axios.put(
      `${backend_core}/suspensions/byUser/${username}/last/isRejected`,
      {
        isRejected: status === 'rejected',
      },
      {
        headers: headers(token),
      },
    );
  } catch (error: any) {
    console.log(error);
    throw error;
  }
}

export async function isAdmin(token: string): Promise<boolean> {
  try {
    const response = await axios.get(`${backend_core}/auth/admin-login`, {
      headers: headers(token),
    });
    return response.status === 200;
  } catch (error: any) {
    return false;
  }
}



export async function getChrono(username: string, token: string): Promise<Suspension[] | Error> {
  try {
    const result = await axios.get(`${backend_core}/suspended/chrono`, {params: {username}, headers: headers(token)});
    if(result.status === 200) {
      return result.data;
    } else {
      return new Error(result.statusText);
    }
  } catch (error: any) {
    console.log(error);
    return new Error(error.message());
  }
}

export async function toggleAdmin(id: number, token: string): Promise<undefined | Error> {
  try {
    console.log(id);
    const result = await axios.put(`${backend_core}/managers/${id}/toggleAdmin`, {}, {headers: headers(token)});
    if(result.status === 200) {
      return undefined;
    }
  } catch (error: any) {
    console.log(error);
    return new Error(error.message);
  }
}

export async function getAdmins(token: string): Promise<Manager[] | Error> {
  try {
    const result = await axios.get(`${backend_core}/managers`, {
      headers: headers(token),
    });
    if (result.status === 200) {
      console.log(result.data);
      return result.data as Manager[];
    } else {
      return new Error('Errore durante la richiesta.');
    }
  } catch (error: any) {
    console.log(error);
    return new Error(error.message);
  }
}

export async function addAdmin(
    email: string,
    password: string,
    admin: boolean,
    token: string
): Promise<undefined | Error> {
  try {
    const result = await axios.post(
        `${backend_core}/managers`,
        { email, password, admin },
        { headers: headers(token) }
    );

    if (result.status === 201 || result.status === 200) {
      return undefined;
    } else {
      return new Error('Errore durante la creazione dell\'admin.');
    }
  } catch (error: any) {
    console.log(error);
    return new Error(error.message);
  }
}

export async function removeManager(id: number, substitutorId: number, token: string): Promise<undefined | Error> {
  try {
    const result = await axios.delete(`${backend_core}/managers/${id}`, {headers: headers(token), params: {substitutorId}});
    if (result.status === 201 || result.status === 200) {
      return undefined;
    } else {
      return new Error('Errore durante la creazione dell\'admin.');
    }
  } catch (error: any) {
    console.log(error);
    return new Error(error.message);
  }
}


