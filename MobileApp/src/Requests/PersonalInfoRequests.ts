import sharedData from '../../shared-data.json';
import axios from 'axios';
import {headers} from './Utils';

const backend_core = sharedData.backend_core;

export interface CustomerInfo {
  username: string;
  name: string;
  surname: string;
  cf: string;
  email: string;
  gender: string;
}

export interface ManagerInfo {
  id: number;
  email: string;
  isAdmin: boolean;
}

async function getCustomerInfo(
  username: string,
  token: string,
): Promise<CustomerInfo | undefined> {
  try {
    const info = await axios.get(`${backend_core}/customers/${username}`, {
      headers: headers(token),
    });
    return  {
      username: info.data.username,
      name: info.data.name,
      surname: info.data.surname,
      email: info.data.email,
      cf: info.data.cf,
      gender: info.data.gender,
    };
  } catch (error: any) {
    console.log(error);
    return undefined;
  }
}

async function getManagerInfo(
    username: string,
    token: string,
): Promise<ManagerInfo | undefined> {
  try {
    const info = await axios.get(`${backend_core}/managers/${username}`, {
      headers: headers(token),
    });
    return  {
      id: info.data.id,
      email: info.data.email,
      isAdmin: info.data.isAdmin,
    };
  } catch (error: any) {
    console.log(error);
    return undefined;
  }
}

export default {
  getCustomerInfo,
  getManagerInfo,
};
