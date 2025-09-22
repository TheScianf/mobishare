import React, {useCallback, useEffect, useState} from 'react';
import {ActivityIndicator, FlatList, Text, TextInput, TouchableOpacity, View} from 'react-native';
import {useAuth} from '../../Contexts/AuthContext.tsx';
import {getAllSuspendedUsers, setUserLastSuspensionState, getChrono} from '../../Requests/Manager';
import {styles} from '../../StyleSheets/riattivaUtentiSospesiStyle.tsx';

type User = {
  username: string;
  name: string;
  email: string;
  suspendedAt: string;
  toPay: string;
};

type Suspension = {
  suspendedAt: string;
  toPay: string;
};

export default function ReactivateSuspendedUsers() {
  const [users, setUsers] = useState<User[]>([]);
  const [filter, setFilter] = useState('');
  const {isAuthenticated} = useAuth();
  const [isLoading, setIsLoading] = useState(true);
  const [isModalVisible, setModalVisible] = useState(false);
  const [selectedUserChrono, setSelectedUserChrono] = useState<Suspension[] | null>(null);
  const [selectedUsername, setSelectedUsername] = useState<string | null>(null);

  const reload = useCallback(async () => {
    try {
      const result = await getAllSuspendedUsers(isAuthenticated.token as string);
      setUsers(result.filter(user => user.toPay === '0€'));
    } catch (err: any) {
      setUsers([]);
    }
  }, [isAuthenticated.token]);


  useEffect(() => {
    setIsLoading(true);
    reload().then().finally(() => setIsLoading(false));
  }, [reload]);

  const filteredUsers = users.filter(
    user =>
      user.name.toLowerCase().includes(filter.toLowerCase()) ||
      user.email.toLowerCase().includes(filter.toLowerCase()),
  );

  //
  const reactivateUser = async (username: string) => {
    await setUserLastSuspensionState(
      isAuthenticated.token as string,
      username,
      'accepted',
    );
    await reload();
  };

  const rejectUser = async (username: string) => {
    await setUserLastSuspensionState(
      isAuthenticated.token as string,
      username,
      'rejected',
    );
    await reload();
  };

  const getChronology = async (username: string) => {
    try {
      const result = await getChrono(username, isAuthenticated.token as string);

      if (!(result instanceof Error)) {
        setSelectedUsername(username);
        setSelectedUserChrono(result);
      } else {
        console.warn('Errore nel recupero dati, uso dati fake.');
        setSelectedUsername(username);
        setSelectedUserChrono([
          { suspendedAt: '2024-10-01', toPay: '5€' },
          { suspendedAt: '2023-12-15', toPay: '0€' },
        ]);
      }
    } catch (e) {
      console.error('Errore grave nella fetch, uso fallback.', e);
      setSelectedUsername(username);
      setSelectedUserChrono([
        { suspendedAt: '2024-10-01', toPay: '5€' },
        { suspendedAt: '2023-12-15', toPay: '0€' },
      ]);
    } finally {
      setModalVisible(true);
    }
  };

  if (isLoading) {
    return (
        <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
          <ActivityIndicator size="large" color="#00aaff" />
          <Text style={{ marginTop: 10 }}>Caricamento utenti...</Text>
        </View>
    );
  }

  return (
    <View style={styles.container}>
      <Text style={styles.title}>🔒 Utenti Sospesi</Text>

      <TextInput
        placeholder="🔍 Cerca per nome o email..."
        placeholderTextColor="#888"
        value={filter}
        onChangeText={setFilter}
        style={styles.input}
      />

      <FlatList
        data={filteredUsers}
        keyExtractor={item => item.username}
        contentContainerStyle={
          filteredUsers.length === 0 && styles.emptyContainer
        }
        ListEmptyComponent={
          <Text style={styles.emptyText}>
            🚫 Nessun utente sospeso trovato.
          </Text>
        }
        renderItem={({item}) => (
          <View style={styles.card}>
            <View style={styles.cardContent}>
              <View style={styles.info}>
                <Text style={styles.name}>{item.name}</Text>
                <Text style={styles.email}>{item.email}</Text>
                <Text style={styles.date}>Sospeso dal: {item.suspendedAt}</Text>
              </View>
              <View style={styles.actions}>
                <TouchableOpacity
                  onPress={() => reactivateUser(item.username)}
                  style={styles.button}
                  activeOpacity={0.8}>
                  <Text style={styles.buttonText}>✔ Riattiva</Text>
                </TouchableOpacity>
                <TouchableOpacity
                  onPress={() => rejectUser(item.username)}
                  style={styles.buttonReject}
                  activeOpacity={0.8}>
                  <Text style={styles.buttonText}>𝐗 Rifiuta</Text>
                </TouchableOpacity>
              </View>
            </View>
          </View>
        )}
      />
      {isModalVisible && (
          <View style={styles.modalContainer}>
            <View style={styles.modalContent}>
              <Text style={styles.modalTitle}>
                🕒 Storico sospensioni: {selectedUsername}
              </Text>

              {selectedUserChrono && selectedUserChrono.length > 0 ? (
                  <FlatList
                      data={selectedUserChrono}
                      keyExtractor={(_, index) => index.toString()}
                      renderItem={({ item }) => (
                          <View style={styles.modalItem}>
                            <Text style={styles.modalText}>📅 Sospeso il: {item.suspendedAt}</Text>
                            <Text style={styles.modalText}>💰 Da pagare: {item.toPay}</Text>
                          </View>
                      )}
                  />
              ) : (
                  <Text style={styles.modalText}>Nessuna sospensione trovata.</Text>
              )}

              <TouchableOpacity
                  onPress={() => setModalVisible(false)}
                  style={styles.modalCloseButton}
              >
                <Text style={styles.buttonText}>Chiudi</Text>
              </TouchableOpacity>
            </View>
          </View>
      )}
    </View>
  );
}
