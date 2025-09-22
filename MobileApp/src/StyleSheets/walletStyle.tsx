import {StyleSheet} from 'react-native';


export const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#FAF8FF',
        paddingTop: 40,
        paddingHorizontal: 20,
    },
    title: {
        fontSize: 24,
        color: '#4B0055',
        fontWeight: '700',
        marginBottom: 20,
    },
    walletCard: {
        backgroundColor: '#fff',
        borderRadius: 20,
        padding: 20,
        marginBottom: 20,
        shadowColor: '#000',
        shadowOpacity: 0.05,
        shadowRadius: 5,
        shadowOffset: {width: 0, height: 2},
        elevation: 3,
    },
    walletCardBackground: {
        ...StyleSheet.absoluteFillObject,
        borderRadius: 20,
        backgroundColor: '#00BFFF',
    },
    walletCardContent: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    walletImage: {
        width: 150,
        height: 150,
        resizeMode: 'contain',
        marginLeft: 5,
    },
    balanceContent: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'flex-end',
        marginRight: 20,
    },
    walletLabel: {
        fontSize: 16,
        color: '#1a365d',
        fontWeight: '600',
        marginBottom: 4,
    },
    balanceAmount: {
        fontSize: 28,
        fontWeight: 'bold',
        color: '#1a365d',
        marginBottom: 12,
    },
    rechargeButton: {
        backgroundColor: '#4B0055',
        paddingHorizontal: 20,
        paddingVertical: 10,
        borderRadius: 24,
        justifyContent: 'center',
        alignItems: 'flex-end',
    },
    rechargeButtonText: {
        color: '#fff',
        fontWeight: '500',
        fontSize: 16,
    },
    greenPointsCard: {
        backgroundColor: '#fff',
        borderRadius: 16,
        padding: 20,
        marginBottom: 20,
        shadowColor: '#000',
        shadowOpacity: 0.05,
        shadowRadius: 5,
        shadowOffset: {width: 0, height: 2},
        elevation: 3,
        borderLeftWidth: 4,
        borderLeftColor: '#22C55E',
    },
    greenPointsContent: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
    },
    greenPointsLeft: {
        flexDirection: 'row',
        alignItems: 'center',
        flex: 1,
    },
    leafIconContainer: {
        backgroundColor: '#F0FDF4',
        padding: 12,
        borderRadius: 12,
        marginRight: 16,
    },
    greenPointsInfo: {
        flex: 1,
    },
    greenPointsLabel: {
        fontSize: 18,
        fontWeight: '600',
        color: '#22C55E',
        marginBottom: 2,
    },
    greenPointsSubtext: {
        fontSize: 13,
        color: '#666',
    },
    greenPointsRight: {
        alignItems: 'center',
        flexDirection: 'row',
    },
    greenPointsAmount: {
        fontSize: 24,
        fontWeight: 'bold',
        color: '#22C55E',
        marginRight: 8,
    },
    infoButton: {
        padding: 4,
    },
    menuContainer: {
        marginTop: 10,
    },
    menuItem: {
        backgroundColor: '#fff',
        borderRadius: 12,
        padding: 16,
        marginBottom: 12,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        shadowColor: '#000',
        shadowOpacity: 0.06,
        shadowRadius: 6,
        shadowOffset: {width: 0, height: 2},
        elevation: 2,
    },
    menuItemLeft: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    menuItemText: {
        fontSize: 16,
        color: '#333333',
        fontWeight: '500',
    },
    separator: {
        width: 1,
        height: '80%', // O puoi usare un valore fisso come 40
        backgroundColor: '#E5E7EB', // Colore grigio chiaro
        alignSelf: 'center',
        marginHorizontal: 12, // Spazio ai lati del separatore
    },
});
