package com.example.zpo_lab3_server;

import java.io.BufferedReader;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class Listener implements Runnable{


    private BlockingQueue<Answer> queue = null;
    private Socket socket;
    private BufferedReader bufferedReader;
    Listener(BlockingQueue<Answer> queue, Socket socket){
        this.queue = queue;
        this.socket = socket;
    }
    Listener(BlockingQueue<Answer> queue, BufferedReader bufferedReader){
        this.queue = queue;
        this.bufferedReader = bufferedReader;
    }
    private boolean stop;

    void Stop(){
        stop = true;
    }
    @Override
    public void run() {
        stop = false;
        while (!stop){





        }

    }
}
