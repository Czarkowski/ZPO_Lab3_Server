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
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ServerController {

    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private int port = 4447;
    //input & output
    private InputStream inputStream;
    private BufferedReader questionBufferedReader;

    private ObjectInputStream objectInputStream = null;

    private BlockingQueue<Answer> queue = null;
    private Listener listener = null;

    private Analyzer analyzer = null;

    private final Path path = Path.of("pytania.txt");
    private BufferedReader bufferedReader = null;

    private boolean b =true;
    private Thread threadListener = null;
    private Thread threadAnalyzer = null;
    private List<Pair<String,String>> pairList = null;
    private Pair<String,String> pairOf2string(String[] s){
        return new Pair<>(s[0], s[1]);
    }


    @FXML
    public void initialize(){
        btnStop.setDisable(true);
    }

    public void close(){
        btnStop.setDisable(true);
        System.out.println("close");
        if (listener != null)
            listener.Stop();
        if (analyzer != null)
            analyzer.Stop();
        try {
            if (serverSocket != null)
                serverSocket.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (threadListener != null)
            threadListener.join();
            if (threadAnalyzer != null)
            threadAnalyzer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        btnStart.setDisable(false);

    }
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;
    @FXML
    private void btnStartClick(){
        btnStart.setDisable(true);
        textBox.setText("Oczekiwanie na dołączenie uczestnika...");
        System.out.println("Oczekiwanie na dołączenie uczestnika..." );
        try {
            pairList = new ArrayList<>();
            questionBufferedReader = Files.newBufferedReader(path);
            questionBufferedReader.lines().forEach(str -> pairList.add(pairOf2string(str.split("\\|",2))));
            questionBufferedReader.close();
            pairList.forEach(pair -> System.out.println(pair.getKey()+ " " + pair.getValue()));


            serverSocket = new ServerSocket(port);

            socket = serverSocket.accept();
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        }catch (Exception e){
            System.out.println("Start error!");
            e.printStackTrace();
            Platform.exit();
        }


        queue = new ArrayBlockingQueue<Answer>(10);
        listener = new Listener(queue, objectInputStream);
        analyzer = new Analyzer(queue,this);
        threadListener = new Thread(listener);
        threadAnalyzer = new Thread(analyzer);

        questionIndex = -1;
        text = "";
        nextQuestion();

        threadListener.start();
        threadAnalyzer.start();
        btnStop.setDisable(false);
    }
    @FXML
    private void btnStopClick(){

        textBox.setText("Zamykanie wątków...");
        close();
        textBox.setText("Zakończono!");

    }

    @FXML
    private Text textBox;
    private String text;
    private Integer questionIndex;

    private Pair<String,String> currentQuestion;
    private void nextQuestion(){
        questionIndex++;
        queue.clear();
        if (questionIndex == pairList.size()){
            appendTextnl("Koniec pytań!");
            //close();
            analyzer.Stop();
        }else {
            currentQuestion = pairList.get(questionIndex);
            appendTextnl(currentQuestion.getKey());
        }
    }
    public void checkAnswer(String nick, String answer){
        String str = nick +" odpowiedział: " + answer;
        if (answer.equals(currentQuestion.getValue())){
            str += " Dobrze!";
            appendTextnl(str);
            nextQuestion();
        }else {
            str += " Źle!";
            appendTextnl(str);
        }

    }

    private void appendTextnl(String str){
        text = text + "\n" + str;
        textBox.setText(text);
    }
}