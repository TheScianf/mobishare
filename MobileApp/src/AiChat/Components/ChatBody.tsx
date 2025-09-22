import React, { useEffect, useRef } from 'react';
import {
    View,
    Text,
    FlatList,
    StyleSheet,
    Animated,
} from 'react-native';

interface Message {
    text: string;
    sender: 'user' | 'bot';
}

interface ChatBodyProps {
    messages: Message[];
    isLoading?: boolean;
}

const ChatBody = ({ messages, isLoading = false }: ChatBodyProps) => {
    const flatListRef = useRef<FlatList>(null);

    // Componente per i puntini di caricamento animati
    const LoadingDots = () => {
        const dot1Anim = useRef(new Animated.Value(0.3)).current;
        const dot2Anim = useRef(new Animated.Value(0.3)).current;
        const dot3Anim = useRef(new Animated.Value(0.3)).current;

        useEffect(() => {
            const animateDots = () => {
                const animationSequence = Animated.sequence([
                    Animated.timing(dot1Anim, { toValue: 1, duration: 400, useNativeDriver: true }),
                    Animated.timing(dot1Anim, { toValue: 0.3, duration: 400, useNativeDriver: true }),
                ]);

                const animationSequence2 = Animated.sequence([
                    Animated.delay(200),
                    Animated.timing(dot2Anim, { toValue: 1, duration: 400, useNativeDriver: true }),
                    Animated.timing(dot2Anim, { toValue: 0.3, duration: 400, useNativeDriver: true }),
                ]);

                const animationSequence3 = Animated.sequence([
                    Animated.delay(400),
                    Animated.timing(dot3Anim, { toValue: 1, duration: 400, useNativeDriver: true }),
                    Animated.timing(dot3Anim, { toValue: 0.3, duration: 400, useNativeDriver: true }),
                ]);

                Animated.parallel([animationSequence, animationSequence2, animationSequence3]).start(() => {
                    if (isLoading) { animateDots(); }
                });
            };

            if (isLoading) {
                animateDots();
            }
        }, [isLoading, dot1Anim, dot2Anim, dot3Anim]);

        return (
            <View style={styles.loadingContainer}>
                <View style={styles.loadingBubble}>
                    <View style={styles.dotsContainer}>
                        <Animated.View style={[styles.dot, { opacity: dot1Anim }]} />
                        <Animated.View style={[styles.dot, { opacity: dot2Anim }]} />
                        <Animated.View style={[styles.dot, { opacity: dot3Anim }]} />
                    </View>
                </View>
            </View>
        );
    };

    // Rendering dei messaggi con parsing del grassetto
    const renderMessage = ({ item }: { item: Message; index: number }) => {
        const isUser = item.sender === 'user';

        const parseText = (text: string) => {
            const parts = text.split(/(\*\*[^*]+\*\*|\*[^*]+\*|__[^_]+__|_[^_]+_)/g);

            return parts.map((part, index) => {
                if (part.match(/^\*\*[^*]+\*\*$/)) {
                    return (
                        <Text key={index} style={{ fontWeight: 'bold' }}>
                            {part.slice(2, -2)}
                        </Text>
                    );
                } else if (part.match(/^\*[^*]+\*$/)) {
                    return (
                        <Text key={index} style={{ fontWeight: 'bold' }}>
                            {part.slice(1, -1)}
                        </Text>
                    );
                } else if (part.match(/^__[^_]+__$/)) {
                    return (
                        <Text key={index} style={{ fontStyle: 'italic' }}>
                            {part.slice(2, -2)}
                        </Text>
                    );
                } else if (part.match(/^_[^_]+_$/)) {
                    return (
                        <Text key={index} style={{ fontStyle: 'italic' }}>
                            {part.slice(1, -1)}
                        </Text>
                    );
                } else {
                    return <Text key={index}>{part}</Text>;
                }
            });
        };

        return (
            <View
                style={[
                    styles.messageContainer,
                    isUser ? styles.userMessageContainer : styles.botMessageContainer,
                ]}>
                <View
                    style={[
                        styles.messageBubble,
                        isUser ? styles.userBubble : styles.botBubble,
                    ]}>
                    <Text
                        style={[
                            styles.messageText,
                            isUser ? styles.userText : styles.botText,
                        ]}>
                        {parseText(item.text)}
                    </Text>
                </View>
            </View>
        );
    };

    return (
        <View style={styles.container}>
            <FlatList
                ref={flatListRef}
                data={messages}
                keyExtractor={(item, index) => index.toString()}
                renderItem={renderMessage}
                inverted
                showsVerticalScrollIndicator={false}
                contentContainerStyle={styles.listContainer}
                ListHeaderComponent={isLoading ? <LoadingDots /> : null}
            />
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
    listContainer: {
        paddingVertical: 10,
    },
    messageContainer: {
        marginVertical: 4,
        paddingHorizontal: 16,
    },
    userMessageContainer: {
        alignItems: 'flex-end',
    },
    botMessageContainer: {
        alignItems: 'flex-start',
    },
    messageBubble: {
        maxWidth: '80%',
        paddingVertical: 10,
        paddingHorizontal: 16,
        borderRadius: 20,
    },
    userBubble: {
        backgroundColor: '#007AFF',
        borderBottomRightRadius: 6,
    },
    botBubble: {
        backgroundColor: '#E9ECEF',
        borderBottomLeftRadius: 6,
    },
    messageText: {
        fontSize: 16,
        lineHeight: 20,
    },
    userText: {
        color: '#FFFFFF',
    },
    botText: {
        color: '#000000',
    },
    loadingContainer: {
        paddingHorizontal: 16,
        paddingVertical: 8,
        alignItems: 'flex-start',
    },
    loadingBubble: {
        backgroundColor: '#E9ECEF',
        paddingVertical: 12,
        paddingHorizontal: 16,
        borderRadius: 20,
        borderBottomLeftRadius: 6,
    },
    dotsContainer: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    dot: {
        width: 8,
        height: 8,
        borderRadius: 4,
        backgroundColor: '#666666',
        marginHorizontal: 2,
    },
});

export default ChatBody;
