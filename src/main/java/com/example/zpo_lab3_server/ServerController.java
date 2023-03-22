package com.example.zpo_lab3_server;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingDeque;

public class ServerController {

    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private int port = 4447;
    //input & output
    private InputStream inputStream;
    private BufferedReader bufferedReader;

    private boolean b =true;
    @FXML
    public void initialize(){
        try {
            System.out.println("init");
            serverSocket = new ServerSocket(port);
            //socket = new Socket("hostname", port);
            socket = serverSocket.accept();
            //System.out.printf(socket.getLocalAddress().toString());
            inputStream = socket.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));



        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }

    @FXML
    private Text textBox;

}