package com.example.zpo_lab3_server;

import PackageAnswer.Answer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ServerController {
    private final int PORT = 4447;
    private final Path path = Path.of("pytania.txt");
    private ServerSocket serverSocket = null;
    private BlockingQueue<Socket> queue = null;
    private List<Listener> clients = null;
    private Listener listener = null;
    private Analyzer analyzer = null;
    private List<Pair<String,String>> pairList = null;
    private Pair<String,String> pairOf2string(String[] s){
        return new Pair<>(s[0], s[1]);
    }
    private Thread thread = null;
    private boolean isRun = false;
    private void getConnections(){
        Socket socket = null;
        isRun = true;
        while (isRun) {
            try {
                socket = serverSocket.accept();
                listener = new Listener(queue, socket);
                clients.add(listener);
            } catch (IOException e) {
                System.out.println("I/O error: " + e.getMessage());
            }
        }
        clients.forEach(Listener::Stop);
        clients.clear();
    }

    @FXML
    private TextArea textBox;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;
    @FXML
    public void initialize(){
        btnStop.setDisable(true);
        textBox.setText("Kliknij Start!");
        pairList = new ArrayList<>();
        clients = new ArrayList<>();
        try (BufferedReader questionBufferedReader = Files.newBufferedReader(path)){
            questionBufferedReader.lines().forEach(str -> pairList.add(pairOf2string(str.split("\\|",2))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //pairList.forEach(pair -> System.out.println(pair.getKey()+ " " + pair.getValue()));
        queue = new ArrayBlockingQueue<Answer>(10);
        analyzer = new Analyzer(queue,this);
    }
    @FXML
    private void btnStartClick(){
        btnStart.setDisable(true);
        startGame();
        btnStop.setDisable(false);
    }
    @FXML
    private void btnResetClick(){
        btnStop.setDisable(true);
        stopGame();
        btnStart.setDisable(false);
        appendTextln("Kliknij Start!");
    }

    private void setStartValue(){
        nextQuestionIndex = 0;
        text = "";
    }
    private void startGame(){
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textBox.setText("Oczekiwanie na dołączenie uczestnika...");
        System.out.println("Oczekiwanie na dołączenie uczestnika..." );
        thread = new Thread(this::getConnections);
        thread.start();
        setStartValue();
        nextQuestion();
    }

    private void stopGame(){
        isRun = false;
        try {
            if (serverSocket != null)
                serverSocket.close();
            if (thread != null)
                thread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        System.out.println("close");
        stopGame();
        if (analyzer != null)
            analyzer.Stop();
    }

    private Pair<String,String> currentQuestion;
    private String text;
    private Integer nextQuestionIndex;

    public void nextQuestion(){
        if (nextQuestionIndex >= pairList.size()){
            appendTextln("Koniec pytań!");
            stopGame();
        }else {
            currentQuestion = pairList.get(nextQuestionIndex);
            appendTextln(currentQuestion.getKey());
            nextQuestionIndex++;
        }
        queue.clear();
    }
    public boolean checkAnswer(String answer){
        return currentQuestion.getValue().trim().equalsIgnoreCase(answer.trim());
    }
    public void appendTextln(String str){
        text = text + str + "\n";
        textBox.setText(text);
    }
}