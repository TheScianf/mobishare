import React from 'react';
import { Text, ScrollView, SafeAreaView} from 'react-native';
import {styles} from '../../StyleSheets/termsOfServiseStyle.tsx';

const TermsOfServiceScreen = () => {

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.content}>


        <Text style={styles.paragraph}>
          Accedendo o utilizzando l'app MobiShare, accetti i seguenti termini. Ti invitiamo a leggerli attentamente.
        </Text>

        <Text style={styles.heading}>1. Uso dell'app</Text>
        <Text style={styles.paragraph}>
          L'utente si impegna a utilizzare l'app in modo lecito e conforme alle normative vigenti.
        </Text>

        <Text style={styles.heading}>2. Account</Text>
        <Text style={styles.paragraph}>
          Sei responsabile della riservatezza delle credenziali del tuo account e di tutte le attività svolte con esso.
        </Text>

        <Text style={styles.heading}>3. Contenuti</Text>
        <Text style={styles.paragraph}>
          È vietato caricare, condividere o distribuire contenuti illeciti, offensivi o che violino diritti di terzi.
        </Text>

        <Text style={styles.heading}>4. Limitazione di responsabilità</Text>
        <Text style={styles.paragraph}>
          Non siamo responsabili per danni diretti o indiretti derivanti dall’uso dell’app.
        </Text>

        <Text style={styles.heading}>5. Modifiche</Text>
        <Text style={styles.paragraph}>
          Ci riserviamo il diritto di modificare i presenti termini in qualsiasi momento, previa comunicazione.
        </Text>

        <Text style={styles.heading}>6. Contatti</Text>
        <Text style={styles.paragraph}>
          Per domande o chiarimenti, contattaci all'indirizzo support@mobishare.com.
        </Text>
      </ScrollView>
    </SafeAreaView>
  );
};

export default TermsOfServiceScreen;
