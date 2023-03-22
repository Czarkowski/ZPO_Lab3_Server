package com.example.zpo_lab3_server;

import java.util.concurrent.BlockingDeque;

public class Listener implements Runnable{

    private BlockingDeque<Answer> queue;

    Listener(BlockingDeque<Answer> queue){
        this.queue = queue;
    }

    @Override
    public void run() {

    }
}
