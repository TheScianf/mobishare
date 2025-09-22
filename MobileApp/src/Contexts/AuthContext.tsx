import authRequests from '../Requests/AuthRequests';
import React, {createContext, ReactNode, useContext, useEffect} from 'react';
import {MMKVLoader, useMMKVStorage} from 'react-native-mmkv-storage';

// Definisci il tipo del contesto
export class AuthEntity {
  public static readonly ANONYMOUS: Number = 0;
  public static readonly CUSTOMER: Number = 1;
  public static readonly MANAGER: Number = 2;
  public static readonly ADMIN: Number = 3;
  public static readonly SUSPENDED: Number = 4;
  public static readonly P_SUSPENDED: Number = 5;

  readonly username: string | undefined;
  readonly token: string | undefined;
  readonly role: Number;
  readonly faceLoginDate: Date | undefined | false;

  constructor(
    username: string | undefined = undefined,
    token: string | undefined = undefined,
    role: Number = 0,
    faceLoginDate: Date | undefined | false = undefined,
  ) {
    this.token = token;
    this.username = username;
    this.role = role;
    this.faceLoginDate = faceLoginDate;
  }
}

const storage = new MMKVLoader().initialize();

interface AuthContextType {
  isAuthenticated: AuthEntity;
  setIsAuthenticated: (value: AuthEntity) => void;
}

// Crea il contesto con valori iniziali
const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Provider del contesto
export const AuthProvider = ({children}: {children: ReactNode}) => {
  const defaultAuth = new AuthEntity(
    undefined,
    undefined,
    AuthEntity.ANONYMOUS,
  );
  const [isAuthenticated, setIsAuthenticated] = useMMKVStorage<AuthEntity>(
    'auth',
    storage,
    defaultAuth,
  );

  // Effetto per gestire il caricamento iniziale
  useEffect(() => {
    const init = async () => {
      try {
        if (isAuthenticated.token) {
          const valid = await authRequests.checkToken(isAuthenticated);
          if (!valid) {
            // Token non valido, reimposta tutto
            setIsAuthenticated(defaultAuth);
          } else {
            if (isAuthenticated.role === AuthEntity.CUSTOMER) {
              if (
                isAuthenticated.faceLoginDate !== false &&
                isAuthenticated.faceLoginDate !== undefined
              ) {
                // Se la data di login facciale è definita, controlla se è scaduta
                const now = new Date();
                const faceLoginDate = new Date(isAuthenticated.faceLoginDate);
                if (
                  now.getTime() - faceLoginDate.getTime() >
                  24 * 60 * 60 * 1000 * 7
                ) {
                  // setta scaduta (false)
                  setIsAuthenticated(
                    new AuthEntity(
                      isAuthenticated.username,
                      isAuthenticated.token,
                      isAuthenticated.role,
                      false,
                    ),
                  );
                }
              }
            }
          }
        }
      } catch (error) {
        console.error('Errore durante il controllo del token:', error);
        setIsAuthenticated(defaultAuth);
      }
    };

    init();
  }, []); // Esegui solo al mount del componente

  return (
    <AuthContext.Provider value={{isAuthenticated, setIsAuthenticated}}>
      {children}
    </AuthContext.Provider>
  );
};

// Hook per usare il contesto
export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
