import {StyleSheet} from "react-native";

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
    menu: {
        flexGrow: 1,
        paddingBottom: 20,
    },
    menuItem: {
        backgroundColor: '#fff',
        borderRadius: 12,
        padding: 16,
        marginBottom: 12,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        elevation: 2,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 1 },
        shadowOpacity: 0.2,
        shadowRadius: 2,
    },
    menuItemLeft: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    menuIcon: {
        marginRight: 12,
    },
    menuItemText: {
        fontSize: 16,
        color: '#333333',
        fontWeight: '500',
    },
    logout: {
        flexDirection: 'row',
        alignItems: 'center',
        marginTop: 40,
        alignSelf: 'center',
        padding: 10,
    },
    logoutIcon: {
        marginRight: 10,
    },
    logoutText: {
        fontSize: 16,
        color: '#D10055',
        fontWeight: '600',
    },
});
