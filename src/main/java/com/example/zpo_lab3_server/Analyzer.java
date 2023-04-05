package com.example.zpo_lab3_server;

import PackageAnswer.Answer;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class Analyzer implements Runnable{
    private BlockingQueue<Answer> queue = null;
    private ServerController serverController = null;
    private Thread thread = null;
    public Analyzer(BlockingQueue<Answer> queue, ServerController serverController) {
        this.queue = queue;
        this.serverController = serverController;
        thread = new Thread(this);
        thread.start();
    }
    private boolean stop;
    public void Stop() {
        this.stop = true;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        Answer answer = null;
        stop = false;
        while (!stop) {
            if (queue.size() > 0)
                try {
                    System.out.println("Pobieranie z kolejki\n");
                    answer = queue.take();
                    System.out.println("Pobrano z kolejki " + answer.getNick() + "\n");
                    String str = answer.getNick() +" odpowiedział: " + answer.getAnswer();
                    if (serverController.checkAnswer(answer.getAnswer())){
                        str += " Dobrze!";
                        serverController.appendTextln(str);
                        serverController.nextQuestion();
                    }else {
                        str += " Źle!";
                        serverController.appendTextln(str);
                    }
                } catch (InterruptedException ex) {
                    System.out.println("Interrupted error: " + ex.getMessage());
                }
        }
        System.out.println("Analyzer stop");
    }
}
