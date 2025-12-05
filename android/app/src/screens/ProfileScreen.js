import React from 'react';
import { View, Text, StyleSheet } from 'react-native';

export default function TaskManagerScreen() {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>Task Manager</Text>
      <Text>CPU, RAM, Network usage will be displayed here.</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', alignItems: 'center' },
  title: { fontSize: 20, fontWeight: 'bold', marginBottom: 10 },
});
