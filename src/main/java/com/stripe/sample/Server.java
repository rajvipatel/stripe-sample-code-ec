package com.stripe.sample;

import com.google.gson.Gson;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.post;
import static spark.Spark.port;
import static spark.Spark.staticFiles;

import com.stripe.Stripe;
import com.stripe.model.AccountSession;

public class Server {
  private static Gson gson = new Gson();

  static class ErrorResponse {
    private String error;

    public ErrorResponse(String error) {
      this.error = error;
    }
  }

  static class CreateAccountSessionResponse {
    private String client_secret;

    public CreateAccountSessionResponse(String clientSecret) {
      this.client_secret = clientSecret;
    }
  }

  public static void main(String[] args) {
    port(4242);
    staticFiles.externalLocation(Paths.get("dist").toAbsolutePath().toString());

    // This is a placeholder - it should be replaced with your secret API key.
    // Sign in to see your own test API key embedded in code samples.
    // Don’t submit any personally identifiable information in requests made with this key.
    Stripe.apiKey = "sk_INSERT_YOUR_SECRET_KEY";

    post("/account_session", (request, response) -> {
      response.type("application/json");

      try {
        
        Map<String, Object> params = new HashMap<>();
        params.put("account", "{{CONNECTED_ACCOUNT_ID}}");
        Map<String, Object> payments = new HashMap<>();
        payments.put("enabled", true);

        Map<String, Object> features = new HashMap<>();
        features.put("refund_management", true);
        features.put("dispute_management", true);
        features.put("capture_payments", true);
        payments.put("features", features);

        Map<String, Object> components = new HashMap<>();
        components.put("payments", payments);
        params.put("components", components);
        AccountSession accountSession = AccountSession.create(params);
        CreateAccountSessionResponse accountSessionResponse = new CreateAccountSessionResponse(accountSession.getClientSecret());
        return gson.toJson(accountSessionResponse);
      } catch(Exception e) {
        System.out.println("An error occurred when calling the Stripe API to create an account session: " + e.getMessage());
        response.status(500);
        return gson.toJson(new ErrorResponse(e.getMessage()));
      }
    });
  }
}