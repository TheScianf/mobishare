import {Platform, StyleSheet} from 'react-native';

export const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#eef2f7',
        padding: 16,
        paddingTop: Platform.OS === 'android' ? 40 : 60,
    },
    title: {
        fontSize: 28,
        fontWeight: '700',
        textAlign: 'center',
        color: '#1e293b',
        marginBottom: 20,
    },
    actions: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
    },
    input: {
        backgroundColor: '#fff',
        paddingHorizontal: 14,
        paddingVertical: 10,
        borderRadius: 12,
        borderWidth: 1,
        borderColor: '#cbd5e1',
        marginBottom: 20,
        fontSize: 16,
        color: '#111',
    },
    card: {
        backgroundColor: '#ffffff',
        borderRadius: 16,
        padding: 16,
        marginBottom: 14,
        shadowColor: '#000',
        shadowOpacity: 0.05,
        shadowRadius: 10,
        shadowOffset: {width: 0, height: 4},
        elevation: 3,
    },
    cardContent: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
    },
    info: {
        flex: 1,
        marginRight: 12,
    },
    name: {
        fontSize: 18,
        fontWeight: '600',
        color: '#0f172a',
    },
    email: {
        fontSize: 14,
        color: '#475569',
        marginVertical: 2,
    },
    date: {
        fontSize: 12,
        color: '#94a3b8',
    },
    toPay: {
        fontSize: 14,
        fontWeight: '600',
        color: '#dc2626', // rosso acceso per evidenziare
        marginTop: 4,
    },
    paid: {
        fontSize: 14,
        fontWeight: '600',
        color: 'green', // rosso acceso per evidenziare
        marginTop: 4,
    },
    button: {
        backgroundColor: '#10b981',
        paddingVertical: 6,
        paddingHorizontal: 12,
        borderRadius: 8,
    },
    buttonText: {
        color: '#fff',
        fontWeight: '600',
        fontSize: 14,
    },
    emptyText: {
        fontSize: 16,
        color: '#64748b',
        textAlign: 'center',
        marginTop: 40,
    },
    emptyContainer: {
        flexGrow: 1,
        justifyContent: 'center',
    },
});
