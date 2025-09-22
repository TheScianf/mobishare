import sharedData from '../../shared-data.json';
import axios from 'axios';
import {headers} from './Utils.ts';

const backend_core = sharedData.backend_core;
const backend_ai = sharedData.backend_ai;

export interface DockInfo {
  number: number;
  idVehicle: number | null;
}

export interface sensorReports {
  time: string; //attenzione, fare casting da date a string nel backend
  value: number;
}

export interface customerReports {
  time: string;
  description: string;
  customer: string;
}

export interface VehicleInfoWithStatus {
  id: number;
  vehicleType: string;
  status: boolean;
  sensorReports: sensorReports[];
  customerReports: customerReports[];
}

export interface ParkInfo {
  id: number;
  name: string;
  docCount: number;
  vehicles: VehicleInfoWithStatus[];
  docks: DockInfo[];
}

export interface ParksInfo {
  parks: ParkInfo[];
}

export interface FormattedReport {
  id: number;
  rating: number;
  bullet_points: string[];
}

export interface MaintenanceVehicle {
  id: number;
  immissionDate: string;
  dismissionDate: string;
  vehicleType: string;
}

export interface Maintenance {
  idMaintenances: number;
  vehicle: MaintenanceVehicle;
  description: string;
  start: string;
}

export interface SimplePark {
  id: number;
  name: string;
}

export interface ReportAI {
  state: number;
  summary: string;
  priorities: number[];
}

async function getParksInfo(token: string): Promise<ParksInfo | Error> {
  try {
    console.log(token);
    const info = await axios.get(
      `${backend_core}/parks/vehiclesWithStatusAndReports`,
      {headers: headers(token)},
    );
    console.log(info);
    return {
      parks: info.data,
    };
  } catch (error: any) {
    console.log(error);
    return new Error(error.message);
  }
}

async function getParksInfoManager(
  token: string,
): Promise<ParksInfo | Error> {
  try {
    console.log(token);
    const info = await axios.get(
      `${backend_core}/parks/vehiclesWithStatusAndReports`,
      {headers: headers(token)},
    );
    console.log(info);
    return {
      parks: info.data,
    };
  } catch (error: any) {
    console.log(error);
    return new Error(error.message);
  }
}

async function getParks(token: string): Promise<SimplePark[] | Error> {
  try {
    const response = await axios.get(`${backend_core}/parks`, {
      headers: headers(token),
    });
    return response.data;
  } catch (error: any) {
    console.error('Errore durante il recupero dei parcheggi:', error);
    return new Error(error.message);
  }
}

async function getMaintainingVehicles(
  token: string,
): Promise<Maintenance[] | Error> {
  try {
    const info = await axios.get(`${backend_core}/vehicles/maintenance`, {
      headers: headers(token),
    });
    console.log(info);
    return info.data;
  } catch (error: any) {
    return new Error(error.message);
  }
}

async function setEndMaintenance(
  vehicleId: number,
  token: string,
  parkId: number,
): Promise<undefined | Error> {
  try {
    console.log(vehicleId);
    console.log(parkId);
    const url = `${backend_core}/vehicles/maintenance/end?vehicleId=${vehicleId}&parkId=${parkId}`;
    console.log('URL finale:', url); // per debug

    const response = await axios.post(
      url,
      {},
      {
        headers: headers(token),
      },
    );

    return response.status === 200
      ? undefined
      : new Error(`Status: ${response.status}`);
  } catch (error: any) {
    console.log(error);
    return new Error(error.response?.data?.message || error.message);
  }
}

async function moveVehiclesToPark(
  vehiclesIds: number[],
  parkId: number,
  token: string,
) {
  try {
    const vehicleParams = vehiclesIds.map(id => `vehicleIds=${id}`).join('&');
    const url = `${backend_core}/parks/vehicles/move?${vehicleParams}&parkId=${parkId}`;
    console.log(vehiclesIds);
    const response = await axios.post(url, {}, {headers: headers(token)});

    if (response.status === 200) {
      return undefined;
    }
  } catch (error: any) {
    console.log(error);
    return new Error(error.message);
  }
}

async function getAddNewVehicle(
  parkId: number,
  vehicleType: number,
  token: string,
): Promise<undefined | Error> {
  try {
    console.log(vehicleType);
    const response = await axios.post(
      `${backend_core}/parks/${parkId}/vehicle`,
      {}, // corpo vuoto se non serve nulla
      {
        headers: headers(token),
        params: {vehicleType},
      },
    );
    if (response.status === 200) {
      return undefined;
    } else {
      return new Error(`Errore: status ${response.status}`);
    }
  } catch (error: any) {
    console.log(error);
    return new Error(error.message || 'Errore generico');
  }
}

async function setMaintainingVehicle(
  vehicleId: number,
  token: string,
): Promise<undefined | Error> {
  try {
    const description = 'prova';
    const response = await axios.post(
      `${backend_core}/vehicles/maintenance`,
      {},
      {headers: headers(token), params: {vehicleId, description}},
    );
    if (response.status === 200) {
      return undefined;
    } else {
      return new Error('Errore generico');
    }
  } catch (error: any) {
    console.log(error);
    return new Error(error.message || 'Errore generico');
  }
}

async function getFormattedReports(
  vehicleId: number,
  token: string,
): Promise<FormattedReport[] | Error> {
  try {
    console.log(token);
    const response = await axios.get(`${backend_ai}/feedback`, {
      params: {
        id_vehicle: vehicleId,
        token: token,
      },
    });
    console.log(response.data);
    return response.data;
  } catch (error: any) {
    console.log(error);
    return new Error(error.message);
  }
}

async function getReportAI(
  parkId: number,
  token: string,
): Promise<ReportAI | Error> {
  try {
    console.log(token);
    console.log(parkId);
    const response = await axios.get(`${backend_core}/parks/${parkId}/report`, {
      headers: headers(token),
    });
    console.log(response);
    return response.data;
  } catch (error: any) {
    console.log(error);
    return new Error(error.message || 'Errore generico');
  }
}

export default {
  getParksInfo,
  setEndMaintenance,
  getAddNewVehicle,
  setMaintainingVehicle,
  getFormattedReports,
  getMaintainingVehicles,
  getParks,
  getReportAI,
  moveVehiclesToPark,
  getParksInfoManager,
};
