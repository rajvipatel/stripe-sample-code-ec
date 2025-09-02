# Deployment Guide for Render

This guide explains how to deploy your Stripe Connect sample application to Render using the provided Dockerfile.

## Prerequisites

Before deploying, ensure you have:

1. **Stripe API Keys**: Replace the placeholder values in your code:
   - `sk_INSERT_YOUR_SECRET_KEY` in `Server.java`
   - `pk_INSERT_YOUR_PUBLISHABLE_KEY` in your frontend code
   - `{{CONNECTED_ACCOUNT_ID}}` in `Server.java`

2. **GitHub Repository**: Your code should be pushed to a GitHub repository that Render can access.

## Deployment Steps

### 1. Create a New Web Service on Render

1. Go to [Render Dashboard](https://dashboard.render.com/)
2. Click "New +" and select "Web Service"
3. Connect your GitHub repository containing this code

### 2. Configure the Service

Use the following settings:

- **Name**: `stripe-connect-app` (or your preferred name)
- **Environment**: `Docker`
- **Region**: Choose your preferred region
- **Branch**: `main` (or your default branch)
- **Root Directory**: Leave empty (unless your code is in a subdirectory)

### 3. Environment Variables

Add the following environment variables in Render:

- `STRIPE_SECRET_KEY`: Your Stripe secret key (starts with `sk_`)
- `STRIPE_PUBLISHABLE_KEY`: Your Stripe publishable key (starts with `pk_`)
- `CONNECTED_ACCOUNT_ID`: Your connected account ID
- `PORT`: `4242` (should match the port in your Dockerfile)

### 4. Advanced Settings

- **Auto-Deploy**: Enable this to automatically deploy when you push to your repository
- **Health Check Path**: `/` (optional, but recommended)

### 5. Deploy

1. Click "Create Web Service"
2. Render will automatically build your Docker image and deploy it
3. The build process will take several minutes as it builds both the Java backend and React frontend

## Build Process

The Dockerfile uses a multi-stage build:

1. **Stage 1**: Builds the Java backend using Maven
2. **Stage 2**: Builds the React frontend using npm and Parcel
3. **Stage 3**: Creates the runtime image with both components

## Accessing Your Application

Once deployed, your application will be available at:
```
https://your-service-name.onrender.com
```

The Java server runs on port 4242 and serves both the API endpoints and static frontend files.

## Environment Variables Setup

You'll need to update your code to use environment variables instead of hardcoded values:

### Java Backend (Server.java)
```java
// Replace this line:
Stripe.apiKey = "sk_INSERT_YOUR_SECRET_KEY";

// With:
Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY");

// And replace:
params.put("account", "{{CONNECTED_ACCOUNT_ID}}");

// With:
params.put("account", System.getenv("CONNECTED_ACCOUNT_ID"));
```

### Frontend
Update your frontend code to use the publishable key from environment variables or configuration.

## Troubleshooting

### Build Failures
- Check that all required files are present in your repository
- Ensure your `package.json` and `pom.xml` are valid
- Check the build logs in Render for specific error messages

### Runtime Issues
- Verify all environment variables are set correctly
- Check the application logs in Render dashboard
- Ensure your Stripe keys are valid and have the correct permissions

### Port Issues
- The application runs on port 4242 internally
- Render automatically handles external port mapping
- Make sure the `PORT` environment variable is set to `4242`

## Local Testing

To test the Docker build locally:

```bash
# Build the image
docker build -t stripe-connect-app .

# Run the container
docker run -p 4242:4242 \
  -e STRIPE_SECRET_KEY=your_secret_key \
  -e CONNECTED_ACCOUNT_ID=your_account_id \
  stripe-connect-app
```

Then visit `http://localhost:4242` to test your application.

## Security Notes

- Never commit your actual Stripe API keys to version control
- Use Render's environment variables for all sensitive configuration
- Ensure your Stripe keys have the minimum required permissions
- Consider using Stripe's test keys for development deployments
