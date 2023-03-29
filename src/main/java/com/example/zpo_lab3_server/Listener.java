package com.example.zpo_lab3_server;

import PackageAnswer.Answer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class Listener implements Runnable{


    private BlockingQueue<Answer> queue = null;
    private Socket socket;
    private ObjectInputStream objectInputStream = null;

    Listener(BlockingQueue<Answer> queue, ObjectInputStream objectInputStream) {
        this.queue = queue;
        this.objectInputStream = objectInputStream;
    }

    private boolean stop;

    void Stop(){
        stop = true;
    }
    @Override
    public void run() {


        stop = false;
        while (!stop){
            try{
                Answer answer = null;
                System.out.println("czekanie na odpowiedź\n");
                answer = (Answer) objectInputStream.readObject();
                System.out.println("otrzymano odpowiedź od " + answer.getNick()+"\n");
                queue.add(answer);
            }catch (IOException ex){
                System.out.println("I/O error: " + ex.getMessage());
            }catch (ClassNotFoundException ex){
                System.out.println("Class Not Found error: " + ex.getMessage());
            }
        }
        System.out.println("Listener close");
    }
}
