import React, { useState, useRef, useEffect } from 'react';
import {
  View,
  Modal,
  StyleSheet,
  Dimensions,
  TouchableWithoutFeedback,
  Animated,
  StatusBar,
  Platform,
  Text,
  TouchableOpacity,
  Alert,
  Keyboard,
} from 'react-native';
import Icon from 'react-native-vector-icons/MaterialIcons';
import ChatFooter from './Components/ChatFooter';
import ChatBody from './Components/ChatBody';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import {sendMessage} from '../Requests/AiRequests.ts';
import {useAuth} from '../Contexts/AuthContext.tsx';

interface Message {
  text: string;
  sender: 'user' | 'bot';
}

interface ChatScreenProps {
  onClose: () => void;
}

const { width: screenWidth, height: screenHeight } = Dimensions.get('window');

const ChatScreen = ({ onClose }: ChatScreenProps) => {
  const [messages, setMessages] = useState<Message[]>([]);
  const [isVisible, setIsVisible] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [keyboardHeight, setKeyboardHeight] = useState(0);

  const fadeAnim = useRef(new Animated.Value(0)).current;
  const scaleAnim = useRef(new Animated.Value(0.8)).current;
  const slideAnim = useRef(new Animated.Value(50)).current;

  const {isAuthenticated} = useAuth();

  useEffect(() => {
    setIsVisible(true);
    Animated.parallel([
      Animated.timing(fadeAnim, { toValue: 1, duration: 400, useNativeDriver: true }),
      Animated.spring(scaleAnim, { toValue: 1, tension: 100, friction: 8, useNativeDriver: true }),
      Animated.timing(slideAnim, { toValue: 0, duration: 400, useNativeDriver: true }),
    ]).start();
  }, [fadeAnim, scaleAnim, slideAnim]);

  useEffect(() => {
    const keyboardDidShowListener = Keyboard.addListener(
      'keyboardDidShow',
      (e) => {
        setKeyboardHeight(e.endCoordinates.height);
      }
    );

    const keyboardDidHideListener = Keyboard.addListener(
      'keyboardDidHide',
      () => {
        setKeyboardHeight(0);
      }
    );

    return () => {
      keyboardDidShowListener.remove();
      keyboardDidHideListener.remove();
    };
  }, []);

  const handleSend = async (text: string) => {
    Keyboard.dismiss();

    const userMessage: Message = { text, sender: 'user' };
    setMessages((prev) => [userMessage, ...prev]);
    setIsLoading(true);

    const responseMsg = await sendMessage(
      isAuthenticated.token!,
      isAuthenticated.username!,
      isAuthenticated.role! as number,
      userMessage.text
    );

    setIsLoading(false);
    console.log(responseMsg);
    if (typeof responseMsg === 'string') {
      setMessages((prev) => [{ text: responseMsg, sender: 'bot' }, ...prev]);
    } else {
      Alert.alert('Errore', responseMsg.error);
    }
  };

  const handleClose = () => {
    Animated.parallel([
      Animated.timing(fadeAnim, { toValue: 0, duration: 250, useNativeDriver: true }),
      Animated.timing(scaleAnim, { toValue: 0.8, duration: 250, useNativeDriver: true }),
      Animated.timing(slideAnim, { toValue: 50, duration: 250, useNativeDriver: true }),
    ]).start(() => {
      setIsVisible(false);
      onClose();
    });
  };

  if (!isVisible) return null;

  return (
      <Modal transparent animationType="none" visible={isVisible} onRequestClose={handleClose} statusBarTranslucent>
        <StatusBar backgroundColor="rgba(0,0,0,0.5)" barStyle="light-content" />
        <TouchableWithoutFeedback onPress={handleClose}>
          <View style={styles.backdrop} />
        </TouchableWithoutFeedback>

        <View style={styles.modalContainer}>
          <Animated.View
              style={[
                styles.modalContent,
                {
                  opacity: fadeAnim,
                  transform: [{ scale: scaleAnim }, { translateY: slideAnim }],
                  height: screenHeight * 0.85 - keyboardHeight,
                },
              ]}
          >
            {/* Header del Bot */}
            <View style={styles.header}>
              <View style={styles.botInfo}>
                <View style={styles.botAvatar}>
                  <Icon name="smart-toy" size={20} color="#FFFFFF" />
                </View>
                <View style={styles.botDetails}>
                  <Text style={styles.botName}>MobiShare Bot</Text>
                  <View style={styles.statusContainer}>
                    <View style={styles.onlineIndicator} />
                    <Text style={styles.statusText}>
                      {isLoading ? 'Sta scrivendo...' : 'Online'}
                    </Text>
                  </View>
                </View>
              </View>
              <TouchableOpacity onPress={handleClose} style={styles.closeButton}>
                <MaterialIcons name="close" size={24} color="#666666" />
              </TouchableOpacity>
            </View>

            <View style={styles.body}>
              <ChatBody messages={messages} isLoading={isLoading} />
            </View>
            <ChatFooter onSend={handleSend} />
          </Animated.View>
        </View>
      </Modal>
  );
};

const styles = StyleSheet.create({
  backdrop: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor: 'rgba(0,0,0,0.6)',
  },
  modalContainer: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'center',
    paddingHorizontal: 20,
    paddingTop: Platform.OS === 'android' ? StatusBar.currentHeight : 0,
  },
  modalContent: {
    width: screenWidth - 40,
    height: screenHeight * 0.85,
    backgroundColor: 'white',
    borderRadius: 24,
    overflow: 'hidden',
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 20,
    paddingVertical: 16,
    backgroundColor: '#F8F9FA',
    borderBottomWidth: 1,
    borderBottomColor: '#E9ECEF',
  },
  botInfo: {
    flexDirection: 'row',
    alignItems: 'center',
    flex: 1,
  },
  botAvatar: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: '#007AFF',
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: 12,
  },
  botDetails: {
    flex: 1,
  },
  botName: {
    fontSize: 16,
    fontWeight: '600',
    color: '#000000',
    marginBottom: 2,
  },
  statusContainer: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  onlineIndicator: {
    width: 8,
    height: 8,
    borderRadius: 4,
    backgroundColor: '#34C759',
    marginRight: 6,
  },
  statusText: {
    fontSize: 12,
    color: '#666666',
  },
  closeButton: {
    width: 32,
    height: 32,
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 16,
    backgroundColor: '#F0F0F0',
  },
  body: {
    flex: 1,
    padding: 16,
  },
});

export default ChatScreen;
