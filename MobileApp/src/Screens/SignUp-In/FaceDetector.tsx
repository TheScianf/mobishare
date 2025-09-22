import React, { useState } from 'react';
import {
    View,
    Text,
    Image,
    TouchableOpacity,
    Alert,
    SafeAreaView,
    PermissionsAndroid,
    Platform,
} from 'react-native';
import { launchCamera, launchImageLibrary, ImagePickerResponse } from 'react-native-image-picker';
import {AuthEntity} from '../../Contexts/AuthContext.tsx';
import FaceRecognition from '../../Requests/FaceRecognition.ts';
import {styles} from '../../StyleSheets/faceDetectorStyle.tsx';


type Photo = {
    uri: string;
    type?: string;
    fileName?: string;
    fileSize?: number;
};

const CameraScreen = ({
                          isAuthenticated,
                      }: {
    isAuthenticated: AuthEntity;
}) => {
    const [photo, setPhoto] = useState<Photo | null>(null);
    const [loading, setLoading] = useState(false);

    const requestCameraPermission = async (): Promise<boolean> => {
        if (Platform.OS === 'android') {
            try {
                const granted = await PermissionsAndroid.request(
                    PermissionsAndroid.PERMISSIONS.CAMERA,
                    {
                        title: 'Permesso Fotocamera',
                        message: "L'app ha bisogno di accedere alla fotocamera per scattare foto",
                        buttonNeutral: 'Chiedi più tardi',
                        buttonNegative: 'Annulla',
                        buttonPositive: 'OK',
                    }
                );
                return granted === PermissionsAndroid.RESULTS.GRANTED;
            } catch (err) {
                console.warn(err);
                return false;
            }
        }
        return true;
    };

    const handleConfirm = async () => {
        if (!photo) {
            Alert.alert('Errore', 'Nessuna foto da inviare');
            return;
        }

        const result = await FaceRecognition.faceRegistration(photo, isAuthenticated.username as string);

        if (result) {
            Alert.alert('Errore', result.message);
        } else {
            Alert.alert('Successo', 'Foto inviata al server');
        }
    };

    const handleImageResponse = (response: ImagePickerResponse) => {
        setLoading(false);

        if (response.didCancel) {
            console.log('Operazione annullata');
            return;
        }

        if (response.errorCode) {
            let errorMessage = 'Si è verificato un errore imprevisto';
            if (response.errorCode === 'camera_unavailable') {errorMessage = 'Fotocamera non disponibile';}
            else if (response.errorCode === 'permission') {errorMessage = 'Permessi non concessi';}
            Alert.alert('Errore', errorMessage);
            return;
        }

        const asset = response.assets?.[0];
        if (asset?.uri?.startsWith('file://') || asset?.uri?.startsWith('content://')) {
            setPhoto({
                uri: asset.uri,
                type: asset.type,
                fileName: asset.fileName,
                fileSize: asset.fileSize,
            });
        }
    };

    const takePhoto = async () => {
        const hasPermission = await requestCameraPermission();
        if (!hasPermission) {
            Alert.alert('Permesso Negato', 'Non posso accedere alla fotocamera senza permesso');
            return;
        }

        setLoading(true);
        launchCamera(
            {
                mediaType: 'photo',
                includeBase64: false,
                maxHeight: 2000,
                maxWidth: 2000,
                quality: 1,
                cameraType: 'front',
                saveToPhotos: true,
            },
            handleImageResponse
        );
    };

    const chooseFromGallery = () => {
        setLoading(true);
        launchImageLibrary(
            {
                mediaType: 'photo',
                includeBase64: false,
                maxHeight: 2000,
                maxWidth: 2000,
                quality: 0.8,
            },
            handleImageResponse
        );
    };

    const handleAddPhoto = () => {
        Alert.alert(
            'Seleziona Immagine',
            'Scegli come vuoi aggiungere la foto',
            [
                { text: 'Fotocamera', onPress: () => setTimeout(takePhoto, 100) },
                { text: 'Galleria', onPress: () => setTimeout(chooseFromGallery, 100) },
                { text: 'Annulla', style: 'cancel' },
            ]
        );
    };

    const removePhoto = () => {
        Alert.alert('Rimuovi Foto', 'Sei sicuro di voler rimuovere questa foto?', [
            { text: 'Annulla', style: 'cancel' },
            { text: 'Rimuovi', style: 'destructive', onPress: () => setPhoto(null) },
        ]);
    };

    return (
        <SafeAreaView style={styles.container}>

            <View style={styles.header}>
                <Text style={styles.title}>📸 Memorizza il tuo volto</Text>
                <Text style={styles.subtitle}>Cattura il tuo volto per un accesso più rapido</Text>
            </View>

            <View style={styles.photoContent}>
                {photo ? (
                    <View style={styles.photoContainer}>
                        <Image source={{ uri: photo.uri }} style={styles.preview} />
                        <View style={styles.photoInfo}>
                            <Text style={styles.fileName}>{photo.fileName || 'Immagine'}</Text>
                            <Text style={styles.fileSize}>
                                {photo.fileSize ? `${(photo.fileSize / 1024 / 1024).toFixed(2)} MB` : ''}
                            </Text>
                        </View>
                        <View style={styles.photoButtons}>
                            <TouchableOpacity
                                style={[styles.actionButton, styles.confirmButton, { marginRight: 10 }]}
                                onPress={handleConfirm}
                            >
                                <Text style={styles.actionText}>✅ Conferma</Text>
                            </TouchableOpacity>

                            <TouchableOpacity
                                style={[styles.actionButton, styles.removeButton]}
                                onPress={removePhoto}
                            >
                                <Text style={styles.actionText}>🗑️ Rimuovi</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                ) : (
                    <View style={styles.placeholder}>
                        <Text style={styles.placeholderIcon}>📷</Text>
                        <Text style={styles.placeholderText}>Nessuna foto selezionata</Text>
                        <Text style={styles.placeholderHint}>Tocca il pulsante per iniziare</Text>
                    </View>
                )}
            </View>

            {!photo && (
                <View style={styles.footerButtons}>
                    <TouchableOpacity
                        style={[styles.primaryButton, loading && styles.disabledButton]}
                        onPress={handleAddPhoto}
                        disabled={loading}
                    >
                        <Text style={styles.primaryButtonText}>
                            {loading ? '⏳ Caricamento...' : '📸 Aggiungi Foto'}
                        </Text>
                    </TouchableOpacity>
                </View>
            )}
        </SafeAreaView>
    );
};

export default CameraScreen;
