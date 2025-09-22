import {Dimensions, StyleSheet} from 'react-native';

const { width } = Dimensions.get('window');


export const styles = StyleSheet.create({
    container: { flex: 1, backgroundColor: '#1a1a1a' },
    header: {
        padding: 20,
        alignItems: 'center',
        backgroundColor: '#2d2d2d',
        borderBottomWidth: 1,
        borderBottomColor: '#404040',
    },
    title: { fontSize: 28, fontWeight: 'bold', color: '#fff', marginBottom: 8 },
    subtitle: { fontSize: 16, color: '#bbb', fontStyle: 'italic' },
    photoContent: { flex: 1, padding: 20, justifyContent: 'center' },
    photoContainer: { alignItems: 'center' },
    preview: {
        width: width - 40,
        height: width - 40,
        borderRadius: 20,
        marginBottom: 20,
        borderWidth: 3,
        borderColor: '#007AFF',
    },
    photoInfo: {
        backgroundColor: '#2d2d2d',
        padding: 15,
        borderRadius: 10,
        marginBottom: 15,
        alignItems: 'center',
        width: '100%',
    },
    fileName: { color: '#fff', fontSize: 16, fontWeight: '600', marginBottom: 5 },
    fileSize: { color: '#bbb', fontSize: 14 },
    photoButtons: { flexDirection: 'row', width: '100%', justifyContent: 'space-between' },
    actionButton: {
        flex: 1,
        paddingVertical: 12,
        borderRadius: 10,
        alignItems: 'center',
    },
    confirmButton: { backgroundColor: '#4CAF50' },
    removeButton: { backgroundColor: '#ff4444' },
    actionText: { color: '#fff', fontSize: 16, fontWeight: '600' },
    placeholder: { alignItems: 'center', justifyContent: 'center', flex: 1 },
    placeholderIcon: { fontSize: 80, marginBottom: 20, opacity: 0.5 },
    placeholderText: { fontSize: 22, color: '#fff', fontWeight: '600', marginBottom: 10 },
    placeholderHint: { fontSize: 16, color: '#bbb', textAlign: 'center' },
    footerButtons: { padding: 20 },
    primaryButton: {
        backgroundColor: '#007AFF',
        paddingVertical: 18,
        borderRadius: 15,
        alignItems: 'center',
        elevation: 8,
        marginBottom: 36,
    },
    disabledButton: {
        backgroundColor: '#666',
        elevation: 0,
    },
    primaryButtonText: { color: '#fff', fontSize: 18, fontWeight: 'bold' },
});
