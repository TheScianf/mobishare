import React, {useEffect, useState} from 'react';
import {
  ActivityIndicator,
  ScrollView,
  Text,
  View,
} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import {AuthEntity} from '../../Contexts/AuthContext.tsx';
import PersonalInfoRequests, {CustomerInfo} from '../../Requests/PersonalInfoRequests.ts';
import {styles} from '../../StyleSheets/personalInfo.tsx';

const PersonalInfo = async ({
  isAuthenticated,
}: {
  isAuthenticated: AuthEntity;
}) => {
  const [userInfo, setUserInfo] = useState<CustomerInfo | undefined>(undefined);

  useEffect(() => {
    PersonalInfoRequests.getCustomerInfo(
      isAuthenticated.username as string,
      isAuthenticated.token as string,
    ).then(value => setUserInfo(value));
  }, [isAuthenticated.token, isAuthenticated.username]);

  if(userInfo !== undefined) {
    return (
      <ScrollView contentContainerStyle={styles.container}>
        <View style={styles.infoBox}>
          <Icon name="person-circle-outline" size={20} color="#4B0055" />
          <Text style={styles.label}>Nome e Cognome</Text>
          <Text style={styles.value}>
            {userInfo.name + ' ' + userInfo.surname}
          </Text>
        </View>

        <View style={styles.infoBox}>
          <Icon name="at-outline" size={20} color="#4B0055" />
          <Text style={styles.label}>Username</Text>
          <Text style={styles.value}>
            {userInfo.username}
          </Text>
        </View>

        <View style={styles.infoBox}>
          <Icon name="card-outline" size={20} color="#4B0055" />
          <Text style={styles.label}>Codice Fiscale</Text>
          <Text style={styles.value}>
            {userInfo.cf}
          </Text>
        </View>

        <View style={styles.infoBox}>
          <Icon name="accessibility-outline" size={20} color="#4B0055" />
          <Text style={styles.label}>Genere</Text>
          <Text style={styles.value}>
            {userInfo.gender}
          </Text>
        </View>

        <View style={styles.infoBox}>
          <Icon name="mail-outline" size={20} color="#4B0055" />
          <Text style={styles.label}>Email</Text>
          <Text style={styles.value}>{userInfo.email}</Text>
        </View>
      </ScrollView>
    );
  } else {
    return (
        <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
          <ActivityIndicator size="large" color="#00aaff" />
          <Text style={{ marginTop: 10 }}>Caricamento informazioni...</Text>
        </View>
    );
  }
};
export default PersonalInfo;
