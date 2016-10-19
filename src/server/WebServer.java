/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author Salem Ozaki
 * I used this person's code as a base starter:
 * <a href = "http://students.engr.scu.edu/~nzooleh/COEN146S05/lab3/WebServer.java">This is another student who has done this and I 
 */
public final class WebServer {
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
    //setting the port number.
    int port = 6789;
    ServerSocket serversocket = new ServerSocket(port);
    while(true){
        //Listening for a TCP connection request.
    Socket clientSocket = serversocket.accept();
    //Construct an object to process the HTTP request message.
    HttpRequest request = new HttpRequest(clientSocket);
    //Create a new thread to process the request.
    Thread thread = new Thread(request);
    //Start the thread.
    thread.start();
    }
    }
    
}
final class HttpRequest implements Runnable
{
    final static String CRLF = "\r\n";
    Socket socket;
    //Constructor
    public HttpRequest(Socket connection) throws Exception
    {
        this.socket = connection;
    }
    
    //Implement the run() method of the Runnable interface.
    public void run()
    {
        try{
            processRequest();
        } catch (Exception e){
            System.out.println("There was an error in the process of your Request:");
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @throws Exception
     * You can put html here!!!!
     */
    private void processRequest() throws Exception
    {
        //Get a reference to the socket"s input and output streams
        //InputStream input = this.socket.getInputStream();
        DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
        //Set up input steam filters.
        BufferedReader buffer = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        //Get the request line of the HTTP request message.
        String requestLine = buffer.readLine();
        //Desplay the request line.
        System.out.println();
        System.out.println(requestLine);
        String headerLine = null;
        while((headerLine = buffer.readLine()).length() != 0){
            System.out.println(headerLine);
        }
        
        //Extract the filename from the request line.
        StringTokenizer tokens = new StringTokenizer(requestLine);
        tokens.nextToken(); //skip over the method, which should be "GET".
        String fileName = tokens.nextToken();
        //Prepend a "." so that file request is within the current directory.
        fileName = "." + fileName;
        //Open the requested file.
        FileInputStream fis = null;
        boolean fileExists = true;
        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException e)
        {
            fileExists = false;
        }
        // Construct the response message.
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;
        if(fileExists) {
            statusLine = "200 OK";
            contentTypeLine = "Content-type: " + contentType(fileName) + CRLF;
        } else {
            statusLine = "error not found";
            entityBody = "<HTML>" + "<HEAD><TITLE>Not Found</TITLE></HEAD>" + 
                    "<BODY><h1>ERROR 404 The file you have requested has not" +
                    "been found." + "</h1></HTML>";
        }
            //Send the status line.
            out.writeBytes(statusLine);
            //Send the content type line.
            if(fileExists) {
            out.writeBytes(contentTypeLine);
            //Send a blank line to indicate the end of the header lines.
            out.writeBytes(CRLF);
            //Send the entity body.
                sendBytes(fis,out);
                fis.close();
            } else {
                //Send a blank line to indicate the end of the header lines.
                out.writeBytes(CRLF);
                out.writeBytes(entityBody);
            }
            
        
        //Closing the streams and socket.
        out.close();
        buffer.close();
        this.socket.close();
    }
    
    private static void sendBytes(FileInputStream fis, OutputStream out) throws Exception{
        //Construct a 1K buffer to hold bytes on their way to the socket.
        byte[] buffer = new byte[1024];
        int bytes = 0;
        
        //Copy requested file into the socket's output stream.
        while((bytes = fis.read(buffer)) != -1) {
            out.write(buffer, 0, bytes);
        }
    }
    
    private static String contentType(String fileName) {
        if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return "text/html";
        }
        if(fileName.endsWith(".jpg")) {
            return "image/jpg";
        }
        if(fileName.endsWith(".png")) {
            return "image/png";
        }
        if(fileName.endsWith(".java")) {
            return "java file";
        }
        if(fileName.endsWith("gif")) {
            return "image/gif";
        }
        return "application/octet-stream";
    }
}