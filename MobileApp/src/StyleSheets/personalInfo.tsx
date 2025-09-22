import {StyleSheet} from "react-native";

export const styles = StyleSheet.create({
    container: {
        flex: 1,
        padding: 24,
        backgroundColor: '#FAF8FF',
    },
    title: {
        marginTop: 20,
        fontSize: 22,
        fontWeight: '700',
        color: '#4B0055',
        marginBottom: 24,
    },
    infoBox: {
        backgroundColor: '#fff',
        borderRadius: 12,
        padding: 16,
        marginBottom: 16,
        shadowColor: '#000',
        shadowOpacity: 0.06,
        shadowRadius: 6,
        shadowOffset: {width: 0, height: 2},
        elevation: 1,
    },
    label: {
        marginTop: 8,
        fontSize: 14,
        color: '#999',
    },
    value: {
        fontSize: 16,
        color: '#333',
        fontWeight: '500',
        marginTop: 4,
    },
    button: {
        // aggiungi questa proprietà
        width: '50%',
        alignSelf: 'center',
        marginTop: 20,
        height: 40,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#fff',
        borderRadius: 12,
        marginBottom: 12,
        flexDirection: 'row',
        shadowColor: '#000',
        shadowOpacity: 0.06,
        shadowRadius: 6,
        shadowOffset: {width: 0, height: 2},
        elevation: 2,
    },
    buttonText: {
        color: '#4B0055',
        fontSize: 18,
    },
    box: {
        alignItems: 'center',
    },
});
