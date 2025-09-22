import sharedData from '../../shared-data.json';
import axios from 'axios';
import {headers} from './Utils.ts';

const backend_core = sharedData.backend_core;

export interface CustomerInfo {
  username: string;
  name: string;
  surname: string;
  cf: string;
  email: string;
  gender: string;
}

export interface Payment {
  id: number;
  value: number;
  time: Date;
}

async function getCustomerInfo(
  username: string,
  token: string,
): Promise<CustomerInfo | undefined> {
  try {
    const info = await axios.get(`${backend_core}/customers/${username}`, {
      headers: headers(token),
    });
    return {
      username: info.data.username,
      name: info.data.name,
      surname: info.data.surname,
      email: info.data.email,
      cf: info.data.cf,
      gender: info.data.gender,
    };
  } catch (error: any) {
    return undefined;
  }
}

async function getCustomerCredit(
  username: string,
  token: string,
): Promise<number> {
  try {
    const result = await axios.get(
      `${backend_core}/customers/${username}/credit`,
      {
        headers: headers(token),
      },
    );
    return Number((result.data as number).toFixed(2));
  } catch (error: any) {
    return 0;
  }
}

async function getCustomerPayments(username: string, token: string): Promise<Payment[] | undefined> {
  try {
    const result = await axios.get(
      `${backend_core}/customers/${username}/payments`,
      {
        headers: headers(token),
      },
    );
    return result.data as Payment[];
  } catch (error: any) {
    return undefined;
  }
}

async function getCustomerGreenPoints(
  username: string,
  token: string,
): Promise<number> {
  try {
    const result = await axios.get(
      `${backend_core}/customers/${username}/greenPoints`,
      {
        headers: headers(token),
      },
    );
    return result.data as number;
  } catch (error: any) {
    return 0;
  }
}

async function getCustomerLastTransaction(
  username: string,
  token: string,
): Promise<number> {
  try {
    const result = await axios.get(
      `${backend_core}/customers/${username}/transactions/last`,
      {
        headers: headers(token),
      },
    );
    return result.data.value as number;
  } catch (error: any) {
    return 0;
  }
}

async function rechargeWallet(username: string, euro: number, token: string): Promise<undefined | Error> {
  try {
    const result = await axios.post(
        `${backend_core}/customers/${username}/wallet/recharge`,
        {}, // corpo della richiesta vuoto
        {
          headers: headers(token),
          params: { euro },
        }
    );

    if (result.status === 200) {
      return undefined;
    } else {
      return new Error(`Errore imprevisto: codice ${result.status}`);
    }
  } catch (error: any) {
    console.log(error.message);
    return new Error(error.message);
  }
}


export default {
  getCustomerInfo,
  getCustomerCredit,
  getCustomerGreenPoints,
  getCustomerPayments,
  getCustomerLastTransaction,
  rechargeWallet,
};
