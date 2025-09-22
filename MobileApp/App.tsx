import React from 'react';
import {StatusBar} from 'react-native';
import RootNavigator from './src/Navigation/RootNavigator';
import {AuthProvider} from './src/Contexts/AuthContext';
import ChatWrapper from './src/AiChat/ChatWrapper.tsx';

const App = () => {
  return (
    <AuthProvider>
      <StatusBar barStyle="dark-content" backgroundColor="#fff" />
      <ChatWrapper>
        <RootNavigator />
      </ChatWrapper>
    </AuthProvider>
  );
};

export default App;
