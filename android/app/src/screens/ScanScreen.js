import React from 'react';
import { View, Text, Button, StyleSheet } from 'react-native';

export default function ScanScreen() {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>Behavior Analysis</Text>
      <Text>No anomalies detected yet.</Text>
      <Button title="Run Scan" onPress={() => alert('Scan logic will go here')} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', alignItems: 'center' },
  title: { fontSize: 20, fontWeight: 'bold', marginBottom: 10 },
});