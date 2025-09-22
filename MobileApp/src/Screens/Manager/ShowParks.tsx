import React, {useCallback, useEffect, useState} from 'react';
import {
    View,
    Text,
    ScrollView,
    TouchableOpacity,
    Modal,
} from 'react-native';
import { Picker } from '@react-native-picker/picker';
import ParkManagerRequest, {
    ParksInfo,
    ParkInfo,
    VehicleInfoWithStatus,
    FormattedReport,
    ReportAI,
} from '../../Requests/ParkManagerRequest.ts';
import { styles } from '../../StyleSheets/showParksStyle.tsx';
import {useAuth} from '../../Contexts/AuthContext.tsx';
import { ActivityIndicator } from 'react-native';

const vehicleIcons: Record<string, string> = {
    'Bici muscolare': '🚲',
    'Bici elettrica': '⚡',
    'Monopattino elettrico': '🛴',
};


function Vehicle({
                     vehicle,
                     onPress,
                     onLongPress,
                     selected,
                 }: {
    vehicle: VehicleInfoWithStatus | undefined;
    onPress: (vehicle: VehicleInfoWithStatus) => void;
    onLongPress: (vehicle: VehicleInfoWithStatus) => void;
    selected: boolean;
}) {
    const bgColor = vehicle
        ? vehicle.status
            ? '#4CAF50'
            : '#F44336'
        : '#555';

    return (
        <TouchableOpacity
            disabled={!vehicle}
            onPress={() => vehicle && onPress(vehicle)}
            onLongPress={() => vehicle && onLongPress(vehicle)}
            style={[
                styles.cell,
                { backgroundColor: bgColor },
                selected && styles.selectedCell,
            ]}
        >
            <Text style={[styles.cellText]}>
                {vehicle ? (
                    <Text style={{
                        fontSize: 24,
                        textShadowColor: '#000000',
                        textShadowOffset: { width: 1, height: 1 },
                        textShadowRadius: 2,
                        color: '#FFFFFF'
                    }}>
                        {vehicleIcons[vehicle.vehicleType] || '❓'}
                    </Text>
                ) : (
                    <Text style={{
                        fontSize: 18,
                        color: '#CCCCCC',
                        textShadowColor: '#000000',
                        textShadowOffset: { width: 1, height: 1 },
                        textShadowRadius: 1
                    }}>━</Text>
                )}
            </Text>
        </TouchableOpacity>
    );
}



