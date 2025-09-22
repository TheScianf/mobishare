import sharedData from '../../shared-data.json';
import {headers} from './Utils.ts';
import axios from 'axios';

const backend_core = sharedData.backend_core;


export interface raceVehicle {
    id: number;
    vehicleType: string;
    constantPrice: number;
    minutePrice: number;
}


async function checkQR(
    username: string,
    vehicleId: number,
    token: string
): Promise<raceVehicle | Error> {
    try {
        console.log(vehicleId);
        console.log(token);
        const response = await axios.get(`${backend_core}/vehicles/${vehicleId}/available`, {
            headers: headers(token),
            params: { username },
        });
        console.log(response.data);
        return response.data as raceVehicle;
    } catch (error: any) {
        if (axios.isAxiosError(error) && error.response) {
            const status = error.response.status;
            if (status === 405) {
                return new Error('Mezzo non esistente');
            }
            if (status === 406) {
                return new Error('Mezzo non disponibile');
            }
        }
        return new Error(error.message);
    }
}

async function startRace(username: string, vehicleId: number, token: string): Promise<string | Error> {
    try {
        const response = await axios.post(`${backend_core}/race`, {}, {headers: headers(token), params: {username, vehicleId}});
        console.log(response.data);
        if(response.status === 200) {
            return response.data;
        } else {
            return new Error(response.statusText);
        }
    } catch (error: any) {
        console.log(error.message);
        return new Error(error.message);
    }
}

async function endRace(
    username: string,
    vehicleId: number,
    start: string,
    token: string
): Promise<number | 'SOSPESO' | 'GIÀ_PARCHEGGIATO' | 'DOCK_MANCANTE' | Error> {
    try {
        const response = await axios.post(
            `${backend_core}/race/end`,
            {},
            {
                headers: headers(token),
                params: { vehicleId, username, start },
            }
        );
        return response.status;
    } catch (error: any) {
        if (axios.isAxiosError(error) && error.response) {
            const status = error.response.status;
            switch (status) {
                case 402:
                    return 'SOSPESO';
                case 405:
                    return 'GIÀ_PARCHEGGIATO';
                case 409:
                    return 'DOCK_MANCANTE';
                default:
                    return new Error(`Errore ${status}: ${error.response.data?.message || 'Errore sconosciuto'}`);
            }
        }
        return new Error(error.message || 'Errore sconosciuto');
    }
}

async function sendFeedback(
    username: string,
    text: string,
    vehicleId: number,
    token: string
): Promise<undefined | Error> {
    try {
        const response = await axios.post(
            `${backend_core}/customers/${username}/feedback`,
            {},
            {
                headers: headers(token),
                params: { text, vehicleId },
            }
        );

        if (response.status === 200) {
            return undefined;
        }

        return new Error(`Errore feedback: ${response.status} ${response.statusText}`);
    } catch (error: any) {

        console.log('Errore imprevisto:', error);
        return new Error(error.message || 'Errore imprevisto');
    }
}


export default {
    checkQR,
    startRace,
    endRace,
    sendFeedback,
};
