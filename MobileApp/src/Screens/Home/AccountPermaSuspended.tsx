import React from 'react';
import { View, Text } from 'react-native';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import {styles} from '../../StyleSheets/accountSospesoStyle.tsx';

const AccountPermaSuspendedScreen = () => {

    return (
        <View style={styles.container}>
            <MaterialIcons name="report-problem" size={100} color="#ff6b6b" />
            <Text style={styles.title}>Account sospeso</Text>
            <Text style={styles.message}>
                Il tuo account è stato permanentemente sospeso per violazioni delle linee guida.
            </Text>
        </View>
    );
};

export default AccountPermaSuspendedScreen;