const ShowParks = () => {

    const { isAuthenticated} = useAuth();

    const [parksInfo, setParksInfo] = useState<ParksInfo | undefined>(undefined);


    const [selectedVehicles, setSelectedVehicles] = useState<VehicleInfoWithStatus[]>([]);
    const [modalVisible, setMoveVehiclesModalVisible] = useState(false);
    const [reportsModalVisible, setReportsModalVisible] = useState(false);
    const [aiReportModalVisible, setAiReportModalVisible] = useState(false);
    const [selectedVehicle, setSelectedVehicle] = useState<VehicleInfoWithStatus | null>(null);
    const [selectedPark, setSelectedPark] = useState<ParkInfo | null>(null);
    const [targetParkId, setTargetParkId] = useState<number | null>(null);
    const [notification, setNotification] = useState<{
        message: string;
        type: 'info' | 'success' | 'error'
    } | null>(null);
    const [aiReport, setAiReport] = useState<ReportAI | null>(null);
    const [, setVehicleMaintaining] = useState<VehicleInfoWithStatus | null>(null);
    const [isAddVehicleModalVisible, setAddVehicleModalVisible] = useState(false);
    const [selectedVehicleType, setSelectedVehicleType] = useState<number | null>(null);
    const [formattedReport, setFormattedReport] = useState<FormattedReport[] | null>(null);


    const [isLoading, setIsLoading] = useState(true);


    const fetchParks = useCallback(() => {
        setIsLoading(true);
        ParkManagerRequest.getParksInfo(isAuthenticated.token as string)
            .then(value => {
                if (!value || value instanceof Error) {
                    console.error('Errore: risposta non valida o errore ricevuto', value);
                    setParksInfo(undefined);
                } else {
                    setParksInfo(value);
                }
            })
            .catch(err => {
                console.error('Errore durante il fetch:', err);
                setParksInfo(undefined);
            })
            .finally(() => {
                setIsLoading(false);
            });
    }, [isAuthenticated.token]); // dipendenza token, se cambia ricalcola fetchParks

    useEffect(() => {
        fetchParks();
    }, [fetchParks]); // ora ESLint non darà warning

    if (isLoading) {
        return (
            <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
                <ActivityIndicator size="large" color="#00aaff" />
                <Text style={{ marginTop: 10 }}>Caricamento parcheggi...</Text>
            </View>
        );
    }



    const showNotification = (message: string, type: 'info' | 'success' | 'error' = 'info') => {
        setNotification({message, type});
        setTimeout(() => {
            setNotification(null);
        }, 3000);
    };



    const generateFormattedFeedbackReports = async (VehicleId: number | undefined) => {
        if (!VehicleId) {return;}

        const result = await ParkManagerRequest.getFormattedReports(VehicleId, isAuthenticated.token as string);

        if (result instanceof Error) {
            showNotification('Errore nella generazione dei report', 'error');
            setReportsModalVisible(false);
            return;
        }

        showNotification('Report AI generati con successo!', 'success');
        setFormattedReport(result);
    };



    const startVehicleMaintenance = async (VehicleId: number | undefined) => {
        if (!VehicleId || !parksInfo || !isAuthenticated?.token) {
            showNotification('Id mezzo non valido o dati parcheggio mancanti', 'error');
            return;
        }

        const parkWithVehicle = parksInfo.parks.find(park =>
            park.vehicles.some(vehicle => vehicle.id === VehicleId)
        );

        if (!parkWithVehicle) {
            showNotification('Mezzo non trovato', 'error');
            return;
        }

        const VehicleToMaintain = parkWithVehicle.vehicles.find(v => v.id === VehicleId);
        if (!VehicleToMaintain) {
            showNotification('Mezzo non trovato', 'error');
            return;
        }

        try {
            await ParkManagerRequest.setMaintainingVehicle(VehicleId, isAuthenticated.token);


            setVehicleMaintaining(VehicleToMaintain);
            showNotification(`Mezzo ${VehicleId} messo in manutenzione`, 'info');

            fetchParks(); // Ricarica i dati dei parcheggi

        } catch (error) {
            console.error('Errore durante la messa in manutenzione:', error);
            showNotification('Errore durante la messa in manutenzione', 'error');
        }
    };





    const handleVehiclePress = (vehicle: VehicleInfoWithStatus) => {
        if (selectedVehicles.length > 0) {
            handleVehicleLongPress(vehicle);
            return;
        }
        setSelectedVehicle(vehicle);
        setReportsModalVisible(true);
    };

    const handleVehicleLongPress = (vehicle: VehicleInfoWithStatus) => {
        setSelectedVehicles(prev => {
            const alreadySelected = prev.find(b => b.id === vehicle.id);
            if (alreadySelected) {
                return prev.filter(b => b.id !== vehicle.id);
            } else {
                return [...prev, vehicle];
            }
        });
    };



    const generateAIReport = async (park: ParkInfo) => {
        try {
            setSelectedPark(park);

            const result = await ParkManagerRequest.getReportAI(park.id, isAuthenticated.token as string);

            if (result instanceof Error) {
                console.error('Errore nel report AI:', result.message);
                return;
            }

            setAiReport(result);
            setAiReportModalVisible(true);
        } catch (error) {
            console.error('Errore durante la generazione del report AI:', error);
            return;
        }
    };

    const moveVehicles = async () => {
        if (!targetParkId || selectedVehicles.length === 0) {
            console.warn('Parcheggio non selezionato o nessun mezzo selezionato');
            return;
        }

        const vehicleIds = selectedVehicles.map(vehicle => vehicle.id);

        try {
            await ParkManagerRequest.moveVehiclesToPark(vehicleIds, targetParkId, isAuthenticated.token as string);
            setMoveVehiclesModalVisible(false); // chiudi il modal
            setTargetParkId(null);              // reset del Picker
            fetchParks();
            clearSelection();
        } catch (error) {
            console.error('Errore nello spostamento dei mezzi:', error);
        }
    };


    const handleAddNewVehicle = async (selectedParkId: number) => {
        if (!selectedVehicleType) {
            showNotification('Seleziona un tipo di mezzo', 'error');
            return;
        }

        try {
            const result = await ParkManagerRequest.getAddNewVehicle(
                selectedParkId,
                selectedVehicleType,
                isAuthenticated.token as string
            );

            if (result instanceof Error) {
                throw result;
            }

            setSelectedVehicleType(null);
            fetchParks(); // Ricarica i dati dei parcheggi
            showNotification('Nuovo mezzo aggiunto con successo!', 'success'); // ✅ Successo visibile

        } catch (err) {
            console.error("Errore durante l'aggiunta del mezzo:", err);
            showNotification("Errore durante l'aggiunta del mezzo", 'error');
            setSelectedVehicleType(null);
        }
    };




    const chunkArray = <T, >(arr: T[], size: number): T[][] => {
        const result: T[][] = [];
        for (let i = 0; i < arr.length; i += size) {
            result.push(arr.slice(i, i + size));
        }
        return result;
    };


    const clearSelection = () => {
        setSelectedVehicles([]);
    };



    return (
        <ScrollView contentContainerStyle={styles.container}>
            <Text style={styles.title}>Parcheggi e Disponibilità</Text>

            {/* Notification Toast */}
            {notification && (
                <View style={[
                    styles.notification,
                    (styles as any)[`notification${notification.type.charAt(0).toUpperCase() + notification.type.slice(1)}`],
                ]}>
                    <Text style={styles.notificationText}>{notification.message}</Text>
                </View>
            )}
            {parksInfo?.parks?.map((park) => (
                <View key={park.id} style={styles.parkContainer}>
                    <View style={styles.parkHeader}>
                        <Text style={styles.parkName}>{park.name}</Text>
                    </View>

                    <View style={styles.buttonsAI}>
                        <TouchableOpacity
                            onPress={() => {
                                setSelectedPark(park);
                                setAddVehicleModalVisible(true);
                            }}
                            style={styles.addButton}
                        >
                            <Text style={styles.addButtonText}>+ Aggiungi Mezzo</Text>
                        </TouchableOpacity>

                        <TouchableOpacity
                            onPress={() => generateAIReport(park)}
                            style={styles.reportsButton}
                        >
                            <Text style={styles.addButtonText}>Report AI</Text>
                        </TouchableOpacity>
                    </View>

                    <View style={styles.gridContainer}>
                        {chunkArray(park.docks.sort((a, b) => a.number - b.number), 5).map((row, rowIndex) => (
                            <View key={rowIndex} style={styles.row}>
                                {row.map((dock, colIndex) => (
                                    <Vehicle
                                        key={colIndex}
                                        vehicle={dock.idVehicle === undefined ? undefined : park.vehicles.find(v => v.id === dock.idVehicle)}                                        onPress={handleVehiclePress}
                                        onLongPress={handleVehicleLongPress}
                                        selected={selectedVehicles.some(b => b?.id === dock.idVehicle)}
                                    />
                                ))}
                            </View>
                        ))}
                    </View>
                </View>
            ))}

            {selectedVehicles.length > 0 && (
                <View style={styles.actionContainer}>
                    <TouchableOpacity
                        onPress={() => setMoveVehiclesModalVisible(true)}
                        style={styles.moveButton}
                    >
                        <Text style={styles.buttonText}>
                            Sposta ({selectedVehicles.length})
                        </Text>
                    </TouchableOpacity>
                    <TouchableOpacity
                        onPress={clearSelection}
                        style={styles.clearButton}
                    >
                        <Text style={styles.buttonText}>Deseleziona</Text>
                    </TouchableOpacity>
                </View>
            )}

            {/* Move Vehicles Modal */}
            <Modal
                visible={modalVisible}
                transparent={true}
                animationType="slide"
                onRequestClose={() => setMoveVehiclesModalVisible(false)}
            >
                <View style={styles.modalOverlay}>
                    <View style={styles.modalContainer}>
                        <Text style={styles.modalTitle}>Spostamento Mezzi</Text>

                        <Text style={styles.modalText}>
                            {selectedVehicles.length} mezzi selezionati:
                        </Text>
                        <Text style={styles.bikeList}>
                            {selectedVehicles.map(b => b.id).join(', ')}
                        </Text>

                        <Text style={styles.labelText}>Sposta in:</Text>
                        <View style={styles.pickerContainer}>
                            <Picker
                                selectedValue={targetParkId}
                                onValueChange={(itemValue) => setTargetParkId(itemValue)}
                                style={styles.picker}
                            >
                                <Picker.Item label="-- Seleziona un parcheggio --" value={null} />
                                {parksInfo?.parks?.map(p => (
                                    <Picker.Item
                                        key={p.id}
                                        label={`${p.name}`}
                                        value={p.id}
                                    />
                                ))}
                            </Picker>
                        </View>

                        <TouchableOpacity
                            onPress={moveVehicles}
                            style={[
                                styles.confirmButton2,
                                (!targetParkId) && styles.disabledButton,
                            ]}
                            disabled={!targetParkId}
                        >
                            <Text style={styles.buttonText}>Sposta</Text>
                        </TouchableOpacity>

                        <TouchableOpacity
                            onPress={() => {
                                setMoveVehiclesModalVisible(false);
                                setTargetParkId(null);
                            }}
                            style={styles.cancelButton2}
                        >
                            <Text style={styles.buttonText2}>Annulla</Text>
                        </TouchableOpacity>
                    </View>
                </View>
            </Modal>

            {/* Single Vehicle Reports Modal */}
            <Modal
                visible={reportsModalVisible}
                transparent={true}
                animationType="slide"
                onRequestClose={() => setReportsModalVisible(false)}
            >
                <View style={styles.modalOverlay}>
                    <View style={styles.reportsModalContainer}>
                        {!formattedReport || formattedReport.length === 0 ? (
                            <TouchableOpacity
                                onPress={() => {
                                    generateFormattedFeedbackReports(selectedVehicle?.id);
                                }}
                                style={styles.topRightButton}
                            >
                                <Text style={styles.topRightButtonText}>🤖</Text>
                            </TouchableOpacity>
                        ) : (
                            <TouchableOpacity
                                onPress={() => {
                                    setFormattedReport(null);
                                }}
                                style={styles.topRightButton}
                            >
                                <Text style={styles.topRightButtonText}>🔙</Text>
                            </TouchableOpacity>
                        )}
                        <Text style={styles.modalTitle}>
                            Segnalazioni veicolo {selectedVehicle?.id}
                        </Text>

                        <Text style={[
                            styles.bikeStatus,
                            {color: selectedVehicle?.status ? '#4CAF50' : '#F44336'},
                        ]}>
                            Stato: {selectedVehicle?.status ? 'Disponibile' : 'Non disponibile'}
                        </Text>

                        <ScrollView style={styles.reportsScrollView}>
                            {formattedReport && formattedReport.length > 0 ? (
                                formattedReport.map((report, index) => {
                                    const userReport = selectedVehicle?.customerReports?.[index];
                                    return (
                                        <View key={report.id ?? index} style={styles.reportCard}>

                                            {userReport && (
                                                <View style={styles.reportHeader}>
                                                    <View style={styles.headerLeftGroup}>
                                                        <View style={styles.reportTypeBadge}>
                                                            <Text style={styles.reportTypeText}>@{userReport.customer}</Text>
                                                        </View>
                                                        <View style={styles.sourceBadgeAI}>
                                                            <Text style={styles.sourceBadgeText}>AI</Text>
                                                        </View>
                                                    </View>
                                                    <Text style={styles.reportDate}>
                                                        {new Date(userReport.time).toLocaleDateString('it-IT')}
                                                    </Text>
                                                </View>
                                            )}
                                            <Text style={styles.reportRating}>Rating: {report.rating} / 3</Text>
                                            <View style={styles.bulletPointsContainer}>
                                                {report.bullet_points.map((point, i) => (
                                                    <Text key={i} style={styles.bulletPoint}>• {point}</Text>
                                                ))}
                                            </View>
                                        </View>
                                    );
                                })
                            ) : selectedVehicle?.customerReports && selectedVehicle.customerReports.length > 0 ? (
                                selectedVehicle.customerReports.map((report, index) => (
                                    <View key={index} style={styles.reportCard}>
                                        <View style={styles.reportHeader}>
                                            <View style={[styles.reportTypeBadge]}>
                                                <Text style={styles.reportTypeText}>@{report.customer}</Text>
                                            </View>
                                            <Text style={styles.reportDate}>
                                                {new Date(report.time).toLocaleDateString('it-IT')}
                                            </Text>
                                        </View>
                                        <Text style={styles.reportDescription}>{report.description}</Text>
                                    </View>
                                ))
                            ) : (
                                <View style={styles.noReportsContainer}>
                                    <Text style={styles.noReportsText}>Nessuna segnalazione per questo mezzo</Text>
                                </View>
                            )}
                        </ScrollView>



                        <TouchableOpacity
                            onPress={() => {
                                setReportsModalVisible(false);
                                setSelectedVehicle(null);
                                startVehicleMaintenance(selectedVehicle?.id);
                            }}
                            style={styles.maintainButton}
                        >
                            <Text style={styles.maintainButtonText}>Manda in manutenzione</Text>
                        </TouchableOpacity>

                        <TouchableOpacity
                            onPress={() => {
                                setReportsModalVisible(false);
                                setSelectedVehicle(null);
                                setFormattedReport(null);
                            }}
                            style={styles.closeReportsButton}
                        >
                            <Text style={styles.buttonText}>Chiudi</Text>
                        </TouchableOpacity>
                    </View>
                </View>
            </Modal>

            {/* Add Vehicle modal */}
            <Modal
                visible={isAddVehicleModalVisible}
                animationType="slide"
                transparent={true}
            >
                <View style={styles.modalOverlay}>
                    <View style={styles.modalContent}>
                        <Text style={styles.modalTitle}>Aggiungi un mezzo</Text>

                        <Text style={styles.labelText}>Tipo di mezzo:</Text>
                        <View style={styles.pickerContainer}>
                            <Picker
                                selectedValue={selectedVehicleType}
                                onValueChange={(itemValue) => setSelectedVehicleType(itemValue)}
                                style={styles.picker}
                            >
                                <Picker.Item label="-- Seleziona tipologia --" value={null} />
                                <Picker.Item label="Bici" value={1} />
                                <Picker.Item label="Bici elettrica" value={2} />
                                <Picker.Item label="Monopattino" value={3} />
                            </Picker>
                        </View>

                        <View style={styles.modalButtons}>
                            <TouchableOpacity onPress={() => {
                                setSelectedVehicleType(null);
                                setAddVehicleModalVisible(false);}}
                                              style={styles.cancelButton}>
                                <Text style={styles.cancelButtonText}>Annulla</Text>
                            </TouchableOpacity>

                            <TouchableOpacity
                                onPress={() => {
                                    if (!selectedPark?.id || !parksInfo) {
                                        setAddVehicleModalVisible(false);
                                        return;
                                    }
                                    handleAddNewVehicle(selectedPark.id);
                                    setAddVehicleModalVisible(false);
                                }}
                                style={styles.confirmButton}
                            >
                                <Text style={styles.confirmButtonText}>Aggiungi</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                </View>
            </Modal>

            {/* AI Report Modal */}
            <Modal
                visible={aiReportModalVisible}
                transparent={true}
                animationType="slide"
                onRequestClose={() => setAiReportModalVisible(false)}
            >
                <View style={styles.modalOverlay}>
                    <View style={styles.aiReportModalContainer}>
                        <Text style={styles.modalTitle}>
                            Report AI - {selectedPark?.name}
                        </Text>

                        {aiReport && (
                            <ScrollView style={styles.aiReportScrollView}>

                                {/* Stato del parcheggio */}
                                <View style={styles.sectionContainer}>
                                    <View
                                        style={[
                                            styles.statusBanner,
                                            aiReport.state === 1
                                                ? styles.statusOk
                                                : aiReport.state=== 2
                                                    ? styles.statusMedium
                                                    : styles.statusCritical,
                                        ]}
                                    >
                                        <Text style={styles.statusText}>
                                            {aiReport.state === 1 && 'Ottimo'}
                                            {aiReport.state === 2 && 'Da monitorare'}
                                            {aiReport.state === 3 && 'Critico'}
                                        </Text>
                                    </View>
                                </View>

                                {/* Riepilogo */}
                                <View style={styles.sectionContainer}>
                                    <Text style={styles.sectionTitle}>📊 Riepilogo Analisi</Text>
                                    <View style={styles.summaryContainer}>
                                        <Text style={styles.summaryText}>{aiReport.summary}</Text>
                                    </View>
                                </View>

                                {/* Priorità Interventi */}
                                <View style={styles.sectionContainer}>
                                    <Text style={styles.sectionTitle}>🔧 Biciclette Prioritarie</Text>
                                    <View>
                                        {aiReport.priorities.length > 0 ? (
                                            aiReport.priorities.map((bikeId) => (
                                                <Text key={bikeId}>• Bicicletta #{bikeId}</Text>
                                            ))
                                        ) : (
                                            <Text>Nessuna bici segnalata</Text>
                                        )}
                                    </View>
                                </View>

                                {/* Report ID */}
                                <View style={styles.reportIdContainer}>
                                    <Text style={styles.reportIdLabel}>🆔 ID Report:</Text>
                                    <Text style={styles.reportIdValue}>455b3j5h3bbmn3b5</Text>
                                </View>
                            </ScrollView>
                        )}

                        {/* Action Buttons */}
                        <View style={styles.aiReportButtonsContainer}>
                            <TouchableOpacity
                                onPress={() => {
                                    setAiReportModalVisible(false);
                                    setAiReport(null);
                                }}
                                style={styles.closeAiReportButton}
                            >
                                <Text style={styles.buttonText}>Chiudi</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                </View>
            </Modal>


        </ScrollView>
    );
};




export default ShowParks;
