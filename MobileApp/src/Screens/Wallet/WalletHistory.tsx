import React, {useEffect, useState} from 'react';
import {FlatList, Text, View} from 'react-native';
import ProfileRequests, {Payment} from '../../Requests/ProfileRequests';
import {useAuth} from '../../Contexts/AuthContext.tsx';
import { styles } from '../../StyleSheets/walletHistoryStyle.tsx';


const WalletHistory = () => {
  const {isAuthenticated} = useAuth();

  const [transactions, setTransactions] = useState<Payment[] | undefined>(
    undefined,
  );

  useEffect(() => {
    ProfileRequests.getCustomerPayments(
      isAuthenticated.username as string,
      isAuthenticated.token as string,
    ).then(it => setTransactions(it)).then(it => console.log(transactions));
  }, [isAuthenticated.token, isAuthenticated.username]);

  const renderItem = ({item}: {item: Payment | undefined}) => {
    return item != undefined ? (
        <View style={styles.transaction}>
          <View style={styles.transactionText}>
            <Text style={styles.description}>{
              (item as Payment).value > 0 ? 'Ricarica' : 'Pagamento'
            }</Text>
            <Text style={styles.date}>{new Date((item as Payment).time).toDateString()}</Text>
          </View>
          <Text
            style={[
              styles.amount,
              {color: (item as Payment).value > 0 ? '#4CAF50' : '#F44336'},
            ]}>
            €{Math.abs(item.value).toFixed(2)}
          </Text>
        </View>
      ) :
      (<Text>Caricamento</Text>)
  };
  return (
    <View style={styles.container}>
      <FlatList
        data={transactions}
        keyExtractor={item => item.id.toString()}
        renderItem={renderItem}
        contentContainerStyle={styles.listContent}
      />
    </View>
  );
};

export default WalletHistory;
