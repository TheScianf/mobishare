import React, {useState} from 'react';
import {View, StyleSheet} from 'react-native';
import ChatButton from './ChatButton.tsx';
import ChatScreen from './ChatScreen.tsx';
import {useAuth} from '../Contexts/AuthContext.tsx';

interface ChatWrapperProps {
  children: React.ReactNode;
}

const ChatWrapper: React.FC<ChatWrapperProps> = ({children}) => {
  const [chatOpen, setChatOpen] = useState<Boolean>(false);
  const {isAuthenticated} = useAuth();

  if (isAuthenticated.role === 0) {
    return <>{children}</>;
  }


  return (
    <View style={styles.wrapper}>
      {children}
      {!chatOpen ? (
        <View style={styles.floatingButton}>
          <ChatButton
            onPress={() => {
              setChatOpen(!chatOpen);
            }}
          />
        </View>
      ) : (
        <View style={styles.floatingChat}>
          <ChatScreen
            onClose={() => {
              setChatOpen(false);
            }}
          />
        </View>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  wrapper: {
    flex: 1,
  },
  floatingButton: {
    position: 'absolute',
    bottom: 110,
    right: 20,
    zIndex: 1000,
  },
  floatingChat: {
  },
});

export default ChatWrapper;
