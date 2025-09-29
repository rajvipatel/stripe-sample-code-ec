import React, { useState } from "react";
import { loadConnectAndInitialize } from "@stripe/connect-js";
import {
  ConnectPayments,
  ConnectComponentsProvider,
} from "@stripe/react-connect-js";
import "./App.css";

export default function App() {
  const [stripeConnectInstance] = useState(() => {
    const fetchClientSecret = async () => {
      // Fetch the AccountSession client secret
      const response = await fetch('/account_session', { method: "POST" });
      if (!response.ok) {
        // Handle errors on the client side here
        const {error} = await response.json();
        console.log('An error occurred: ', error);
        return undefined;
      } else {
        const {client_secret: clientSecret} = await response.json();
        return clientSecret;
      }
    };
    return loadConnectAndInitialize({
        // This is a placeholder - it should be replaced with your publishable API key.
        // Sign in to see your own test API key embedded in code samples.
        // Don’t submit any personally identifiable information in requests made with this key.
        publishableKey: "pk_test_51JdH8WKFPdZAIqHpqRNr9L8CIcaan1B8YHue6xZK7wlK9z8rDN5R8a6iX68kbbScDg6UdCG0SfdmleeWgV7LhX8x00J9edMxFl",
        fetchClientSecret: fetchClientSecret,
        appearance: {
          overlays: 'dialog',
          variables: {
            colorPrimary: '#625afa',
          },
        },
      })
    });

  return (
    <div className="container">
        <ConnectComponentsProvider connectInstance={stripeConnectInstance}>
          <ConnectPayments />
        </ConnectComponentsProvider>
    </div>
  )
}
