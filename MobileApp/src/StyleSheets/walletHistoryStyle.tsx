import {StyleSheet} from 'react-native';


export const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#FAF8FF',
        padding: 24,
    },
    title: {
        fontSize: 22,
        fontWeight: 'bold',
        marginBottom: 16,
        color: '#333',
    },
    listContent: {
        paddingBottom: 24,
    },
    transaction: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        backgroundColor: '#fff',
        padding: 16,
        marginBottom: 12,
        borderRadius: 12,
        shadowColor: '#000',
        shadowOpacity: 0.05,
        shadowOffset: {width: 0, height: 2},
        shadowRadius: 4,
        elevation: 2,
    },
    transactionText: {
        flexDirection: 'column',
    },
    description: {
        fontSize: 16,
        fontWeight: '500',
        color: '#444',
    },
    date: {
        fontSize: 12,
        color: '#999',
    },
    amount: {
        fontSize: 16,
        fontWeight: 'bold',
        alignSelf: 'center',
    },
});
