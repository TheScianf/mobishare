import React from 'react';
import { Text, ScrollView, SafeAreaView } from 'react-native';
import {styles} from '../../StyleSheets/privacyPolicyStyle.tsx';

const PrivacyPolicyScreen = () => {

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.content}>


        <Text style={styles.section}>
          La tua privacy è importante per noi. Questa informativa descrive quali dati raccogliamo, come li utilizziamo e quali sono i tuoi diritti.
        </Text>

        <Text style={styles.heading}>1. Raccolta dei dati</Text>
        <Text style={styles.section}>
          Raccogliamo dati personali come email e password forniti al momento della registrazione. Possiamo anche raccogliere informazioni di utilizzo dell'app.
        </Text>

        <Text style={styles.heading}>2. Utilizzo dei dati</Text>
        <Text style={styles.section}>
          I tuoi dati vengono utilizzati per offrirti i nostri servizi, garantire la sicurezza e migliorare l’esperienza d’uso.
        </Text>

        <Text style={styles.heading}>3. Condivisione con terze parti</Text>
        <Text style={styles.section}>
          Non condividiamo le tue informazioni personali con terze parti senza consenso, salvo obblighi legali.
        </Text>

        <Text style={styles.heading}>4. Sicurezza</Text>
        <Text style={styles.section}>
          Applichiamo misure di sicurezza per proteggere i tuoi dati da accessi non autorizzati, alterazioni o divulgazioni.
        </Text>

        <Text style={styles.heading}>5. I tuoi diritti</Text>
        <Text style={styles.section}>
          Puoi richiedere accesso, modifica o cancellazione dei tuoi dati in qualsiasi momento contattandoci.
        </Text>

        <Text style={styles.heading}>6. Contattaci</Text>
        <Text style={styles.section}>
          Per qualsiasi domanda, scrivici a <Text style={styles.link}>support@mobishare.com</Text>.
        </Text>
      </ScrollView>
    </SafeAreaView>
  );
};

export default PrivacyPolicyScreen;
