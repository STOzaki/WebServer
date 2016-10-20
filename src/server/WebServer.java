/*
 * These are the specific java imports that are used in this program.
 */
package server;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import javax.swing.JLabel;

/**
 *
 * @author nzooleh modified by Salem Ozaki.
 * This simply reserves the port 6789 to listen.  Once a client
 * uese TCP to connect, then the server accepts the connection, uses the method HttpRequest, makes a specific thread for
 * that connection, and then continues to listen.
 * 
 * <h1>Location of the code:
 * <a href = "http://students.engr.scu.edu/~nzooleh/COEN146S05/lab3/WebServer.java"></a></h1>
 */
public final class WebServer {
    

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception: This is because the ServerSocket, the 
     * accept and the HttpRequest need the exception.
     */
    public static void main(String[] args) throws Exception{
        
    //setting the port number.
    int port = 6789;
    
    //makes a socket with the port 6789.
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
        } //while
    } //main()
    
} //WebServer

/**
 * 
 * @author nzooleh modified by Salem Ozaki.
 * <h2>HttpRequest</h2><div>It uses the socket that was established to talk
 * to the client.  It also implements the Runnable class which is used to run
 * threads 
 * <a scr = "http://docs.oracle.com/javase/7/docs/api/java/lang/Runnable.html">
 * Here is the document for the Runnable class</a></div>
 */
final class HttpRequest implements Runnable
{
    final static String CRLF = "\r\n"; /*to move to the start of the line
    and then move down a line.*/
    
    Socket socket;
    
    //Constructor that stores the connetion into socket.
    public HttpRequest(Socket connection) throws Exception
    {
        this.socket = connection;
    } //HttpRequest(Socket)
    
    
    /**
     * Implement the run() method of the Runnable interface.
     * Runs the method processRequest which receives information and then
     * appropriately give the web an output.
     * If the processRequest hits an error, then throws and exception.
     */
    @Override
    public void run()
    {
        try{
            processRequest();
        } catch (Exception e){
            System.out.println("There was an error in the process of your Request:");
        } //catch(Exception)
    } //run()
    
    /**
     * 
     * @throws Exception: The exception is needs to be implemented for
     * getOutputStream to run.
     * <h2>processRequest</h2><div>It uses the DataOutputStream to push
     * either </div>
     */
    private void processRequest() throws Exception
    {
        //Get a reference to the socket"s input and output streams
        DataOutputStream out;
        out = new DataOutputStream(this.socket.getOutputStream());
        
        //Set up input steam filters.
        BufferedReader buffer = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        
        //Get the request line of the HTTP request message.
        String requestLine = buffer.readLine();
        
        //Display the request line.
        System.out.println();
        System.out.println(requestLine);
        
        String headerLine = null;
        
        //Prints out the rest of the head.
        while((headerLine = buffer.readLine()).length() != 0){
            System.out.println(headerLine);
        } //while
        
        //Extract the filename from the request line.
        StringTokenizer tokens = new StringTokenizer(requestLine);
        
        tokens.nextToken(); //skip over the method, which should be "GET".
        String fileName = tokens.nextToken(); /*grabs the filename which is
        right after "GET" file.*/
        
        //Prepend a "." so that file request is within the current directory.
        fileName = "." + fileName;
        
        //Open the requested file.
        FileInputStream fis = null;
        boolean fileExists = true;
        try { //makes sure that the file works.
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException e)
        {
            fileExists = false;
        }// catch
        
        // Construct the response message.
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;
        
        //outputs the file content, otherwise outputs an error.
        if(fileExists) {
            
            //Gives the status
            statusLine = "The file you requested is found:";
            out.writeBytes(statusLine);
            out.writeBytes(CRLF);
            
            /*combines them together that contains the content type and then
            goes to begining of the next line.*/
            contentTypeLine = "Content-type: " + contentType(fileName) + CRLF;
            
            //sends the content that has the file type to the client.
            out.writeBytes(contentTypeLine);
            
            //Send a blank line to indicate the end of the header lines.
            out.writeBytes(CRLF);
            
            //Send the entity body and then closes the file.
            sendBytes(fis,out);
            
            //Closes the FileInputStream to inspect a file.
            fis.close();
                
        } else { //gives an error if the file does not exist.
            statusLine = "ERROR, file not found:";
            
            //Grabs the name of the file.
            int file_start = fileName.indexOf("/");
            int file_end = fileName.indexOf(".", file_start);
            String file = fileName.substring(file_start + 1, file_end);
            
            //gets the directory.
            FileInputStream directory = new FileInputStream("directory.txt");
            
            //stores the message into
            entityBody = file + " does not exist!";

            //Send an error header lines.
            out.writeBytes(statusLine);
            out.writeBytes(CRLF);
            out.writeBytes(entityBody);
            out.writeBytes(CRLF);
            sendBytes(directory,out);
        } //else
            
        
        //Closing the streams and socket.
        out.close();
        buffer.close();
        this.socket.close();
    } //processRequest()
    
    
    /**
     * 
     * @param fis file content that you want to send.
     * @param out the socket's out stream that the data will go through.
     * @throws Exception The program needs the exception for the InputStream.
     * gets a file content and then sends that data through the socket's
     * out stream.
     */
    private static void sendBytes(FileInputStream fis, OutputStream out) throws Exception{
        //Construct a 1K buffer to hold bytes on their way to the socket.
        byte[] buffer = new byte[1024];
        int bytes = 0;
        
        //Copy requested file into the socket's output stream.
        while((bytes = fis.read(buffer)) != -1) {
            out.write(buffer, 0, bytes);
        } //while
    } //sendBytes
    
    /**
     * 
     * @param fileName This is the file name that was parsed by the
     * StringTokenizer and will be used to find what kind of application to use.
     * @return the type of application to use.
     */
    private static String contentType(String fileName) {
        
        //checks to see if the file is html or htm.
        if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return "text/html";
        } //if
        
        //checks to see if the file is txt.
        if(fileName.endsWith(".txt")) {
            return "text/txt";
        } //if
        
        //checks to see if the file is jpg.
        if(fileName.endsWith(".jpg")) {
            return "image/jpg";
        } //if
        
        //checks to see if the file is png.
        if(fileName.endsWith(".png")) {
            return "image/png";
        } //if
        
        //checks to see if the file is a java.
        if(fileName.endsWith(".java")) {
            return "java file";
        } //if
        
        //checks to see if the file is gif.
        if(fileName.endsWith("gif")) {
            return "image/gif";
        } //if
        
        //by default it will return a binary version of a MIME-type.
        return "application/octet-stream";
    } //contentType(String)
} //HttpRequest