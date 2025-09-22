import React, { useState } from 'react';
import { View, TextInput, TouchableOpacity, StyleSheet } from 'react-native';
import Icon from 'react-native-vector-icons/MaterialIcons';

interface ChatFooterProps {
  onSend: (message: string) => void;
}
const ChatFooter = (props: ChatFooterProps) => {
    const [message, setMessage] = useState('');

    const isMessageValid = () => {
        return message.trim().length > 0;
    };

    const handleSend = () => {
        if (isMessageValid()) {
            props.onSend(message);
            setMessage(''); // ← Resetta il campo dopo l'invio
        }
    };

    return (
        <View style={styles.container}>
            <TextInput
                style={styles.input}
                placeholder="chiedi qualcosa..."
                value={message}
                onChangeText={setMessage}
                multiline
                textAlignVertical="top"
                scrollEnabled={true}
                numberOfLines={4}
                onSubmitEditing={() => props.onSend(message)}
            />
            <TouchableOpacity
                style={[
                    styles.sendButton,
                    !isMessageValid() && styles.sendButtonDisabled,
                ]}
                onPress={handleSend} // ← Usa la funzione locale
                disabled={!isMessageValid()}
            >
                <Icon
                    name="send"
                    size={24}
                    color={isMessageValid() ? '#007AFF' : '#CCCCCC'}
                />
            </TouchableOpacity>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flexDirection: 'row',
        padding: 10,
        alignItems: 'flex-end',
        backgroundColor: '#FFFFFF',
        borderTopWidth: 1,
        borderTopColor: '#EEEEEE',
    },
    input: {
        flex: 1,
        marginRight: 10,
        maxHeight: 96, // Altezza approssimativa per 4 righe (24px per riga)
        minHeight: 40,
        paddingHorizontal: 15,
        paddingVertical: 8,
        backgroundColor: '#F8F8F8',
        borderRadius: 20,
        fontSize: 16,
    },
    sendButton: {
        width: 40,
        height: 40,
        justifyContent: 'center',
        alignItems: 'center',
    },
    sendButtonDisabled: {
        opacity: 0.5,
    },
});

export default ChatFooter;
