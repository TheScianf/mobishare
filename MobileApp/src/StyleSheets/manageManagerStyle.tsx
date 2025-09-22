import {Platform, StyleSheet} from "react-native";

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
    addButton: {
        backgroundColor: '#10b981',
        padding: 12,
        borderRadius: 12,
        marginBottom: 20,
    },
    addButtonText: {
        color: 'white',
        fontSize: 16,
        textAlign: 'center',
        fontWeight: '600',
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
    email: {
        fontSize: 18,
        fontWeight: '600',
        color: '#0f172a',
    },
    adminness: {
        fontSize: 14,
        color: '#475569',
        marginVertical: 2,
    },
    numParking: {
        fontSize: 12,
        color: '#94a3b8',
    },
    buttonContainer: {
        gap: 8,
    },
    toggleButton: {
        backgroundColor: '#4A90E2',
        padding: 8,
        borderRadius: 8,
    },
    toggleButtonText: {
        color: 'white',
        textAlign: 'center',
        fontSize: 12,
    },
    deleteButton: {
        backgroundColor: '#ef4444',
        padding: 8,
        borderRadius: 8,
    },
    deleteButtonText: {
        color: 'white',
        textAlign: 'center',
        fontSize: 12,
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
    loadingContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    modalOverlay: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(0,0,0,0.5)',
    },
    modalContent: {
        backgroundColor: 'white',
        padding: 20,
        borderRadius: 12,
        width: '90%',
    },
    modalTitle: {
        fontSize: 20,
        fontWeight: '600',
        marginBottom: 20,
        textAlign: 'center',
    },
    switchContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        marginBottom: 20,
        justifyContent: 'space-between',
    },
    switchLabel: {
        fontSize: 16,
        color: '#1e293b',
    },
    modalButtons: {
        flexDirection: 'row',
        justifyContent: 'space-around',
    },
    // Nuovi stili per il modal di eliminazione
    deleteModalContent: {
        backgroundColor: 'white',
        borderRadius: 16,
        padding: 20,
        width: '90%',
        maxHeight: '80%',
    },
    deleteModalTitle: {
        fontSize: 22,
        fontWeight: '700',
        textAlign: 'center',
        color: '#ef4444',
        marginBottom: 8,
    },
    deleteModalSubtitle: {
        fontSize: 16,
        textAlign: 'center',
        color: '#1e293b',
        marginBottom: 16,
        fontWeight: '500',
    },
    deleteModalDescription: {
        fontSize: 14,
        color: '#64748b',
        textAlign: 'center',
        marginBottom: 20,
        lineHeight: 20,
    },
    substitutorsList: {
        maxHeight: 300,
        marginBottom: 20,
    },
    substitutorItem: {
        flexDirection: 'row',
        alignItems: 'center',
        padding: 16,
        marginBottom: 8,
        borderRadius: 12,
        backgroundColor: '#f8fafc',
        borderWidth: 2,
        borderColor: 'transparent',
    },
    substitutorItemSelected: {
        backgroundColor: '#dbeafe',
        borderColor: '#3b82f6',
    },
    substitutorContent: {
        flex: 1,
    },
    substitutorEmail: {
        fontSize: 16,
        fontWeight: '600',
        color: '#1e293b',
        marginBottom: 4,
    },
    substitutorEmailSelected: {
        color: '#1e40af',
    },
    substitutorInfo: {
        fontSize: 12,
        color: '#64748b',
    },
    substitutorInfoSelected: {
        color: '#3730a3',
    },
    checkmark: {
        width: 24,
        height: 24,
        borderRadius: 12,
        backgroundColor: '#10b981',
        alignItems: 'center',
        justifyContent: 'center',
        marginLeft: 12,
    },
    checkmarkText: {
        color: 'white',
        fontSize: 16,
        fontWeight: 'bold',
    },
    deleteModalButtons: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        gap: 12,
    },
    cancelButton: {
        flex: 1,
        backgroundColor: '#f3f4f6',
        padding: 14,
        borderRadius: 12,
        alignItems: 'center',
    },
    cancelButtonText: {
        color: '#374151',
        fontSize: 16,
        fontWeight: '600',
    },
    confirmButton: {
        flex: 1,
        backgroundColor: '#ef4444',
        padding: 14,
        borderRadius: 12,
        alignItems: 'center',
    },
    confirmButtonDisabled: {
        backgroundColor: '#d1d5db',
    },
    confirmButtonText: {
        color: 'white',
        fontSize: 16,
        fontWeight: '600',
    },
    confirmButtonTextDisabled: {
        color: '#9ca3af',
    },
});
