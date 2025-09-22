import React, {useEffect, useState} from 'react';
import {
  Image,
  SafeAreaView,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import {useNavigation} from '@react-navigation/native';
import {useAuth} from '../../Contexts/AuthContext';
import ProfileRequests from '../../Requests/ProfileRequests.ts';
import { ScrollView, RefreshControl } from 'react-native';
import {styles} from '../../StyleSheets/walletStyle.tsx';

const Wallet = () => {
  const {isAuthenticated} = useAuth();

  const [balance, setBalance] = useState(0);
  const [greenPoints, setGreenPoints] = useState(0);
  const navigation: any = useNavigation();
  const [refreshing, setRefreshing] = useState(false);

  useEffect(() => {
    const fn = async () => {
      const credit = await ProfileRequests.getCustomerCredit(
        isAuthenticated.username as string,
        isAuthenticated.token as string,
      );
      setBalance(credit);
    };
    fn();
  }, [isAuthenticated.token, isAuthenticated.username]);

  useEffect(() => {
    const fn = async () => {
      const points = await ProfileRequests.getCustomerGreenPoints(
        isAuthenticated.username as string,
        isAuthenticated.token as string,
      );
      setGreenPoints(points);
    };
    fn();
  }, [isAuthenticated.token, isAuthenticated.username]);

  const onRefresh = async () => {
    setRefreshing(true);
    try {
      const credit = await ProfileRequests.getCustomerCredit(
          isAuthenticated.username as string,
          isAuthenticated.token as string,
      );
      setBalance(credit);

      const points = await ProfileRequests.getCustomerGreenPoints(
          isAuthenticated.username as string,
          isAuthenticated.token as string,
      );
      setGreenPoints(points);
    } catch (error) {
      console.warn('Errore durante il refresh', error);
    } finally {
      setRefreshing(false);
    }
  };

  return (
      <SafeAreaView style={styles.container}>
        <ScrollView
            contentContainerStyle={{ paddingBottom: 30 }}
            refreshControl={
              <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
            }
        >
          <Text style={styles.title}>Il mio borsellino</Text>

      <View style={styles.walletCard}>
        <View style={styles.walletCardBackground} />

        <View style={styles.walletCardContent}>
          <Image
            source={require('../../../assets/wallet.png')}
            style={styles.walletImage}
          />

          <View style={styles.balanceContent}>
            <Text style={styles.walletLabel}>Saldo attuale</Text>
            <Text style={styles.balanceAmount}>$ {balance.toFixed(2)}</Text>
            <TouchableOpacity
              style={styles.rechargeButton}
              onPress={() => navigation.navigate('RechargeAmountScreen')}>
              <Text style={styles.rechargeButtonText}>Ricarica</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>

      {/* Green Points Section */}
      <View style={styles.greenPointsCard}>
        <View style={styles.greenPointsContent}>
          <View style={styles.greenPointsLeft}>
            <View style={styles.leafIconContainer}>
              <Icon name="leaf" size={24} color="#22C55E" />
            </View>
            <View style={styles.greenPointsInfo}>
              <Text style={styles.greenPointsLabel}>Punti Green</Text>
              <Text style={styles.greenPointsSubtext}>
                Premio per essere eco-friendly
              </Text>
            </View>
          </View>

          {/* Separatore verticale */}
          <View style={styles.separator} />

          <View style={styles.greenPointsRight}>
            <Text style={styles.greenPointsAmount}>{greenPoints}</Text>
          </View>
        </View>
      </View>

      <View style={styles.menuContainer}>
        <TouchableOpacity
          style={styles.menuItem}
          onPress={() => navigation.navigate('WalletHistoryScreen')}>
          <View style={styles.menuItemLeft}>
            <Icon
              name="time-outline"
              size={22}
              color="#4B0055"
              style={{marginRight: 12}}
            />
            <Text style={styles.menuItemText}>Cronologia borsellino</Text>
          </View>
          <Icon name="chevron-forward" size={20} color="#aaa" />
        </TouchableOpacity>
      </View>
  </ScrollView>
  </SafeAreaView>
  );
};

export default Wallet;
