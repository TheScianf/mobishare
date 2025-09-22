import {StyleSheet} from "react-native";

export const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#FAF8FF',
        paddingHorizontal: 24,
        justifyContent: 'space-between',
        paddingTop: 40,
        paddingBottom: 20,
    },
    appName: {
        fontSize: 28,
        fontWeight: '600',
        textAlign: 'center',
        marginBottom: 20,
    },
    formContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        paddingHorizontal: 8,
    },
    headerSection: {
        alignItems: 'center',
        marginBottom: 32,
    },
    title: {
        fontSize: 24,
        fontWeight: '600',
        marginBottom: 8,
        color: '#000',
    },
    subtitle: {
        fontSize: 16,
        color: '#666',
        textAlign: 'center',
    },
    inputSection: {
        width: '100%',
        marginBottom: 16,
    },
    input: {
        width: '100%',
        height: 52,
        borderRadius: 12,
        borderColor: '#E0E0E0',
        borderWidth: 1.5,
        paddingHorizontal: 16,
        marginBottom: 16,
        fontSize: 16,
        backgroundColor: '#FFFFFF',
        fontFamily: 'sans-serif', // 👈 AGGIUNGI QUESTO
    },
    errorContainer: {
        width: '100%',
        alignItems: 'center',
        marginBottom: 20,
    },
    error: {
        color: '#FF3B30',
        fontSize: 14,
        textAlign: 'center',
        paddingHorizontal: 16,
    },
    buttonSection: {
        alignItems: 'center',
        width: '100%',
        marginTop: 8,
    },
    button: {
        backgroundColor: '#000',
        borderRadius: 12,
        height: 52,
        justifyContent: 'center',
        alignItems: 'center',
        width: '100%',
        marginBottom: 16,
    },
    buttonText: {
        color: '#fff',
        fontSize: 16,
        fontWeight: '600',
    },
    orText: {
        fontSize: 14,
        color: '#888',
        textAlign: 'center',
        marginBottom: 16,
    },
    outlineButton: {
        backgroundColor: 'transparent',
        borderWidth: 1.5,
        borderColor: '#000',
    },
    outlineButtonText: {
        color: '#000',
    },
    signupLink: {
        marginTop: 24,
    },
    signupText: {
        fontSize: 15,
        color: '#666',
        textAlign: 'center',
    },
    link: {
        textDecorationLine: 'underline',
        color: '#000',
        fontWeight: '500',
    },
    legalTextContainer: {
        alignItems: 'center',
        paddingHorizontal: 16,
    },
    legalText: {
        fontSize: 12,
        color: '#999',
        textAlign: 'center',
        lineHeight: 16,
    },
});
