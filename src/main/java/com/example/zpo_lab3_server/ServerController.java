package com.example.zpo_lab3_server;

import PackageAnswer.Answer;
import javafx.fxml.FXML;
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
    private List<Pair<String,String>> pairList = new ArrayList<>();
    private Pair<String,String> pairOf2string(String[] s){
        return new Pair<>(s[0], s[1]);
    }
    @FXML
    public void initialize(){
        try {
            questionBufferedReader = Files.newBufferedReader(path);
            questionBufferedReader.lines().forEach(str -> pairList.add(pairOf2string(str.split("\\|",2))));
            pairList.forEach(pair -> System.out.println(pair.getKey()+ " " + pair.getValue()));

            System.out.println("init");
            serverSocket = new ServerSocket(port);
            //socket = new Socket("hostname", port);
            socket = serverSocket.accept();
            System.out.printf(socket.getLocalAddress().toString());
            inputStream = socket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));



        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
        queue = new ArrayBlockingQueue<Answer>(10);
        listener = new Listener(queue, objectInputStream);
        analyzer = new Analyzer(queue,this);

        questionIndex = -1;
        text = "";
        nextQuestion();

        threadListener = new Thread(listener);
        threadAnalyzer = new Thread(analyzer);
        threadListener.start();
        threadAnalyzer.start();
    }

    @FXML
    private Text textBox;
    private String text;
    private Integer questionIndex;

    private Pair<String,String> currentQuestion;
    public void nextQuestion(){
        questionIndex++;
        if (questionIndex == pairList.size()){
            listener.Stop();
            analyzer.Stop();
        }else {
            currentQuestion = pairList.get(questionIndex);
            text += currentQuestion.getKey() + "\n";
            textBox.setText(text);
            analyzer.setGoodAnswer(currentQuestion.getValue());
        }
    }
    public void checkAnswer(String nick, String answer){
        String str = nick +" odpowiedział: " + answer;
        if (answer.equals(currentQuestion.getValue())){
            str += " Dobrze!\n";
            text += str;
            textBox.setText(text);
            nextQuestion();
        }else {
            str += " Źle!\n";
            text += str;
            textBox.setText(text);
        }

    }
}