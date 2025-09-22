import React from 'react';
import { View, Text, Pressable } from 'react-native';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import { useNavigation } from '@react-navigation/core';
import {styles} from '../../StyleSheets/accountSospesoStyle.tsx';

const AccountSuspendedScreen = () => {

const navigation: any = useNavigation();

  return (
    <View style={styles.container}>
      <MaterialIcons name="report-problem" size={100} color="#ff6b6b" />
      <Text style={styles.title}>Account sospeso</Text>
      <Text style={styles.message}>
        Il tuo account è stato temporaneamente sospeso per violazioni delle linee guida.
      </Text>
      <Pressable
        style={styles.button}
        onPress={() => {
          navigation.navigate('RechargeWallet');
        }}
      >
        <Text style={styles.buttonText}>PAGA</Text>
      </Pressable>
    </View>
  );
};

export default AccountSuspendedScreen;
