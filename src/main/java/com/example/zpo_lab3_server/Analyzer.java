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

    private String goodAnswer;

    public void setGoodAnswer(String ans)
    {
        this.goodAnswer = ans;
    }
    @Override
    public void run() {
        Answer answer = null;
        stop = false;

        while (!stop){
            try {
                answer = queue.take();
                serverController.checkAnswer(answer.getNick(),answer.getAnswer());

            }catch (InterruptedException ex){

            }

        }

    }
}
