import {StyleSheet} from 'react-native';


export const styles = StyleSheet.create({
    // Container generale
    container: {
        backgroundColor: '#FAF8FF',
        paddingTop: 40,
        paddingHorizontal: 20,
        paddingBottom: 100,
    },

    reportsScrollView: {
        padding: 16,
        backgroundColor: '#f9fafb',
    },


    statusBanner: {
        borderRadius: 12,
        paddingVertical: 10,
        paddingHorizontal: 16,
        marginHorizontal: 50,
        alignItems: 'center',
    },

    statusText: {
        fontSize: 16,
        fontWeight: '600',
        color: '#fff',
    },

    statusOk: {
        backgroundColor: '#22C55E', // verde
    },

    statusMedium: {
        backgroundColor: '#F59E0B', // arancio
    },

    statusCritical: {
        backgroundColor: '#EF4444', // rosso
    },
    reportCard: {
        backgroundColor: '#ffffff',
        borderRadius: 12,
        padding: 16,
        marginBottom: 14,
        shadowColor: '#000',
        shadowOpacity: 0.06,
        shadowRadius: 6,
        shadowOffset: { width: 0, height: 2 },
        elevation: 3,
    },

    reportHeader: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: 10,
    },

    reportTypeBadge: {
        borderRadius: 20,
        paddingVertical: 4,
    },

    aiBadge: {
        backgroundColor: '#3b82f6', // blu AI
    },

    userInfoLeft: {
        flexDirection: 'row',
        alignItems: 'center',
        gap: 8, // usa marginRight se non hai supporto gap
    },


    reportTypeText: {
        fontSize: 14,
        fontWeight: '600',
        color: '#111827',
    },

    sourceBadgeAI: {
        backgroundColor: '#3b82f6', // blu
        borderRadius: 10,
        paddingHorizontal: 6,
        paddingVertical: 2,
        marginLeft: 5,
    },

    sourceBadgeUser: {
        backgroundColor: '#10b981', // verde
        borderRadius: 10,
        paddingHorizontal: 6,
        paddingVertical: 2,
        marginLeft: 6,
    },

    sourceBadgeText: {
        color: 'white',
        fontSize: 12,
        fontWeight: 'bold',
    },

    reportDate: {
        fontSize: 13,
        color: '#6b7280',
    },

    headerLeftGroup: {
        flexDirection: 'row',
        alignItems: 'center',
        gap: 6,
    },

    reportRating: {
        fontSize: 13,
        fontWeight: '600',
        color: '#6b7280',
        marginBottom: 10,
    },

    reportDescription: {
        fontSize: 14,
        color: '#374151',
        lineHeight: 20,
    },

    bulletPointsContainer: {
        paddingLeft: 10,
    },

    bulletPoint: {
        fontSize: 14,
        lineHeight: 20,
        color: '#4b5563',
        marginBottom: 6,
    },

    noReportsContainer: {
        marginTop: 30,
        alignItems: 'center',
    },

    noReportsText: {
        color: '#9ca3af',
        fontSize: 16,
        fontStyle: 'italic',
    },

    // Titolo principale
    title: {
        fontSize: 28,
        fontWeight: 'bold',
        color: '#4B0055',
        marginBottom: 20,
        textAlign: 'center',
    },

    // Contenitore singolo parcheggio
    parkContainer: {
        marginBottom: 40,
        backgroundColor: 'white',
        borderRadius: 12,
        padding: 16,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 4,
        elevation: 3,
    },

    parkHeader: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: 15,
    },

    parkName: {
        fontSize: 22,
        color: '#4B0055',
        fontWeight: '600',
        flex: 1,
        textAlign: 'center',
    },



    reportsButtonAlert: {
        backgroundColor: '#FFE5E5',
        borderWidth: 1,
        borderColor: '#F44336',
    },

    reportsButtonText: {
        fontSize: 12,
        color: '#666',
        fontWeight: '500',
    },

    reportsButtonTextAlert: {
        color: '#F44336',
        fontWeight: 'bold',
    },

    modalOverlay: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(0, 0, 0, 0.5)', // sfondo scuro trasparente
    },

    modalContent: {
        width: '85%',
        backgroundColor: '#fff',
        borderRadius: 12,
        padding: 20,
        alignItems: 'center',
        elevation: 5,
    },

    modalTitle: {
        marginTop:20,
        fontSize: 25,
        fontWeight: 'bold',
        textAlign: 'center',
        marginBottom: 20,
        color: '#333',
    },

    modalButtons: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        width: '100%',
        marginTop: 20,
    },

    cancelButton: {
        flex: 1,
        backgroundColor: '#ccc',
        paddingVertical: 10,
        borderRadius: 8,
        marginRight: 10,
        alignItems: 'center',
    },

    cancelButton2: {
        backgroundColor: '#ccc',
        padding: 10,
        borderRadius: 8,
        alignItems: 'center',
        margin: 10,
    },

    cancelButtonText: {
        color: '#333',
        fontWeight: 'bold',
    },

    confirmButton: {
        flex: 1,
        backgroundColor: '#4CAF50',
        paddingVertical: 10,
        borderRadius: 8,
        marginLeft: 10,
        alignItems: 'center',
    },

    confirmButton2: {
        backgroundColor: '#4CAF50',
        padding: 15,
        borderRadius: 8,
        alignItems: 'center',
        margin: 10,
    },

    confirmButtonText: {
        color: '#fff',
        fontWeight: 'bold',
    },

    // Griglia parcheggi e celle
    gridContainer: {
        flexDirection: 'column',
        alignItems: 'center',
    },

    row: {
        flexDirection: 'row',
    },

    cell: {
        width: 50,
        height: 50,
        margin: 3,
        borderRadius: 8,
        alignItems: 'center',
        justifyContent: 'center',
        borderWidth: 1,
        borderColor: '#DDD',
    },

    cellText: {
        fontWeight: 'bold',
        fontSize: 12,
        color: 'white',
    },

    reportCustomer: {
        fontWeight: 'bold',
    },

    selectedCell: {
        borderWidth: 3,
        borderColor: '#6A0DAD',
        shadowColor: '#6A0DAD',
        shadowOffset: { width: 0, height: 0 },
        shadowOpacity: 0.5,
        shadowRadius: 4,
        elevation: 5,
    },

    menuIcon: {
        marginRight: 12,
    },

    // Container azioni in basso a destra
    actionContainer: {
        position: 'absolute',
        bottom: 30,
        right: 20,
        flexDirection: 'column',
        gap: 10,
    },

    moveButton: {
        backgroundColor: '#4B0055',
        paddingHorizontal: 20,
        paddingVertical: 12,
        borderRadius: 30,
        elevation: 4,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.3,
        shadowRadius: 4,
    },

    clearButton: {
        backgroundColor: '#666',
        paddingHorizontal: 20,
        paddingVertical: 12,
        borderRadius: 30,
        elevation: 4,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.3,
        shadowRadius: 4,
    },

    buttonText: {
        color: 'white',
        fontWeight: 'bold',
        textAlign: 'center',
    },

    buttonText2: {
        color: 'black',
        fontWeight: 'bold',
        textAlign: 'center',
    },

    modalContainer: {
        width: '90%',
        maxWidth: 400,
        backgroundColor: '#FAF8FF',
        padding: 24,
        borderRadius: 12,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 4 },
        shadowOpacity: 0.3,
        shadowRadius: 8,
        elevation: 8,
    },


    modalText: {
        fontSize: 16,
        color: '#333',
        marginBottom: 8,
    },

    bikeList: {
        fontSize: 14,
        color: '#666',
        fontStyle: 'italic',
        marginBottom: 20,
    },

    labelText: {
        fontSize: 16,
        color: '#333',
        marginBottom: 8,
        fontWeight: '500',
    },

    pickerContainer: {
        borderWidth: 1,
        borderColor: '#DDD',
        borderRadius: 8,
        marginBottom: 16,
        backgroundColor: 'white',
        width: '100%',
        overflow: 'hidden', // aiuta con problemi su Android
    },

    picker: {
        height: 50,
        width: '100%',
        color: '#333',
        backgroundColor: '#fff', // migliora la visibilità del testo
    },

    spotsInfo: {
        fontSize: 14,
        fontWeight: '500',
        marginBottom: 20,
        textAlign: 'center',
    },


    disabledButton: {
        backgroundColor: '#CCC',
    },

    reportId: {
        fontSize: 13,
        fontWeight: '600',
        color: '#374151',
        marginBottom: 6,
    },


    customerReportCard: {
        backgroundColor: '#ffffff',
        borderRadius: 12,
        padding: 16,
        marginBottom: 14,
        borderWidth: 1,
        borderColor: '#e5e7eb',
    },

    customerReportHeader: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        marginBottom: 8,
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

    // Modal segnalazioni
    reportsModalContainer: {
        width: '95%',
        maxWidth: 500,
        height: '80%',
        backgroundColor: '#FAF8FF',
        borderRadius: 12,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 4 },
        shadowOpacity: 0.3,
        shadowRadius: 8,
        elevation: 8,
    },

    bikeStatus: {
        fontSize: 16,
        fontWeight: '600',
        textAlign: 'center',
        marginBottom: 20,
    },



    bikeIdContainer: {
        backgroundColor: '#4B0055',
        paddingHorizontal: 8,
        paddingVertical: 4,
        borderRadius: 12,
    },

    bikeIdText: {
        color: 'white',
        fontSize: 12,
        fontWeight: 'bold',
    },

    reportTypeBadgeText: {
        color: 'white',
        fontSize: 12,
        fontWeight: 'bold',
    },


    reportStatus: {
        alignItems: 'flex-end',
    },

    reportStatusText: {
        fontSize: 12,
        fontWeight: '600',
    },

    closeReportsButton: {
        margin: 24,
        marginTop: 0,
        marginBottom: 16,
        backgroundColor: '#4B0055',
        paddingVertical: 12,
        borderRadius: 8,
        alignItems: 'center',
    },

    // Modal report AI
    aiReportModalContainer: {
        width: '95%',
        maxWidth: 600,
        height: '85%',
        backgroundColor: '#FAF8FF',
        borderRadius: 12,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 4 },
        shadowOpacity: 0.3,
        shadowRadius: 8,
        elevation: 8,
    },

    aiReportScrollView: {
        flex: 1,
        paddingHorizontal: 24,
    },

    priorityContainer: {
        alignItems: 'center',
        marginBottom: 20,
    },

    priorityBadge: {
        paddingHorizontal: 20,
        paddingVertical: 8,
        borderRadius: 20,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.2,
        shadowRadius: 4,
        elevation: 3,
    },

    priorityText: {
        color: 'white',
        fontSize: 14,
        fontWeight: 'bold',
        letterSpacing: 1,
    },

    reportInfoContainer: {
        backgroundColor: 'rgba(75, 0, 85, 0.05)',
        padding: 16,
        borderRadius: 8,
        marginBottom: 20,
    },

    reportInfoLabel: {
        fontSize: 12,
        color: '#666',
        fontWeight: '600',
        marginTop: 8,
        textTransform: 'uppercase',
        letterSpacing: 0.5,
    },

    reportInfoValue: {
        fontSize: 16,
        color: '#4B0055',
        fontWeight: '500',
        marginBottom: 4,
    },

    sectionContainer: {
        marginBottom: 24,
    },

    sectionTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#4B0055',
        marginBottom: 12,
        borderBottomWidth: 2,
        borderBottomColor: '#E8E8E8',
        paddingBottom: 8,
    },

    summaryContainer: {
        backgroundColor: 'white',
        padding: 16,
        borderRadius: 8,
        borderLeftWidth: 4,
        borderLeftColor: '#4B0055',
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 1 },
        shadowOpacity: 0.1,
        shadowRadius: 2,
        elevation: 2,
    },

    summaryText: {
        fontSize: 14,
        color: '#333',
        lineHeight: 22,
        textAlign: 'justify',
    },

    recommendationItem: {
        flexDirection: 'row',
        alignItems: 'flex-start',
        backgroundColor: 'white',
        padding: 16,
        borderRadius: 8,
        marginBottom: 8,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 1 },
        shadowOpacity: 0.1,
        shadowRadius: 2,
        elevation: 2,
    },

    recommendationNumber: {
        fontSize: 16,
        fontWeight: 'bold',
        color: '#4B0055',
        marginRight: 12,
        minWidth: 20,
    },

    recommendationText: {
        fontSize: 14,
        color: '#333',
        lineHeight: 20,
        flex: 1,
    },

    reportIdContainer: {
        backgroundColor: 'rgba(0, 0, 0, 0.05)',
        padding: 12,
        borderRadius: 8,
        marginBottom: 10,
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
    },

    reportIdLabel: {
        fontSize: 12,
        color: '#666',
        fontWeight: '600',
    },

    reportIdValue: {
        fontSize: 12,
        color: '#333',
        fontFamily: 'monospace',
    },

    aiReportButtonsContainer: {
        padding: 24,
        paddingTop: 16,
        flexDirection: 'row',
        justifyContent: 'space-between',
        gap: 0,
    },
    closeAiReportButton: {
        marginLeft: 10,
        flex: 1,
        backgroundColor: '#666',
        paddingVertical: 12,
        borderRadius: 8,
        alignItems: 'center',
        elevation: 3,
    },
    modalButtonsContainer: {
        paddingHorizontal: 24,
        paddingTop: 16,
        flexDirection: 'column',
        gap: 12,
    },
    aiReportButton: {
        backgroundColor: '#6A0DAD',
        paddingVertical: 12,
        borderRadius: 8,
        alignItems: 'center',
        marginTop: 8,
        elevation: 3,
    },
    loadingContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        gap: 8,
    },
    maintainButton: {
        marginHorizontal: 24,
        marginBottom: 16,
        marginTop: 10,
        backgroundColor: '#FFA726', // Material Design Orange 400
        paddingVertical: 14,
        borderRadius: 10,
        alignItems: 'center',
        flexDirection: 'row',
        justifyContent: 'center',
        elevation: 5,
    },
    maintainButtonText: {
        color: '#000',
        fontSize: 16,
        fontWeight: '600',
    },
    input: {
        borderWidth: 1,
        borderColor: '#ccc',
        borderRadius: 8,
        padding: 10,
        marginBottom: 16,
    },
    topRightButton: {
        position: 'absolute',
        top: 15,
        right: 15,
        padding: 12,
        zIndex: 10,
        backgroundColor: '#2180f3',
        borderRadius: 10,
    },
    topRightButtonText: {
        fontSize: 16,
        fontWeight: 'bold',
        color: '#333',
    },
    buttonsAI: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center', // <-- centra orizzontalmente
        gap: 10,                  // o usa marginRight se gap non è supportato
        marginBottom: 15,
    },

    reportsButton: {
        backgroundColor: '#2196F3',
        paddingVertical: 10,
        paddingHorizontal: 15,
        borderRadius: 8,
    },

    addButton: {
        backgroundColor: '#4CAF50',
        paddingVertical: 10,
        paddingHorizontal: 15,
        borderRadius: 8,
    },

    addButtonText: {
        color: '#fff',
        fontWeight: 'bold',
        textAlign: 'center',
    },
});
