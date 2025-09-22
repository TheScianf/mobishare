import React, {useEffect, useState} from 'react';
import {
  Alert,
  Image,
  SafeAreaView,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import Icon from 'react-native-vector-icons/Feather';
import ProfileRequests from '../../Requests/ProfileRequests.ts';
import {useAuth} from '../../Contexts/AuthContext.tsx';
import {styles} from '../../StyleSheets/rechargeAmountStyle.tsx';

const RechargeAmountScreen = () => {
  const [selectedAmount, setSelectedAmount] = useState<number | null>(5);
  const [selectedPayment, setSelectedPayment] = useState<string | null>(null);
  const [customAmount, setCustomAmount] = useState<string>('');
  const {isAuthenticated} = useAuth();
  let [amounts, setAmounts] = useState([5, 10, 25, 50, 'Altro...']);

  useEffect(() => {
    ProfileRequests.getCustomerLastTransaction(
      isAuthenticated.username as string,
      isAuthenticated.token as string,
    ).then(last => {
      if(last !== 0 && !amounts.includes(last)){
        setAmounts([...amounts, last]);
      }
    });
  }, [amounts, isAuthenticated.token, isAuthenticated.username]);

  const handleAmountSelect = (amount: number | string) => {
    if (typeof amount === 'number') {
      setSelectedAmount(amount);
      setCustomAmount('');
    } else if (amount === 'Altro...') {
      setSelectedAmount(null);
    } else {
      setSelectedAmount(null);
    }
  };

  const renderPaymentMethodCard = (label: string, imageSource: any) => {
    const isSelected = selectedPayment === label;
    return (
      <TouchableOpacity
        key={label}
        style={[
          styles.paymentMethod,
          isSelected && styles.paymentMethodSelected,
        ]}
        onPress={() => setSelectedPayment(label)}>
        <Text style={styles.cardLabel}>{label}</Text>
        <Image
          source={imageSource}
          style={styles.cardLogo}
          resizeMode="contain"
        />
        {isSelected && (
          <View style={styles.checkmarkPayment}>
            <Icon name="check" size={14} color="#fff" />
          </View>
        )}
      </TouchableOpacity>
    );
  };

  const handleRecharge = async () => {
    const finalAmount = selectedAmount ?? parseFloat(customAmount);

    if (!finalAmount || isNaN(finalAmount)) {
      Alert.alert('Attenzione', 'Seleziona o inserisci un importo valido.');
      return;
    }

    if (!selectedPayment) {
      Alert.alert('Attenzione', 'Seleziona un metodo di pagamento.');
      return;
    }


    await ProfileRequests.rechargeWallet(isAuthenticated.username as string, finalAmount, isAuthenticated.token as string);
  };

  const isRechargeReady =
    (selectedAmount !== null ||
      (customAmount && !isNaN(parseFloat(customAmount)))) &&
    !!selectedPayment;

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.grid}>
        {amounts.map((amount, index) => {
          const isSelected =
            selectedAmount === amount ||
            (amount === 'Altro...' && selectedAmount === null);
          const isCustom = amount === 'Altro...';

          return (
            <TouchableOpacity
              key={index}
              style={[styles.amountBox, isSelected && styles.amountBoxSelected]}
              onPress={() => handleAmountSelect(amount)}
              activeOpacity={isCustom ? 1 : 0.7}>
              {isCustom && selectedAmount === null ? (
                <View style={{alignItems: 'center'}}>
                  <Text
                    style={{
                      marginBottom: 4,
                      color: '#4B2E6B',
                      fontWeight: '600',
                    }}>
                    Altro
                  </Text>
                  <TextInput
                    placeholder="€"
                    value={customAmount}
                    onChangeText={setCustomAmount}
                    keyboardType="numeric"
                    style={styles.inputCustomAmount}
                  />
                </View>
              ) : (
                <Text style={styles.amountText}>
                  {typeof amount === 'number' ? `${amount}` : amount}
                </Text>
              )}

              {isSelected && (
                <View style={styles.checkmark}>
                  <Icon name="check" size={14} color="#fff" />
                </View>
              )}
            </TouchableOpacity>
          );
        })}
      </View>

      <View style={styles.divider}></View>

      <View style={styles.paymentContainer}>
        {renderPaymentMethodCard('MasterCard', {
          uri: 'https://upload.wikimedia.org/wikipedia/commons/0/04/Mastercard-logo.png',
        })}
        {renderPaymentMethodCard('Visa', require('../../../assets/visa.png'))}
      </View>

      <TouchableOpacity
        style={[styles.button, !isRechargeReady && {backgroundColor: '#ccc'}]}
        onPress={handleRecharge}
        disabled={!isRechargeReady}>
        <Text style={styles.buttonText}>Ricarica</Text>
      </TouchableOpacity>
    </SafeAreaView>
  );
};

export default RechargeAmountScreen;
