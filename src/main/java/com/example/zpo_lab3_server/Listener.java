package com.example.zpo_lab3_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class Listener implements Runnable{


    private BlockingQueue<Answer> queue = null;
    private Socket socket;
    private ObjectInputStream objectInputStream = null;

            Listener(BlockingQueue<Answer> queue, ObjectInputStream objectInputStream){
        this.queue = queue;
        this.objectInputStream = objectInputStream;
    }

    private boolean stop;

    void Stop(){
        stop = true;
    }
    @Override
    public void run() {

        Answer answer = null;
        stop = false;
        while (!stop){
            try{
                answer = (Answer) objectInputStream.readObject();
                queue.add(answer);
            }catch (Exception ex){

            }




        }

    }
}
