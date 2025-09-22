import React from 'react';
import { View, Text, TouchableOpacity, Modal, StatusBar } from 'react-native';
import { park } from '../../Requests/HomeRequest';
import {styles} from '../../StyleSheets/parkingModalStyle.tsx';

type Props = {
  visible: boolean;
  parking: park | null;
  onClose: () => void;
};

const ParkingModal = ({ visible, parking, onClose }: Props) => {
  return (
      <Modal
          visible={visible}
          animationType="slide"
          transparent={true}
          onRequestClose={onClose}
          statusBarTranslucent={true}
      >
        <StatusBar backgroundColor="transparent" barStyle="light-content" />
        <View style={styles.overlay}>
          <View style={styles.modal}>
            {/* Header con icona */}
            <View style={styles.header}>
              <View style={styles.iconContainer}>
                <Text style={styles.iconText}>🅿️</Text>
              </View>
              <Text style={styles.title}>Dettagli Parcheggio</Text>
              <TouchableOpacity style={styles.closeButton} onPress={onClose}>
                <Text style={styles.closeButtonText}>✕</Text>
              </TouchableOpacity>
            </View>

            {/* Contenuto principale */}
            <View style={styles.content}>
              <View style={styles.infoCard}>
                <Text style={styles.cardTitle}>Informazioni Generali</Text>

                <View style={styles.infoRow}>
                  <View style={styles.labelContainer}>
                    <Text style={styles.labelIcon}>📍</Text>
                    <Text style={styles.label}>Nome</Text>
                  </View>
                  <Text style={styles.value}>{parking?.name ?? 'Non disponibile'}</Text>
                </View>

                <View style={styles.separator} />

                <View style={styles.infoRow}>
                  <View style={styles.labelContainer}>
                    <Text style={styles.labelIcon}>#️⃣</Text>
                    <Text style={styles.label}>ID Parcheggio</Text>
                  </View>
                  <Text style={styles.value}>{parking?.id ?? 'N/D'}</Text>
                </View>
              </View>

              {/* Card disponibilità mezzi */}
              <View style={styles.availabilityCard}>
                <Text style={styles.cardTitle}>Disponibilità Mezzi</Text>

                {parking ? (
                    <View style={styles.vehiclesContainer}>
                      <View style={styles.vehicleItem}>
                        <Text style={styles.vehicleIcon}>🚲</Text>
                        <Text style={styles.vehicleLabel}>Biciclette</Text>
                        <View style={styles.countBadge}>
                          <Text style={styles.countText}>{parking.bikeCount}</Text>
                        </View>
                      </View>

                      <View style={styles.vehicleItem}>
                        <Text style={styles.vehicleIcon}>⚡</Text>
                        <Text style={styles.vehicleLabel}>E-bike</Text>
                        <View style={styles.countBadge}>
                          <Text style={styles.countText}>{parking.ebikeCount}</Text>
                        </View>
                      </View>

                      <View style={styles.vehicleItem}>
                        <Text style={styles.vehicleIcon}>🛴</Text>
                        <Text style={styles.vehicleLabel}>Monopattini</Text>
                        <View style={styles.countBadge}>
                          <Text style={styles.countText}>{parking.scooterCount}</Text>
                        </View>
                      </View>
                    </View>
                ) : (
                    <Text style={styles.noDataText}>Dati non disponibili</Text>
                )}
              </View>
            </View>

            {/* Bottone di chiusura principale */}
            <TouchableOpacity
                style={styles.mainButton}
                onPress={onClose}
                activeOpacity={0.8}
            >
              <Text style={styles.mainButtonText}>Chiudi</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
  );
};

export default ParkingModal;

