/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sozaki19
 */
public class HttpRequestTest {
    
    public HttpRequestTest() {
    }

    /**
     * Test of contentType method, of class HttpRequest, to make sure
     * that html or htm, txt, jpg, png, java, gif, and the default
     * application/octet-stream.
     */
    @Test
    public void testContentType() {
        String fileName = "website.html";
        String expResult = "text/html";
        String result = HttpRequest.contentType(fileName);
        assertEquals(expResult, result);
        fileName = "anotherwebsite.htm";
        expResult = "text/html";
        result = HttpRequest.contentType(fileName);
        assertEquals(expResult, result);fileName = "directory.txt";
        expResult = "text/txt";
        result = HttpRequest.contentType(fileName);
        assertEquals(expResult, result);
        fileName = "picture.jpg";
        expResult = "image/jpg";
        result = HttpRequest.contentType(fileName);
        assertEquals(expResult,result);
        fileName = "differentPicture.png";
        expResult = "image/png";
        result = HttpRequest.contentType(fileName);
        assertEquals(expResult,result);
        fileName = "javaCode.java";
        expResult = "java file";
        result = HttpRequest.contentType(fileName);
        assertEquals(expResult,result);
        fileName = "oneLastPicture.gif";
        expResult = "image/gif";
        result = HttpRequest.contentType(fileName);
        assertEquals(expResult,result);
        fileName = "application.svg";
        expResult = "application/octet-stream";
        result = HttpRequest.contentType(fileName);
        assertEquals(expResult,result);
    }
    
    
}
