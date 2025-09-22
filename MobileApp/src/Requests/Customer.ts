import axios from 'axios';

import {backend_core} from '../../shared-data.json';
import {headers} from './Utils.ts';

export async function isSuspended(token: string): Promise<boolean> {
  try {
    const response = await axios.get(`${backend_core}/auth/suspended-login`, {
      headers: headers(token),
    });
    return response.status === 200;
  } catch (error: any) {
    return false;
  }
}

export async function isP_Suspended(token: string): Promise<boolean> {
  try {
    const response = await axios.get(
      `${backend_core}/auth/suspended-login?permanently=true`,
      {
        headers: headers(token),
      },
    );
    return response.status === 200;
  } catch (error: any) {
    return false;
  }
}
