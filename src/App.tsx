import React, {useState, useEffect} from 'react';
import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  NativeEventEmitter,
} from 'react-native';
import {NavigationContainer} from '@react-navigation/native';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import type {NativeStackScreenProps} from '@react-navigation/native-stack';
import NavigationModule from './NavigationModule';

type RootStackParamList = {
  FragmentA: undefined;
  FragmentB: undefined;
};

const Stack = createNativeStackNavigator<RootStackParamList>();

type FragmentAProps = NativeStackScreenProps<RootStackParamList, 'FragmentA'>;
type FragmentBProps = NativeStackScreenProps<RootStackParamList, 'FragmentB'>;

interface AppProps {
  fragmentId?: string;
}

// Fragment A Screen Component
function FragmentAScreen({navigation}: FragmentAProps): React.JSX.Element {
  useEffect(() => {
    // Listen to activity state change events from native
    const eventEmitter = new NativeEventEmitter(NavigationModule);
    const subscription = eventEmitter.addListener('onActivityStateChanged', (status: string) => {
      console.log('[Fragment A] Activity state changed event:', status);
    });

    return () => {
      subscription.remove();
    };
  }, []);

  // Auto-poll activity status every 500ms
  useEffect(() => {
    const intervalId = setInterval(() => {
      NavigationModule.checkActivityState()
        .then(status => {
          console.log('[Fragment A] Auto-check - Activity status:', status);
        })
        .catch(error => {
          console.error('[Fragment A] Auto-check error:', error);
        });
    }, 500);

    return () => {
      clearInterval(intervalId);
    };
  }, []);

  const handleGoToFragmentB = () => {
    console.log('[Fragment A] Navigating to Fragment B (Native Method)');
    NavigationModule.pushReactFragmentB();
  };

  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar barStyle="dark-content" />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={styles.scrollView}>
        <View style={styles.content}>
          <View style={styles.header}>
            <Text style={styles.title}>📱 Fragment A</Text>
            <Text style={styles.subtitle}>React Native Screen</Text>
          </View>

          <View style={styles.instructionCard}>
            <Text style={styles.instructionTitle}>📋 Instructions:</Text>
            <Text style={styles.instructionText}>
              1. Click "Go to Fragment B" to navigate to Fragment B
            </Text>
            <Text style={styles.instructionText}>
              2. Press back to return to Fragment A
            </Text>
            <Text style={styles.instructionText}>
              4. Check the toast and Logcat if Activity status shows "null" (the bug). Also the button on Fragment won't be clickable as the RN screen has lost activity reference.
            </Text>
          </View>

          <View style={styles.buttonSection}>
            <TouchableOpacity
              style={[styles.button, styles.buttonSuccess]}
              onPress={handleGoToFragmentB}>
              <Text style={styles.buttonText}>
                📱 Go to Fragment B
              </Text>
            </TouchableOpacity>
          </View>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

// Fragment B Screen Component
function FragmentBScreen({}: FragmentBProps): React.JSX.Element {
  return (
    <SafeAreaView style={[styles.safeArea, styles.safeAreaC]}>
      <StatusBar barStyle="light-content" />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={styles.scrollView}>
        <View style={styles.content}>
          <View style={[styles.header, styles.headerB]}>
            <Text style={styles.title}>🎯 Fragment B</Text>
            <Text style={styles.subtitle}>React Native Screen</Text>
          </View>

          <View style={styles.instructionCard}>
            <Text style={[styles.instructionTitle, styles.textDark]}>
              📋 Next Step:
            </Text>
            <Text style={[styles.instructionText, styles.textDark]}>
              Press the back button to return to Fragment A
            </Text>
          </View>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}


// Main App Component with React Navigation inside each fragment
function App({fragmentId = 'A'}: AppProps): React.JSX.Element {
  return (
    <NavigationContainer>
      <Stack.Navigator
        initialRouteName={fragmentId === 'B' ? 'FragmentB' : 'FragmentA'}
        screenOptions={{
          headerShown: false,
          animation: 'slide_from_right',
          presentation: 'card',
        }}>
        <Stack.Screen
          name="FragmentA"
          component={FragmentAScreen}
          options={{
            title: 'Fragment A',
          }}
        />
        <Stack.Screen
          name="FragmentB"
          component={FragmentBScreen}
          options={{
            title: 'Fragment B',
            animation: 'slide_from_right',
          }}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  screen: {
    flex: 1,
  },
  safeArea: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  safeAreaC: {
    backgroundColor: '#2C2C2C',
  },
  scrollView: {
    flex: 1,
  },
  content: {
    padding: 20,
  },
  header: {
    backgroundColor: '#4A90E2',
    padding: 20,
    borderRadius: 10,
    marginBottom: 20,
    alignItems: 'center',
  },
  headerB: {
    backgroundColor: '#9C27B0',
  },
  title: {
    fontSize: 28,
    fontWeight: 'bold',
    color: '#ffffff',
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 14,
    color: '#ffffff',
    opacity: 0.9,
    marginBottom: 8,
  },
  textDark: {
    color: '#333',
  },
  instructionCard: {
    backgroundColor: '#FFF3E0',
    padding: 20,
    borderRadius: 10,
    marginBottom: 20,
    borderLeftWidth: 4,
    borderLeftColor: '#FF9800',
  },
  instructionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#E65100',
    marginBottom: 12,
  },
  instructionText: {
    fontSize: 15,
    color: '#E65100',
    marginBottom: 8,
    lineHeight: 24,
  },
  buttonSection: {
    marginBottom: 20,
  },
  button: {
    padding: 16,
    borderRadius: 8,
    alignItems: 'center',
    marginBottom: 12,
    shadowColor: '#000',
    shadowOffset: {width: 0, height: 2},
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 2,
  },
  buttonPrimary: {
    backgroundColor: '#4A90E2',
  },
  buttonSuccess: {
    backgroundColor: '#50C878',
  },
  buttonText: {
    color: '#ffffff',
    fontSize: 16,
    fontWeight: '600',
  },
});

export default App;
