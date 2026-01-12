import {NativeModules} from 'react-native';

interface NavigationModuleInterface {
  pushReactFragmentA(): void;
  pushReactFragmentB(): void;
  pop(): void;
  checkActivityState(): Promise<string>;
  addListener(eventName: string): void;
  removeListeners(count: number): void;
}

const {NavigationModule} = NativeModules;

export default NavigationModule as NavigationModuleInterface;
