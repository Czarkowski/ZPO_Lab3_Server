package com.example.zpo_lab3_server;

import java.util.concurrent.BlockingQueue;

public class Analyzer implements Runnable{

    private boolean stop;

    private BlockingQueue<Answer> queue = null;

    public Analyzer(BlockingQueue<Answer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Answer answer = null;
        stop = false;

        while (!stop){
            try {
                answer = queue.take();
            }catch (InterruptedException ex){

            }

        }

    }
}
