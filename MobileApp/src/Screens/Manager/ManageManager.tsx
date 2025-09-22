import React, {useEffect, useState, useCallback, useMemo} from 'react';
import {
  ActivityIndicator,
  FlatList,
  Text,
  TextInput,
  View,
  TouchableOpacity,
  Modal,
  Button,
  Switch,
  Alert,
  ScrollView,
} from 'react-native';
import {toggleAdmin, getAdmins, addAdmin, removeManager} from '../../Requests/Manager.ts';
import {useAuth} from '../../Contexts/AuthContext';
import {styles} from '../../StyleSheets/manageManagerStyle.tsx';

export interface Manager {
  id: number;
  email: string;
  admin: boolean;
  parkCount: number;
}

interface AddManagerForm {
  email: string;
  password: string;
  isAdmin: boolean;
}

export const ManageManager = () => {
  const [managers, setManagers] = useState<Manager[]>([]);
  const [filter, setFilter] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [isDeleteModalVisible, setIsDeleteModalVisible] = useState(false);
  const [managerToDelete, setManagerToDelete] = useState<Manager | null>(null);
  const [selectedSubstitutorId, setSelectedSubstitutorId] = useState<number | null>(null);
  const [addManagerForm, setAddManagerForm] = useState<AddManagerForm>({
    email: '',
    password: '',
    isAdmin: false,
  });

  const {isAuthenticated} = useAuth();

  const fetchManagers = useCallback(async () => {
    if (!isAuthenticated.token) {return;}

    setIsLoading(true);
    try {
      const result = await getAdmins(isAuthenticated.token);
      if (result instanceof Error) {
        console.error('Errore nel caricamento manager:', result.message);
        Alert.alert('Errore', 'Impossibile caricare i manager');
      } else {
        setManagers(result);
      }
    } catch (error) {
      console.error('Errore nel caricamento manager:', error);
      Alert.alert('Errore', 'Errore di rete durante il caricamento');
    } finally {
      setIsLoading(false);
    }
  }, [isAuthenticated.token]);

  useEffect(() => {
    fetchManagers();
  }, [fetchManagers]);
  
  

  const filteredManagers = useMemo(() => {
    return managers.filter(manager =>
        manager.email.toLowerCase().includes(filter.toLowerCase())
    );
  }, [managers, filter]);

  const availableSubstitutors = useMemo(() => {
    if (!managerToDelete) return [];
    return managers.filter(m => m.id !== managerToDelete.id);
  }, [managers, managerToDelete]);

  const handleToggleAdmin = useCallback(async (managerId: number) => {
    if (!isAuthenticated.token) return;

    try {
      const result = await toggleAdmin(managerId, isAuthenticated.token);
      if (result instanceof Error) {
        console.error('Errore nel toggle admin:', result.message);
        Alert.alert('Errore', 'Impossibile modificare i permessi admin');
      } else {
        await fetchManagers();
        Alert.alert('Successo', 'Permessi admin aggiornati');
      }
    } catch (error) {
      console.error('Errore nel toggle admin:', error);
      Alert.alert('Errore', 'Errore durante la modifica dei permessi');
    }
  }, [isAuthenticated.token, fetchManagers]);

  const isValidEmail = (email: string): boolean => {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  };

  const handleAddManager = useCallback(async () => {
    if (!addManagerForm.email || !addManagerForm.password) {
      Alert.alert('Errore', 'Inserisci email e password');
      return;
    }

    if (!isValidEmail(addManagerForm.email)) {
      Alert.alert('Errore', 'Inserisci un indirizzo email valido');
      return;
    }

    if (!isAuthenticated.token) return;

    try {
      const result = await addAdmin(
          addManagerForm.email,
          addManagerForm.password,
          addManagerForm.isAdmin,
          isAuthenticated.token
      );

      if (result instanceof Error) {
        Alert.alert('Errore', result.message);
      } else {
        setIsModalVisible(false);
        setAddManagerForm({ email: '', password: '', isAdmin: false });
        await fetchManagers();
        Alert.alert('Successo', 'Manager aggiunto con successo');
      }
    } catch (error) {
      console.error('Errore aggiunta manager:', error);
      Alert.alert('Errore', 'Errore durante l\'aggiunta del manager');
    }
  }, [addManagerForm, isAuthenticated.token, fetchManagers]);

  const handleDeleteManager = useCallback((manager: Manager) => {
    setManagerToDelete(manager);
    setSelectedSubstitutorId(null);
    setIsDeleteModalVisible(true);
  }, []);

  const handleConfirmDelete = useCallback(async () => {
    if (!managerToDelete || selectedSubstitutorId === null || !isAuthenticated.token) {
      return;
    }

    try {
      const result = await removeManager(
          managerToDelete.id,
          selectedSubstitutorId,
          isAuthenticated.token
      );

      if (result instanceof Error) {
        Alert.alert('Errore', result.message);
      } else {
        setIsDeleteModalVisible(false);
        setSelectedSubstitutorId(null);
        setManagerToDelete(null);
        await fetchManagers();
        Alert.alert('Successo', 'Manager eliminato con successo');
      }
    } catch (error) {
      console.error('Errore eliminazione manager:', error);
      Alert.alert('Errore', 'Errore durante l\'eliminazione del manager');
    }
  }, [managerToDelete, selectedSubstitutorId, isAuthenticated.token, fetchManagers]);

  const handleCancelDelete = useCallback(() => {
    setIsDeleteModalVisible(false);
    setSelectedSubstitutorId(null);
    setManagerToDelete(null);
  }, []);

  const handleSelectSubstitutor = useCallback((managerId: number) => {
    setSelectedSubstitutorId(managerId);
  }, []);

  const updateAddManagerForm = useCallback((field: keyof AddManagerForm, value: string | boolean) => {
    setAddManagerForm(prev => ({...prev, [field]: value}));
  }, []);

  const renderSubstitutorItem = useCallback((manager: Manager) => {
    const isSelected = selectedSubstitutorId === manager.id;

    return (
        <TouchableOpacity
            key={manager.id}
            onPress={() => handleSelectSubstitutor(manager.id)}
            style={[
              styles.substitutorItem,
              isSelected && styles.substitutorItemSelected
            ]}
            activeOpacity={0.7}
        >
          <View style={styles.substitutorContent}>
            <Text style={[
              styles.substitutorEmail,
              isSelected && styles.substitutorEmailSelected
            ]}>
              {manager.email}
            </Text>
            <Text style={[
              styles.substitutorInfo,
              isSelected && styles.substitutorInfoSelected
            ]}>
              {manager.admin ? 'Admin' : 'Manager'} • {manager.parkCount} parcheggi
            </Text>
          </View>
          {isSelected && (
              <View style={styles.checkmark}>
                <Text style={styles.checkmarkText}>✓</Text>
              </View>
          )}
        </TouchableOpacity>
    );
  }, [selectedSubstitutorId, handleSelectSubstitutor]);

  const renderManagerItem = useCallback(({item}: {item: Manager}) => (
      <View style={styles.card}>
        <View style={styles.cardContent}>
          <View style={styles.info}>
            <Text style={styles.email}>{item.email}</Text>
            <Text style={styles.adminness}>Admin: {item.admin ? 'Sì' : 'No'}</Text>
            <Text style={styles.numParking}>Parcheggi gestiti: {item.parkCount}</Text>
          </View>
          <View style={styles.buttonContainer}>
            <TouchableOpacity
                style={styles.toggleButton}
                onPress={() => handleToggleAdmin(item.id)}
            >
              <Text style={styles.toggleButtonText}>Toggle Admin</Text>
            </TouchableOpacity>

            <TouchableOpacity
                onPress={() => handleDeleteManager(item)}
                style={styles.deleteButton}
            >
              <Text style={styles.deleteButtonText}>Elimina</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
  ), [handleToggleAdmin, handleDeleteManager]);

  if (isLoading) {
    return (
        <View style={styles.loadingContainer}>
          <ActivityIndicator size="large" color="#00aaff" />
          <Text style={{marginTop: 10}}>Caricamento manager...</Text>
        </View>
    );
  }

  return (
      <View style={styles.container}>
        <Text style={styles.title}>👔 Manager</Text>

        <TextInput
            placeholder="🔍 Cerca per email..."
            placeholderTextColor="#888"
            value={filter}
            onChangeText={setFilter}
            style={styles.input}
        />

        <TouchableOpacity
            onPress={() => setIsModalVisible(true)}
            style={styles.addButton}
        >
          <Text style={styles.addButtonText}>➕ Aggiungi Manager</Text>
        </TouchableOpacity>

        <FlatList
            data={filteredManagers}
            keyExtractor={item => (item?.id?.toString?.() || `key-${Math.random()}`)}
            contentContainerStyle={
              filteredManagers.length === 0 ? styles.emptyContainer : undefined
            }
            ListEmptyComponent={
              <Text style={styles.emptyText}>
                🚫 Nessun manager trovato.
              </Text>
            }
            renderItem={renderManagerItem}
            removeClippedSubviews={true}
            maxToRenderPerBatch={10}
            windowSize={10}
        />

        {/* Modal Aggiungi Manager */}
        <Modal visible={isModalVisible} animationType="slide" transparent>
          <View style={styles.modalOverlay}>
            <View style={styles.modalContent}>
              <Text style={styles.modalTitle}>Nuovo Manager</Text>

              <TextInput
                  placeholder="Email"
                  value={addManagerForm.email}
                  onChangeText={(text) => updateAddManagerForm('email', text)}
                  style={styles.input}
                  autoCapitalize="none"
                  keyboardType="email-address"
              />

              <TextInput
                  placeholder="Password"
                  value={addManagerForm.password}
                  onChangeText={(text) => updateAddManagerForm('password', text)}
                  secureTextEntry
                  style={styles.input}
              />

              <View style={styles.switchContainer}>
                <Text style={styles.switchLabel}>Admin:</Text>
                <Switch
                    value={addManagerForm.isAdmin}
                    onValueChange={(value) => updateAddManagerForm('isAdmin', value)}
                />
              </View>

              <View style={styles.modalButtons}>
                <Button
                    title="Annulla"
                    color="#ef4444"
                    onPress={() => {
                      setIsModalVisible(false);
                      setAddManagerForm({email: '', password: '', isAdmin: false});
                    }}
                />
                <Button
                    title="Aggiungi"
                    onPress={handleAddManager}
                />
              </View>
            </View>
          </View>
        </Modal>

        {/* Modal Eliminazione Manager */}
        <Modal visible={isDeleteModalVisible} transparent animationType="slide">
          <View style={styles.modalOverlay}>
            <View style={styles.deleteModalContent}>
              <Text style={styles.deleteModalTitle}>
                Elimina Manager
              </Text>

              {managerToDelete && (
                  <Text style={styles.deleteModalSubtitle}>
                    Stai per eliminare: {managerToDelete.email}
                  </Text>
              )}

              <Text style={styles.deleteModalDescription}>
                Seleziona un manager che si occuperà dei parcheggi attualmente gestiti da questo manager:
              </Text>

              <ScrollView
                  style={styles.substitutorsList}
                  showsVerticalScrollIndicator={true}
              >
                {availableSubstitutors.length === 0 ? (
                    <View style={{padding: 20, alignItems: 'center'}}>
                      <Text style={{color: '#64748b', fontSize: 16}}>
                        Nessun manager disponibile come sostituto
                      </Text>
                    </View>
                ) : (
                    availableSubstitutors.map(manager => renderSubstitutorItem(manager))
                )}
              </ScrollView>

              <View style={styles.deleteModalButtons}>
                <TouchableOpacity
                    style={styles.cancelButton}
                    onPress={handleCancelDelete}
                >
                  <Text style={styles.cancelButtonText}>Annulla</Text>
                </TouchableOpacity>

                <TouchableOpacity
                    style={[
                      styles.confirmButton,
                      selectedSubstitutorId === null && styles.confirmButtonDisabled
                    ]}
                    onPress={handleConfirmDelete}
                    disabled={selectedSubstitutorId === null}
                >
                  <Text style={[
                    styles.confirmButtonText,
                    selectedSubstitutorId === null && styles.confirmButtonTextDisabled
                  ]}>
                    Conferma
                  </Text>
                </TouchableOpacity>
              </View>
            </View>
          </View>
        </Modal>
      </View>
  );
};