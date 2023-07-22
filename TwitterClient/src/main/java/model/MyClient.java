package model;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MyClient {

    private final String serverAddress = "127.0.0.1"; // replace with your server address
    private final int port = 9999; // replace with your server port

    public String sendRequest(String request, String requestType) {
        try {
            Socket socket = new Socket(serverAddress, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(requestType);
            out.println(request);
            String response = in.readLine();
            in.close();
            out.close();
            socket.close();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}