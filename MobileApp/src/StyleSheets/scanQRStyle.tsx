import {Dimensions, StyleSheet} from "react-native";

const { width, height } = Dimensions.get('window');
export const styles = StyleSheet.create({
    container: {
        flex: 1,
        width: '100%',
        height: '100%',
    },
    fullScreen: {
        width: width,
        height: height,
        position: 'absolute',
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
    },
    overlayContainer: {
        position: 'absolute',
        top: 0,
        left: 5,
        right: 5,
        bottom: 0,
        justifyContent: 'flex-start',
    },
    headerContainer: {
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        paddingVertical: 16,
        paddingHorizontal: 24,
        borderRadius: 20,
        alignItems: 'center',
    },
    headerText: {
        fontSize: 24,
        fontWeight: 'bold',
        color: '#ffffff',
        textShadowColor: 'rgba(0, 0, 0, 0.75)',
        textShadowOffset: { width: 1, height: 1 },
        textShadowRadius: 3,
        marginBottom: 6,
    },
    subHeaderText: {
        fontSize: 16,
        color: '#f0f0f0',
        textShadowColor: 'rgba(0, 0, 0, 0.75)',
        textShadowOffset: { width: 0.5, height: 0.5 },
        textShadowRadius: 2,
    },
    title: {
        fontSize: 20,
        fontWeight: 'bold',
        marginTop: 200,
        alignSelf: 'center',
    },
});
