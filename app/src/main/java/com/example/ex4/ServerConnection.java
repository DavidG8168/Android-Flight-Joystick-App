package com.example.ex4;
import android.os.AsyncTask;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
// The class starts the connection to the simulator.
// Extends AsyncTask to be able to override the doInBackground method
// that runs in a different thread.
public class ServerConnection extends AsyncTask<String, String, Void> {
    // The socket the connection will be made on.
    private Socket socket;
    // The buffer messages to the simulator will be send from.
    DataOutputStream buffer;
    // A commandQueue of strings holding the messages send in order.
    private BlockingQueue<String> commandQueue = new LinkedBlockingDeque<>();
    // Starts the connection, runs on a different thread.
    @Override
    protected Void doInBackground(String... strings) {
        try {
            this.socket = new Socket(strings[0], Integer.parseInt(strings[1]));
            this.buffer = new DataOutputStream(this.socket.getOutputStream());
            while(true) {
                this.buffer.write((commandQueue.take()+"\r\n").getBytes());
                this.buffer.flush();
            }
        }
        catch (Exception e) {
            System.out.println("" + e.getMessage());
         }
        return null;
    }
    // Place the command to be sent to the simulator in a buffer.
    public void Send(String commandString) {
        try {
            this.commandQueue.put(commandString);
        }
        catch (Exception e) {
            System.out.println("" + e.getMessage());
        }
    }
    // Close the socket.
    public void HaltConnection() {
        try {
            this.socket.close();
        } catch(Exception e) {
            System.out.println("" + e.getMessage());
        }
    }
}