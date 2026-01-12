import {NativeModules, NativeModule} from 'react-native';

interface NavigationModuleInterface extends NativeModule {
  pushReactFragmentA(): void;
  pushReactFragmentB(): void;
  pop(): void;
  checkActivityState(): Promise<string>;
}

const {NavigationModule} = NativeModules;

export default NavigationModule as NavigationModuleInterface;
