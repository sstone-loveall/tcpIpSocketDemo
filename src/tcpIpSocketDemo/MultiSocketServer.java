package tcpIpSocketDemo;

import java.net.*;
import java.io.*;

/*
 * Tutorial from http://edn.embarcadero.com/article/31995
 * A multiple socket server: processes multiple sockets at a time
 */
public class MultiSocketServer implements Runnable {

    public static final String ASCII_ENCODING = "US-ASCII";
    public static final int PORT = 19999;
    public static final int TERMINAL_CHAR = 13;
    
    private static int connectionCount;
    private static ServerSocket socket;
    
    private Socket connection;
    private String timeStamp;
    
    private MultiSocketServer(Socket s, int i) {
        this.connection = s;
    }
    
    /***
     * Process the input coming from the socket
     */
    @Override
    public void run() {
        
        try {
            // read the input
            BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
            InputStreamReader isr = new InputStreamReader(is);
            int character;
            StringBuffer process = new StringBuffer();
            while( (character = isr.read()) != TERMINAL_CHAR) {
                process.append( (char) character);
            }
            
            System.out.println("Input received:");
            System.out.println(process);
            
            waitTenSeconds();
            
            // respond
            timeStamp = new java.util.Date().toString();
            String returnCode = "MultiSocketServer responded at " + timeStamp + (char) TERMINAL_CHAR;
            BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream());
            OutputStreamWriter osw = new OutputStreamWriter(os, ASCII_ENCODING);
            osw.write(returnCode);
            osw.flush();
        } 
        catch (IOException e) {
            System.out.println(e);
        }
        finally {
            try {
                connection.close();
                System.out.println("Connection " + connection.hashCode() + " closed at " + (new java.util.Date().toString()));
            }
            catch (IOException f) {}
        }
    }
    
    /***
     * Wait 10 seconds: this is for demo purposes only. It pretends something is processing.
     */
    private void waitTenSeconds() {
        try {
            Thread.sleep(10000);
        }
        catch (Exception e) {}
    }
    
    /***
     * Start the MultiSocketServer application
     */
    public static void startInstance() {
        
        // represent the number of open sockets
        connectionCount = 0;
        try {
            // init our server to receive connections on the specified port
            socket = new ServerSocket(PORT, 2);
            
            // log status to the console
            System.out.println("MultiSocketServer initialized");
            
            // listen
            while (true) {
                // init the socket object connection
                Socket connection = socket.accept();
                
                // start a new server for received client
                Runnable runnable = new MultiSocketServer(connection, ++connectionCount);
                System.out.println(runnable.hashCode());
                System.out.println("Current connections: " + connectionCount);
                
                Thread thread = new Thread(runnable);
                thread.start();
            }
        } 
        catch (IOException e) {}
    }
    
    /***
     * Start the MultiSocketServer application
     */
    public static void main(String[] args) {
        startInstance();
    }
}
