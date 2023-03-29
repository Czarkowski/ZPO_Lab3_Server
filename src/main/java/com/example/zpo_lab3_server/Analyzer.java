package com.example.zpo_lab3_server;

import PackageAnswer.Answer;

import java.util.concurrent.BlockingQueue;

public class Analyzer implements Runnable{

    public void Stop() {
        this.stop = true;
    }

    private boolean stop;

    private BlockingQueue<Answer> queue = null;
    private ServerController serverController;

    public Analyzer(BlockingQueue<Answer> queue, ServerController serverController) {
        this.queue = queue;
        this.serverController = serverController;
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
                    serverController.checkAnswer(answer.getNick(), answer.getAnswer());

                } catch (InterruptedException ex) {
                    System.out.println("Interrupted error: " + ex.getMessage());
                }

        }
        System.out.println("Analyzer close");

    }
}
