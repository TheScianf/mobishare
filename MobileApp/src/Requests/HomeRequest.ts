import sharedData from '../../shared-data.json';
import axios from 'axios';
import {headers} from './Utils.ts';

const backend_core = sharedData.backend_core;


export interface park {
    id: number;
    name: string;
    longitude: string;
    latitude: string;
    bikeCount: number;
    ebikeCount: number;
    scooterCount: number;
}


async function getParks(token: string): Promise<park[] | Error> {
    try {
        const info = await axios.get(`${backend_core}/parks/info`, { headers: headers(token) });
        return info.data;
    } catch (error: any) {
        console.log(error);
        return new Error(error.message);
    }
}

export default {
    getParks,
};


