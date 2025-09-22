import { StyleSheet, Dimensions } from 'react-native';

const { width } = Dimensions.get('window');

export const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#f0f4f8',
        paddingTop: 10,
    },
    titolo: {
        fontSize: 22,
        fontWeight: '700',
        textAlign: 'center',
        marginVertical: 14,
        color: '#1e293b',
    },
    card: {
        backgroundColor: '#fff',
        borderRadius: 14,
        padding: 16,
        marginVertical: 8,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 4,
        elevation: 3,
    },
    // Notifiche
    notification: {
        position: 'absolute',
        top: 10,
        left: 20,
        right: 20,
        padding: 16,
        borderRadius: 8,
        zIndex: 1000,
        elevation: 10,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.3,
        shadowRadius: 4,
    },

    notificationInfo: {
        backgroundColor: '#2196F3',
    },

    notificationSuccess: {
        backgroundColor: '#4CAF50',
    },

    notificationError: {
        backgroundColor: '#F44336',
    },

    notificationText: {
        color: 'white',
        fontWeight: '500',
        textAlign: 'center',
    },
    nome: {
        fontSize: 17,
        fontWeight: '600',
        color: '#0f172a',
        marginBottom: 6,
    },
    tipo: {
        fontSize: 14,
        color: '#334155',
        marginBottom: 4,
    },
    empty: {
        fontSize: 16,
        color: '#64748b',
        marginTop: 30,
    },
    modalOverlay: {
        flex: 1,
        backgroundColor: 'rgba(30, 41, 59, 0.6)',
        justifyContent: 'center',
        alignItems: 'center',
        paddingHorizontal: 20,
    },
    modalContainer: {
        width: width * 0.9,
        maxWidth: 400,
        backgroundColor: '#fff',
        borderRadius: 20,
        padding: 24,
        shadowColor: '#00000040',
        shadowOffset: { width: 0, height: 6 },
        shadowOpacity: 0.25,
        shadowRadius: 8,
        elevation: 7,
    },
    modalTitle: {
        fontSize: 20,
        fontWeight: '700',
        color: '#0f172a',
        marginBottom: 20,
        textAlign: 'center',
    },
    bottoneConferma: {
        backgroundColor: '#16a34a',
        borderRadius: 10,
        paddingVertical: 14,
        alignItems: 'center',
        marginBottom: 12,
    },
    bottoneTesto: {
        color: '#fff',
        fontWeight: '700',
        fontSize: 16,
    },
    chiudiTesto: {
        color: '#dc2626',
        fontWeight: '600',
        fontSize: 16,
        textAlign: 'center',
    },
});
