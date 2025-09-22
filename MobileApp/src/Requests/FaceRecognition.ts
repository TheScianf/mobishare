import axios from 'axios';
import {RegisterError} from './Utils.ts';
import sharedData from '../../shared-data.json';


const backend_ai = sharedData.backend_ai;


type Photo = {
    uri: string;
    type?: string;
    fileName?: string;
};



async function faceRegistration(photo: Photo, username: string): Promise<RegisterError | undefined> {
    try {
        const formData = new FormData();

        console.log(username);
        username = 'Paladino'; // ?

        formData.append('image', {
            uri: photo.uri,
            name: photo.fileName || 'photo.jpg',
            type: photo.type || 'image/jpeg',
        } as any);

        const response = await axios.post(
            `${backend_ai}/face_recognition/register`,
            formData,
            {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'username': username,
                },
            }
        );

        return undefined;  // successo
    } catch (error) {
        console.error('Errore nella registrazione facciale:', error);
        return new RegisterError('Errore durante la registrazione facciale');
    }
}



async function faceSignIn(photo: Photo): Promise<RegisterError | undefined> {

    try {

        const formData = new FormData();

        formData.append('image', {
            uri: photo.uri,
            name: photo.fileName || 'photo.jpg',
            type: photo.type || 'image/jpeg',
        } as any);

        const response = await axios.post(
            `${backend_ai}/face_recognition/verify`,
            formData,
            {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            }
        );

        return undefined;  // successo
    } catch (error) {
        console.error('Errore durante il riconoscimento del volto:', error);
        return new RegisterError('Errore durante il riconoscimento del volto ');
    }
}

export default { faceRegistration, faceSignIn };
