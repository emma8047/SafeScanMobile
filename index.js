/**
 * @format
 */

import { AppRegistry } from 'react-native';
import App from './App';

AppRegistry.registerComponent('SafeScanMobile', () => App);

const functions = require('firebase-functions');
const admin = require('firebase-admin');

exports.processNewUser = functions.auth.user().onCreate((user) => {
    
    console.log("New user detected: " + user.email);
    
    return admin.firestore().collection('users').doc(user.uid).set({
        email: user.email,
        points: 0,
        status: "Newbie"
    });
});