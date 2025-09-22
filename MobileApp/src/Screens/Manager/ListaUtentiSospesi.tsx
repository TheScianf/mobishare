import React, {useEffect, useState} from 'react';
import {ActivityIndicator, FlatList, Text, TextInput, View} from 'react-native';
import {getAllSuspendedUsers} from '../../Requests/Manager.ts';
import {useAuth} from '../../Contexts/AuthContext.tsx';
import {styles} from '../../StyleSheets/listaUtentiSospesiStyle.tsx';

export interface User {
  username: string;
  name: string;
  email: string;
  suspendedAt: string;
  toPay: string;
}

export interface Suspension {
  suspendedAt: string;
  toPay: string;
}

export default function SuspendedUsersScreen() {
  const [users, setUsers] = useState<User[] | undefined>(undefined);
  const [filter, setFilter] = useState('');
  const {isAuthenticated} = useAuth();
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setIsLoading(true);
    getAllSuspendedUsers(isAuthenticated.token as string)
      .then(value => setUsers(value))
      .catch(() => []).finally(() =>  setIsLoading(false));
  }, [isAuthenticated.token]);

  const filteredUsers =
    users !== undefined
      ? users.filter(
        user =>
          user.name.toLowerCase().includes(filter.toLowerCase()) ||
          user.email.toLowerCase().includes(filter.toLowerCase()),
      )
      : undefined;

  if (isLoading) {
    return (
        <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
          <ActivityIndicator size="large" color="#00aaff" />
          <Text style={{ marginTop: 10 }}>Caricamento utenti...</Text>
        </View>
    );
  }

  return filteredUsers !== undefined ? (
    <View style={styles.container}>
      <Text style={styles.title}>🔒 Utenti Sospesi</Text>

      <TextInput
        placeholder="🔍 Cerca per nome o email..."
        placeholderTextColor="#888" // casinò
        value={filter}
        onChangeText={setFilter}
        style={styles.input}
      />

      <FlatList
        data={filteredUsers}
        keyExtractor={item => item.username}
        contentContainerStyle={
          (filteredUsers as User[]).length === 0 && styles.emptyContainer
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
                {item.toPay !== '0€' ? (
                  <Text style={styles.toPay}>💰 Da pagare: {Math.abs(Number(item.toPay.replace('€', '').replace(',', '.')))}€</Text>
                ) : (
                  <Text style={styles.toPay}>💰 Saldato</Text>
                )}
              </View>
            </View>
          </View>
        )}
      />
    </View>
  ) : (
    <Text>Loading...</Text>
  );
}
