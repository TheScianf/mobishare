import React, { useCallback, useEffect, useState } from 'react';
import {
    View,
    Text,
    FlatList,
    SafeAreaView,
    TouchableOpacity,
    Modal,
    ActivityIndicator,
} from 'react-native';
import { Picker } from '@react-native-picker/picker';
import FontAwesome5 from 'react-native-vector-icons/FontAwesome5';
import ParkManagerRequest, {
    Maintenance,
    SimplePark,
} from '../../Requests/ParkManagerRequest';
import { styles } from '../../StyleSheets/paginaMezziManutenzioneStyle.tsx';
import { useAuth } from '../../Contexts/AuthContext.tsx';

export default function GestioneManutenzioni() {
    const [maintenanceList, setMaintenanceList] = useState<Maintenance[]>([]);
    const [selectedMaintenance, setSelectedMaintenance] = useState<Maintenance | null>(null);
    const [modalVisible, setModalVisible] = useState(false);
    const [selectedParcheggio, setSelectedParcheggio] = useState<number>(-1);
    const [parks, setParksInfo] = useState<SimplePark[]>([]);
    const { isAuthenticated } = useAuth();
    const [isLoading, setIsLoading] = useState(true);
    const [notification, setNotification] = useState<{
        message: string;
        type: 'info' | 'success' | 'error'
    } | null>(null);

    const showNotification = (message: string, type: 'info' | 'success' | 'error' = 'info') => {
        setNotification({ message, type });
        setTimeout(() => setNotification(null), 3000);
    };

    const fetchData = useCallback(async () => {
        setIsLoading(true);
        try {
            const [maintenanceResult, parksResult] = await Promise.all([
                ParkManagerRequest.getMaintainingVehicles(isAuthenticated.token as string),
                ParkManagerRequest.getParks(isAuthenticated.token as string),
            ]);

            if (maintenanceResult instanceof Error) {
                showNotification(maintenanceResult.message, 'error');
                setMaintenanceList([]);
            } else {
                setMaintenanceList(maintenanceResult);
            }

            if (parksResult instanceof Error) {
                showNotification(parksResult.message, 'error');
                setParksInfo([]);
            } else {
                setParksInfo(parksResult);
            }

        } catch (error) {
            showNotification('Errore durante il caricamento dei dati.', 'error');
        } finally {
            setIsLoading(false);
        }
    }, [isAuthenticated.token]);

    useEffect(() => {
        fetchData();
    }, [fetchData]);

    const apriModalRipristino = (manutenzione: Maintenance) => {
        setSelectedMaintenance(manutenzione);
        setSelectedParcheggio(-1);
        setModalVisible(true);
    };

    const confermaRipristino = async () => {
        if (!selectedMaintenance || selectedParcheggio === -1) {
            showNotification('Seleziona un parcheggio prima di confermare.', 'error');
            return;
        }

        try {
            await ParkManagerRequest.setEndMaintenance(
                selectedMaintenance.vehicle.id,
                isAuthenticated.token as string,
                selectedParcheggio
            );

            showNotification(
                `Mezzo ${selectedMaintenance.vehicle.id} spostato nel parcheggio ${selectedParcheggio}.`,
                'success'
            );

            setModalVisible(false);
            setSelectedMaintenance(null);
            setSelectedParcheggio(-1);

            fetchData();
        } catch {
            showNotification('Ripristino non riuscito.', 'error');
        }
    };

    if (isLoading) {
        return (
            <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
                <ActivityIndicator size="large" color="#00aaff" />
                <Text style={{ marginTop: 10 }}>Caricamento manutenzioni...</Text>
            </View>
        );
    }

    const NotificationBanner = ({ message, type }: { message: string, type: string }) => (
        <View style={[
            styles.notification,
            (styles as any)[`notification${type.charAt(0).toUpperCase() + type.slice(1)}`],
        ]}>
            <Text style={styles.notificationText}>{message}</Text>
        </View>
    );

    return (
        <SafeAreaView style={styles.container}>
            <Text style={styles.titolo}>🔧 Gestione Manutenzioni</Text>

            {notification && <NotificationBanner message={notification.message} type={notification.type} />}

            <FlatList
                data={maintenanceList}
                keyExtractor={(item, index) => item.idMaintenances?.toString() ?? `maintenance-${index}`}                contentContainerStyle={{ paddingHorizontal: 16, paddingBottom: 20 }}
                renderItem={({ item }) => (
                    <View style={styles.card}>
                        <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' }}>
                            <View style={{ flex: 1, paddingRight: 12 }}>
                                <Text style={styles.nome}>Mezzo #{item.vehicle.id}</Text>
                                <Text style={styles.tipo}>
                                    Tipo: <Text style={{ fontWeight: '600' }}>{item.vehicle.vehicleType}</Text>
                                </Text>
                                <Text style={styles.tipo}>
                                    Immissione: {new Date(item.vehicle.immissionDate).toLocaleDateString()}
                                </Text>
                                <Text style={styles.tipo}>
                                    Inizio: {new Date(item.start).toLocaleString()}
                                </Text>
                                <Text style={styles.tipo}>{item.description}</Text>
                            </View>
                            <TouchableOpacity
                                onPress={() => apriModalRipristino(item)}
                                accessibilityLabel={`Apri modal ripristino mezzo ${item.vehicle.id}`}
                            >
                                <FontAwesome5 name="wrench" size={24} color="#eab308" />
                            </TouchableOpacity>
                        </View>
                    </View>
                )}
                ListEmptyComponent={
                    <Text style={[styles.empty, { textAlign: 'center', marginTop: 20 }]}>
                        Nessun mezzo in manutenzione!
                    </Text>
                }
            />

            <Modal
                visible={modalVisible}
                transparent
                animationType="slide"
                onRequestClose={() => setModalVisible(false)}
            >
                <View style={styles.modalOverlay}>
                    <View style={styles.modalContainer}>
                        <Text style={styles.modalTitle}>
                            Ripristina mezzo #{selectedMaintenance?.vehicle.id}
                        </Text>

                        <Picker
                            selectedValue={selectedParcheggio}
                            onValueChange={(value) => setSelectedParcheggio(value)}
                            style={{ marginBottom: 16 }}
                        >
                            <Picker.Item label="-- Seleziona parcheggio --" value={-1} />
                            {parks.map((p) => (
                                <Picker.Item key={p.id} label={p.name} value={p.id} />
                            ))}
                        </Picker>

                        <TouchableOpacity style={styles.bottoneConferma} onPress={confermaRipristino}>
                            <Text style={styles.bottoneTesto}>✅ Conferma ripristino</Text>
                        </TouchableOpacity>

                        <TouchableOpacity onPress={() => setModalVisible(false)} style={{ alignItems: 'center' }}>
                            <Text style={styles.chiudiTesto}>Chiudi</Text>
                        </TouchableOpacity>
                    </View>
                </View>
            </Modal>
        </SafeAreaView>
    );
}