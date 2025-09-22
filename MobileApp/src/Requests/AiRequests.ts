import axios from 'axios';

import {backend_ai} from '../../shared-data.json';
import {headers} from './Utils.ts';
import {AuthEntity} from '../Contexts/AuthContext.tsx';

const roleEndpointMapping = {
  [AuthEntity.MANAGER as number]: 'manager',
  [AuthEntity.CUSTOMER as number]: 'user',
  [AuthEntity.ADMIN as number]: 'admin',
};

export async function sendMessage(
  token: string,
  username: string,
  role: number,
  message: string,
): Promise<string | {error: string}> {
  try {
    const response = await axios.post(
      `${backend_ai}/chat/${roleEndpointMapping[role]}`,
      {
        username,
        prompt: message,
      },
      {
        headers: headers(token),
      },
    );
    if (response.status === 200) {
      return response.data;
    } else {
      return {error: 'Errore: servizio non disponibile, riprova più tardi...'};
    }
  } catch (error: any) {
    return {error: 'Errore generico...'};
  }
}
