import React, { useRef } from 'react';
import {
  View,
  TextInput,
  TouchableOpacity,
  TextInput as RNTextInput,
} from 'react-native';
import Icon from 'react-native-vector-icons/Feather';
import {styles} from '../../StyleSheets/searchBarStyle.tsx';

type Props = {
  value: string;
  onChangeText: (text: string) => void;
};

export default function SearchBar({ value, onChangeText }: Props) {
  const inputRef = useRef<RNTextInput>(null);

  const clearSearch = () => {
    onChangeText('');
    inputRef.current?.focus();
  };

  return (
      <View style={styles.searchContainer}>
        <Icon name="search" size={20} color="#888" style={styles.icon} />
        <TextInput
            ref={inputRef}
            style={styles.input}
            value={value}
            onChangeText={onChangeText}
            placeholder="Cerca..."
            placeholderTextColor="#999"
            returnKeyType="search"
        />
        {value.length > 0 && (
            <TouchableOpacity onPress={clearSearch}>
              <Icon name="x" size={20} color="#888" />
            </TouchableOpacity>
        )}
      </View>
  );
}
