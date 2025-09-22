import {StyleSheet} from "react-native";

export const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#FAF8FF',
    },
    scrollContent: {
        flexGrow: 1,
        paddingHorizontal: 24,
        paddingTop: 20,
        paddingBottom: 20,
    },
    appName: {
        fontSize: 28,
        fontWeight: '600',
        textAlign: 'center',
        marginTop: 20,
        marginBottom: 32,
        color: '#000',
    },
    formContainer: {
        flex: 1,
        justifyContent: 'center',
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
        marginBottom: 20,
    },
    inputContainer: {
        marginBottom: 16,
    },
    input: {
        width: '100%',
        height: 52,
        borderRadius: 12,
        borderColor: '#E0E0E0',
        borderWidth: 1.5,
        paddingHorizontal: 16,
        fontSize: 16,
        backgroundColor: '#FFFFFF',
    },
    errorInput: {
        borderColor: '#FF3B30',
    },
    errorText: {
        color: '#FF3B30',
        fontSize: 13,
        marginTop: 6,
        marginLeft: 4,
    },
    errorContainer: {
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
        marginTop: 8,
    },
    button: {
        backgroundColor: '#000',
        borderRadius: 12,
        height: 52,
        justifyContent: 'center',
        alignItems: 'center',
        width: '100%',
        marginBottom: 20,
    },
    buttonText: {
        color: '#fff',
        fontSize: 16,
        fontWeight: '600',
    },
    loginLink: {
        marginTop: 8,
    },
    loginText: {
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
        marginTop: 24,
    },
    legalText: {
        fontSize: 12,
        color: '#999',
        textAlign: 'center',
        lineHeight: 16,
    },
});