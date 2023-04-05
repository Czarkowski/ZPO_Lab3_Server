package com.example.zpo_lab3_server;

import PackageAnswer.Answer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class Listener implements Runnable{
    private BlockingQueue<Socket> queue = null;
    private ServerSocket serverSocket= null;
    private ObjectInputStream objectInputStream = null;
    private Thread thread = null;
    Listener(BlockingQueue<Socket> queue, ServerSocket socket) {
        this.queue = queue;
        this.serverSocket = socket;
        try {
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        thread = new Thread(this);
        thread.start();
    }
    private boolean stop;
    public void Stop(){
        stop = true;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        stop = false;
        while (!stop) {
            try {
                    Socket socket = serverSocket.accept();
                    System.out.println("czekanie na odpowiedź\n");
                    //System.out.println("otrzymano odpowiedź od " + answer.getNick() + "\n");
                    queue.add(socket);
            }catch (IOException ex){
                System.out.println("I/O error: " + ex.getMessage());
            }catch (ClassNotFoundException ex){
                System.out.println("Class Not Found error: " + ex.getMessage());
            }
        }
        System.out.println("Listener stop");
    }
}
