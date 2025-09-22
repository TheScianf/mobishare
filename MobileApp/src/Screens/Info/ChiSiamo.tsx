import React from 'react';
import { ScrollView, Text } from 'react-native';
import {styles} from '../../StyleSheets/chiSiamoStyle.tsx';

const ChiSiamoScreen = () => {


  return (
    <ScrollView style={styles.container}>
      <Text style={styles.header}>Termini di Servizio</Text>

      <Text style={styles.paragraph}>
        Benvenuto nella nostra applicazione. Utilizzando i nostri servizi, accetti i seguenti termini e condizioni. Ti invitiamo a leggerli attentamente.
      </Text>

      <Text style={styles.sectionHeader}>1. Accettazione dei Termini</Text>
      <Text style={styles.paragraph}>
        L'accesso e l'utilizzo dell'app costituiscono accettazione integrale dei presenti Termini. Se non accetti questi Termini, ti invitiamo a non utilizzare l'app.
      </Text>

      <Text style={styles.sectionHeader}>2. Modifiche ai Termini</Text>
      <Text style={styles.paragraph}>
        Ci riserviamo il diritto di aggiornare o modificare i Termini in qualsiasi momento. Le modifiche entreranno in vigore al momento della pubblicazione.
      </Text>

      <Text style={styles.sectionHeader}>3. Uso Consentito</Text>
      <Text style={styles.paragraph}>
        L'app è destinata esclusivamente a un uso personale e non commerciale. Qualsiasi uso illecito o abusivo può comportare la sospensione o la cessazione dell'accesso.
      </Text>

      <Text style={styles.sectionHeader}>4. Privacy</Text>
      <Text style={styles.paragraph}>
        L'utilizzo dei nostri servizi è soggetto anche alla nostra Informativa sulla Privacy, che descrive come raccogliamo e utilizziamo i tuoi dati.
      </Text>

      <Text style={styles.sectionHeader}>5. Limitazione di Responsabilità</Text>
      <Text style={styles.paragraph}>
        Non saremo responsabili per eventuali danni diretti, indiretti o consequenziali derivanti dall'uso o dall'impossibilità di usare l'app.
      </Text>

      <Text style={styles.sectionHeader}>6. Contatti</Text>
      <Text style={styles.paragraph}>
        Per qualsiasi domanda relativa a questi Termini, puoi contattarci all'indirizzo email: supporto@tuaapp.com
      </Text>

      <Text style={styles.footer}>Ultimo aggiornamento: 8 maggio 2025</Text>
    </ScrollView>
  );
};



export default ChiSiamoScreen;
