import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  Image,
  Modal,
  SafeAreaView,
  Alert,
  TextInput,
  ScrollView,
} from 'react-native';
import { RouteProp, useRoute } from '@react-navigation/native';
import RaceRequest from '../../Requests/RaceRequest.ts';
import {AuthEntity, useAuth} from '../../Contexts/AuthContext.tsx';
import {styles} from '../../StyleSheets/corsaStartStyle.tsx';

type VehicleDataType = {
  id?: number;
  constantPrice?: number;
  minutePrice?: number;
  vehicleType?: string;
};

type DatiCorsa = {
  mezzo: string;
  tempo: string;
  co2: string;
  prezzo: string;
};

// Definisci il tipo dei parametri passati alla schermata
type CorsaStartRouteProp = RouteProp<
    { CorsaStartScreen: { vehicleData?: VehicleDataType } },
    'CorsaStartScreen'
>;

const CorsaStart = () => {
  // Specifica il tipo di route con vehicleData opzionale
  const route = useRoute<CorsaStartRouteProp>();
  const vehicleData = route.params?.vehicleData;

  // Usa valori di default in caso vehicleData sia undefined
  const COSTO_BASE = vehicleData?.constantPrice ?? 0.99;
  const COSTO_MINUTO = vehicleData?.minutePrice ?? 0.05;
  const mezzoNome = vehicleData?.vehicleType ?? 'Undefined';
  const { isAuthenticated } = useAuth();
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const [role, setRole] = useState(isAuthenticated.role);

  const [corsaAttiva, setCorsaAttiva] = useState(false);
  const [modalVisibile, setModalVisibile] = useState(false);
  const [corsaTerminata, setCorsaTerminata] = useState(false);
  const [datiFinali, setDatiFinali] = useState<DatiCorsa | null>(null);
  const [secondiTrascorsi, setSecondiTrascorsi] = useState(0);
  const [timerID, setTimerID] = useState<NodeJS.Timeout | null>(null);
  const [startRaceTime, setStartRaceTime] = useState<string | null>(null);
  const [showFeedbackForm, setShowFeedbackForm] = useState(false);
  const [feedbackText, setFeedbackText] = useState('');
  const CO2_RISPARMIATA_AL_MINUTO = 10;

  const getDatiCorsa = (): DatiCorsa => {
    if (corsaAttiva) {
      const minuti = Math.floor(secondiTrascorsi / 60);
      const secondi = secondiTrascorsi % 60;
      return {
        mezzo: mezzoNome,
        tempo: `${minuti} min ${secondi} sec`,
        co2: `+${CO2_RISPARMIATA_AL_MINUTO * minuti}g rispetto a un'auto`,
        prezzo: `€${(COSTO_BASE + COSTO_MINUTO * minuti).toFixed(2)}`,
      };
    } else if (corsaTerminata && datiFinali) {
      return datiFinali;
    } else {
      return {
        mezzo: mezzoNome,
        tempo: '0 min 0 sec',
        co2: "+0g rispetto a un'auto",
        prezzo: `€${COSTO_BASE.toFixed(2)}`,
      };
    }
  };

  const datiCorsa = getDatiCorsa();

  useEffect(() => {
    return () => {
      if (timerID) {
        clearInterval(timerID);
      }
    };
  }, [timerID]);

  const handlePulsante = () => {
    if (corsaAttiva) {
      terminaCorsa();
    } else {
      avviaCorsa();
    }
  };

  const avviaCorsa = async () => {
    try {
      const inizio = await RaceRequest.startRace(isAuthenticated.username as string, vehicleData?.id as number, isAuthenticated.token as string);
      if(inizio instanceof Error) {
        throw inizio;
      }
      setStartRaceTime(inizio);
      setSecondiTrascorsi(0);
      const id = setInterval(() => {
        setSecondiTrascorsi((prev) => prev + 1);
      }, 1000);
      setTimerID(id);
      setCorsaAttiva(true);
      setCorsaTerminata(false);
    } catch (error) {
      Alert.alert('Errore', 'Impossibile avviare la corsa. Riprova più tardi.', [{ text: 'OK' }]);
    }
  };

  const terminaCorsa = async () => {
    try {
      if (timerID) {
        clearInterval(timerID);
        setTimerID(null);
      }

      const response = await RaceRequest.endRace(
          isAuthenticated.username as string,
          vehicleData?.id as number,
          startRaceTime as string,
          isAuthenticated.token as string
      );

      // Gestione risposte personalizzate
      if (response === 'SOSPESO') {
        setRole(AuthEntity.SUSPENDED);
        return;
      } else if (response === 'GIÀ_PARCHEGGIATO' || response === 'DOCK_MANCANTE') {
        setModalVisibile(true);
        return;
      } else if (response instanceof Error) {
        console.warn(response.message);
        throw response;
      }

      // Continua con il flusso normale
      const minuti = Math.floor(secondiTrascorsi / 60);
      const secondi = secondiTrascorsi % 60;
      const tempoStr = `${minuti} min ${secondi} sec`;

      const prezzo = (COSTO_MINUTO * minuti + COSTO_BASE).toFixed(2);
      const co2 = `+${CO2_RISPARMIATA_AL_MINUTO * minuti}g rispetto a un'auto`;

      setDatiFinali({
        mezzo: mezzoNome,
        tempo: tempoStr,
        co2,
        prezzo: `€${prezzo}`,
      });

      chiudiModal();
      setCorsaAttiva(false);
      setCorsaTerminata(true);

    } catch (error) {
      Alert.alert('Errore', "Impossibile terminare la corsa. Contatta l'assistenza.", [{ text: 'OK' }]);
    }
  };

  const chiudiModal = () => {
    setModalVisibile(false);
  };

  const handleSendFeedback = async () => {
    if (feedbackText.trim().length === 0) {
      Alert.alert('Attenzione', 'Inserisci un testo per il feedback.');
      return;
    }

    const result = await RaceRequest.sendFeedback(
        isAuthenticated.username as string,
        feedbackText,
        vehicleData?.id as number,
        isAuthenticated.token as string
    );

    if (result instanceof Error) {
      Alert.alert('Errore', result.message || 'Impossibile inviare il feedback. Riprova più tardi.');
      return;
    }

    Alert.alert('Grazie!', 'Il tuo feedback è stato inviato.');
    setFeedbackText('');
    setShowFeedbackForm(false);
  };

  const bikeImagePlaceholder = require('../../../assets/logo.png');

  return (
      <SafeAreaView style={styles.container}>
        <ScrollView contentContainerStyle={styles.scrollContainer} showsVerticalScrollIndicator={false}>
          <Text style={styles.header}>🌱 Corsa Sostenibile</Text>

          <Image source={bikeImagePlaceholder} defaultSource={bikeImagePlaceholder} style={styles.bikeImage} />

          <View style={styles.infoBox}>
            <InfoRow label="🚲 Mezzo:" value={datiCorsa.mezzo} />
            <InfoRow label="⏱ Tempo trascorso:" value={datiCorsa.tempo} />
            <InfoRow label="🌍 CO₂ risparmiata:" value={datiCorsa.co2} />
            <InfoRow label="💵 Importo:" value={datiCorsa.prezzo} />
          </View>

          {!corsaTerminata ? (
              <TouchableOpacity
                  style={[styles.button, corsaAttiva && styles.buttonRed]}
                  onPress={handlePulsante}
                  activeOpacity={0.7}
              >
                <Text style={styles.buttonText}>{corsaAttiva ? 'Termina Corsa' : 'Avvia la Corsa'}</Text>
              </TouchableOpacity>
          ) : (
              <View style={styles.riepilogoContainer}>
                <Text style={styles.riassunto}>📋 Riepilogo Corsa</Text>
                <Text style={styles.riepilogoDesc}>
                  Il pagamento verrà effettuato automaticamente se il saldo del tuo account è sufficiente
                </Text>

                <View style={styles.feedbackSection}>
                  {!showFeedbackForm ? (
                      <TouchableOpacity
                          style={styles.feedbackButton}
                          onPress={() => setShowFeedbackForm(true)}
                          activeOpacity={0.7}
                      >
                        <Text style={styles.feedbackButtonText}>
                          💬 Hai qualcosa da segnalare? Lascia un feedback
                        </Text>
                      </TouchableOpacity>
                  ) : (
                      <View style={styles.feedbackContainer}>
                        <Text style={styles.feedbackTitle}>📝 Lascia il tuo feedback</Text>
                        <TextInput
                            style={styles.feedbackInput}
                            multiline
                            numberOfLines={4}
                            placeholder="Scrivi qui il tuo feedback..."
                            value={feedbackText}
                            onChangeText={setFeedbackText}
                            textAlignVertical="top"
                            maxLength={500}
                        />
                        <Text style={styles.characterCount}>
                          {feedbackText.length}/300 caratteri
                        </Text>

                        <View style={styles.feedbackButtons}>
                          <TouchableOpacity
                              style={styles.cancelFeedbackButton}
                              onPress={() => {
                                setShowFeedbackForm(false);
                                setFeedbackText('');
                              }}
                              activeOpacity={0.7}
                          >
                            <Text style={styles.cancelFeedbackButtonText}>Annulla</Text>
                          </TouchableOpacity>

                          <TouchableOpacity
                              style={styles.sendFeedbackButton}
                              onPress={handleSendFeedback}
                              activeOpacity={0.7}
                          >
                            <Text style={styles.sendFeedbackButtonText}>Invia feedback</Text>
                          </TouchableOpacity>
                        </View>
                      </View>
                  )}
                </View>
              </View>
          )}
        </ScrollView>

        <Modal visible={modalVisibile} transparent animationType="fade" onRequestClose={chiudiModal}>
          <View style={styles.modalOverlay}>
            <View style={styles.modalBox}>
              <Text style={styles.modalTitle}>❌ Non hai collegato il mezzo alla stazione!</Text>
              <Text style={styles.modalText}>Collega il mezzo alla stazione e premi OK!</Text>
              <TouchableOpacity style={styles.modalButton} onPress={terminaCorsa} activeOpacity={0.7}>
                <Text style={styles.modalButtonText}>OK</Text>
              </TouchableOpacity>
            </View>
          </View>
        </Modal>
      </SafeAreaView>
  );
};

const InfoRow = ({ label, value }: { label: string; value: string }) => (
    <View style={{ marginBottom: 10 }}>
      <Text style={styles.label}>{label}</Text>
      <Text style={styles.value}>{value}</Text>
    </View>
);

export default CorsaStart;
