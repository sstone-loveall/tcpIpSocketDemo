package tcpIpSocketDemo;

import java.net.*;
import java.io.*;

/***
 * Tutorial from http://edn.embarcadero.com/article/31995
 * The SocketClient class is a simple example of a TCP/IP Socket Client.
 */
public class SocketClient {

    protected static final String ASCII_ENCODING = "US-ASCII";
    protected static final String HOST = "localhost";
    protected static final int PORT = 4000; // 19999;
    protected static final int TERMINAL_CHAR = 13;
    
    private StringBuffer instr;
    private String timeStamp;
    private String response;
    private boolean success;
    
    public SocketClient() {
        instr = new StringBuffer();
        timeStamp = "";
        System.out.println("SocketClient initialized");
    }
    
    public String getResponse() {
        return response;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    /*
     * Writes some data to a socket.
     */
    public void makeConnection() {
        try {
            // obtain an address object of the server
            InetAddress address = InetAddress.getByName(HOST);
            
            // establish a socket connection
            // note: if the server is not responding on the port, we'll get a "java.netConnectException: Connection refused..." message
            Socket connection = new Socket(address, PORT);
            
            // init a BufferedOutputStream object. 
            // note: in general TCP stacks use buffers to improve performance within the network
            BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
            
            // init an OutputStreamWriter object with the optional char encoding
            // note: with OutputStreamWriter you can pass objects such as Strings without converting to byte, byte arrays, or int values
            OutputStreamWriter osw = new OutputStreamWriter(bos, ASCII_ENCODING);
            
            socketWrite(timeStamp, osw); 
            socketRead(connection, instr);
        } 
        catch (UnknownHostException f) {
            success = false;
            System.out.println("UnknownHostException: " + f);
        }
        catch (IOException g) {
            success = false;
            System.out.println("IOException: " + g);
        } 
        catch (Exception e) {
            success = false;
            System.out.println("Exception: " + e);
        }
    }
    
    /*
     * Write across the socket connection and then flush the buffer
     */
    protected void socketWrite(String timeStamp, OutputStreamWriter osw) throws IOException {
        // the char(13) marks the end of the process
        timeStamp = new java.util.Date().toString();
        String process = "Calling the Socket Server on " + HOST + " port " + PORT + " at " + timeStamp + (char) TERMINAL_CHAR;
        System.out.println(process);
        // write across the socket connection and flush the buffer
        // note: if we don't flush the buffer, the data may not be written across the socket in a timely manner
       osw.write(process);
       osw.flush();
    }
    
    /*
     * Read bytes coming back from the server until the terminating char is reached (13), then disconnect the socket
     */
    protected void socketRead(Socket connection, StringBuffer instr) throws IOException {
        
        // instantiate a BufferedInputStream object for reading incoming socket streams
        // note: most networks buffer socket traffic to improve performance; for this reason use the BufferedInputStream class
        BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
        
        // instantiate an InputStreamReader with the optional char encoding
        InputStreamReader isr = new InputStreamReader(bis, ASCII_ENCODING);
        
        // read the socket's InputStream and append to a StringBuffer
        // read bytes until the terminating char is reached (13), then disconnect the socket
        int c;
        while ( (c = isr.read()) != TERMINAL_CHAR) {
            instr.append( (char) c);
        }
        success = true;
        
        // close the socket connection
        connection.close();
        
        // output the response
        response = instr.toString();
        System.out.println("Response received:");
        System.out.println(response);
    }
    
    public static void main(String[] args) {
        SocketClient client = new SocketClient();
        client.makeConnection();
    }
}
