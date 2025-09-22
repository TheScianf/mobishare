import React from 'react';
import {Image, StyleSheet, TouchableOpacity} from 'react-native';

interface ChatButtonProps {
  onPress: () => void;
  red?: boolean;
}

const ChatButton = (props: ChatButtonProps)=> {
  return (
    <TouchableOpacity style={styles.button} onPress={props.onPress}>
      <Image
        source={require('../../assets/chatbot_32.png')}
        style={styles.image}
      />
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  button: {
    backgroundColor: '#00a6ff',
    borderRadius: 32,
    width: 64,
    height: 64,
    justifyContent: 'center',
    alignItems: 'center',
    elevation: 3,
  },
  image: {
    width: 32,
    height: 32,
  },
});

export default ChatButton;
