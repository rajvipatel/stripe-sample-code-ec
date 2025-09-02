# Integrate with Connect embedded components

Build a full, working integration using Connect embedded components. Here are some basic
scripts you can use to build and run the application.

## Replace the following variables

Ensure that you have replaced the following placeholders in the downloaded code sample:
- {{CONNECTED_ACCOUNT_ID}}
- pk_INSERT_YOUR_PUBLISHABLE_KEY
- sk_INSERT_YOUR_SECRET_KEY

## Run the sample

1. Build the server

~~~
mvn package
~~~

2. Run the server

~~~
java -cp target/sample-jar-with-dependencies.jar com.stripe.sample.Server
~~~

3. Build the client app

~~~
npm install
~~~

4. Run the client app

~~~
npm start
~~~

5. Go to [http://localhost:4242/index.html](http://localhost:4242/index.html)