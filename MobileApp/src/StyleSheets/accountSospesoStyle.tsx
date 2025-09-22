import {StyleSheet} from "react-native";

export const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fef2f2',
        alignItems: 'center',
        justifyContent: 'center',
        padding: 30,
    },
    title: {
        fontSize: 28,
        fontWeight: 'bold',
        color: '#b91c1c',
        marginTop: 20,
    },
    message: {
        fontSize: 16,
        color: '#7f1d1d',
        textAlign: 'center',
        marginTop: 15,
        marginBottom: 30,
    },
    button: {
        backgroundColor: '#ef4444',
        paddingVertical: 12,
        paddingHorizontal: 25,
        borderRadius: 10,
        elevation: 3,
    },
    buttonText: {
        color: 'white',
        fontWeight: '600',
        fontSize: 16,
    },
});
