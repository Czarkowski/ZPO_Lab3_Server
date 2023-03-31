package com.example.zpo_lab3_server;

import PackageAnswer.Answer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private BlockingQueue<Answer> queue = null;
    private Listener listener = null;
    private Analyzer analyzer = null;

    private List<Pair<String,String>> pairList = null;
    private Pair<String,String> pairOf2string(String[] s){
        return new Pair<>(s[0], s[1]);
    }


    private void getConnection(){
        Socket socket = null;
        try {
            socket = serverSocket.accept();
            listener = new Listener(queue, socket);
            setStartValue();
            nextQuestion();
            btnStop.setDisable(false);
        } catch (IOException e) {
            e.printStackTrace();
            btnStart.setDisable(false);
        }
    }

    @FXML
    private Text textBox;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;

    @FXML
    public void initialize(){
        btnStop.setDisable(true);
        textBox.setText("Kliknij Start!");
        pairList = new ArrayList<>();
        try (BufferedReader questionBufferedReader = Files.newBufferedReader(path)){
            questionBufferedReader.lines().forEach(str -> pairList.add(pairOf2string(str.split("\\|",2))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //pairList.forEach(pair -> System.out.println(pair.getKey()+ " " + pair.getValue()));
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        queue = new ArrayBlockingQueue<Answer>(10);
        analyzer = new Analyzer(queue,this);
    }

    @FXML
    private void btnStartClick(){
        startGame();
    }

    @FXML
    private void btnResetClick(){
        resetGame();
    }

    private void setStartValue(){
        nextQuestionIndex = 0;
        text = "";
    }
    private void startGame(){
        btnStart.setDisable(true);
        textBox.setText("Oczekiwanie na dołączenie uczestnika...");
        System.out.println("Oczekiwanie na dołączenie uczestnika..." );
        new Thread(this::getConnection).start();
    }

    private void resetGame(){
        btnStop.setDisable(true);
        if (listener != null)
            listener.Stop();
        btnStart.setDisable(false);
        appendTextln("Kliknij Start!");
    }

    public void close(){
        System.out.println("close");
        if (listener != null)
            listener.Stop();
        if (analyzer != null)
            analyzer.Stop();
        try {
            if (serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Pair<String,String> currentQuestion;
    private String text;
    private Integer nextQuestionIndex;

    public void nextQuestion(){
        if (nextQuestionIndex >= pairList.size()){
            appendTextln("Koniec pytań!");
            resetGame();
        }else {
            currentQuestion = pairList.get(nextQuestionIndex);
            appendTextln(currentQuestion.getKey());
            nextQuestionIndex++;
        }
        queue.clear();
    }
    public boolean checkAnswer(String answer){
        return currentQuestion.getValue().equals(answer);
    }

    public void appendTextln(String str){
        text = text + str + "\n";
        textBox.setText(text);
    }
}