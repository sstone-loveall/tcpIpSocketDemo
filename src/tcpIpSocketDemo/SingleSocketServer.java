package tcpIpSocketDemo;

import java.net.*;
import java.io.*;

/*
 * Tutorial from http://edn.embarcadero.com/article/31995
 * A single socket server: processes one socket at a time
 */
public class SingleSocketServer {

    protected static final String ASCII_ENCODING = "US-ASCII";
    protected static final int PORT = 19999;
    protected static final int TERMINAL_CHAR = 13;
    
    static ServerSocket socket;
    static Socket connection;
    static boolean first;
    static StringBuffer process;
    static String timeStamp;
    
    public static void main(String[] args) {
        
        try {
            // init our server to receive connections on the specified port
            socket = new ServerSocket(PORT);
            
            // log status to the console
            System.out.println("SingleSocketServer initialized");
            
            int character;
            
            // listen
            while (true) {
                
                // init the socket object connection
                connection = socket.accept();
                
                // process the input coming from the socket
                BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
                InputStreamReader isr = new InputStreamReader(is);
                process = new StringBuffer();
                while ( (character = isr.read()) != TERMINAL_CHAR) {
                    process.append((char) character);
                }
                System.out.println("Input received:");
                System.out.println(process);
                
               
                // Respond to the client: once the response is complete, the server is now ready to accept another socket
                respond();
            }
        }
        catch (IOException f) { }
    }
    
    /***
     * After processing input, return some information.
     */
    protected static void respond() throws IOException {
        waitTenSeconds();
        
        timeStamp = new java.util.Date().toString();
        String returnCode = "SingleSocketServer responded at " + timeStamp + (char) TERMINAL_CHAR;
        
        BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream());
        OutputStreamWriter osw = new OutputStreamWriter(os, ASCII_ENCODING);
        osw.write(returnCode);
        osw.flush();
    }
    
    /***
     * Wait 10 seconds: this is for demo purposes only. It pretends something is processing.
     */
    protected static void waitTenSeconds() {
        try {
            Thread.sleep(10000);
        }
        catch (Exception e) {}
    }
}
