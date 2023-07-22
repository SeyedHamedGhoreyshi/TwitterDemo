module com.twitter.twitterclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.auth0.jwt;
    requires com.google.gson;
    requires java.xml;
    requires com.fasterxml.jackson.databind;


    opens com.twitter.twitterclient to javafx.fxml;
    exports com.twitter.twitterclient;
    exports controller;
    opens controller to javafx.fxml;
    opens model to com.google.gson;
}