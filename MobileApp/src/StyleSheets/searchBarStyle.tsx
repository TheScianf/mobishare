import {StyleSheet} from "react-native";

export const styles = StyleSheet.create({
    searchContainer: {
        backgroundColor: '#fff',
        borderRadius: 12,
        padding: 8,
        marginTop: 40,
        marginBottom: 12,
        marginRight: 20,
        marginLeft: 20,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        shadowColor: '#000',
        shadowOpacity: 0.06,
        shadowRadius: 6,
        shadowOffset: { width: 0, height: 2 },
        elevation: 2,
    },
    icon: {
        marginRight: 8,
    },
    input: {
        flex: 1,
        fontSize: 16,
        color: '#333',
    },
});
