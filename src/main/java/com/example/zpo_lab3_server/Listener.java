package com.example.zpo_lab3_server;

import java.net.Socket;
import java.util.concurrent.BlockingDeque;

public class Listener implements Runnable{


    private BlockingDeque<Answer> queue = null;
    private Socket socket;
    Listener(BlockingDeque<Answer> queue, Socket socket){
        this.queue = queue;
        this.socket = socket;
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
