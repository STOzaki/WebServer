# WebServer
## WebServer
This program opens a connection on port 6789.  After a connection is established it runs HttpRequest and puts that connetion to a thread and then continues to listen. [location](https://github.com/STOzaki/WebServer/blob/master/src/server/WebServer.java#L41)
## HttpRequest
### run
This simply calls the processRequest and catches if the processRequest does not work.
### processRequest
reference to socket's [input](https://github.com/STOzaki/WebServer/blob/master/src/server/WebServer.java#L112) and [output](https://github.com/STOzaki/WebServer/blob/master/src/server/WebServer.java#L109) streams.  After, it gets the name of the file and then [changes](https://github.com/STOzaki/WebServer/blob/master/src/server/WebServer.java#L136) it so that file request is within the current directory.  Next the program tries to find the file using the name in the chosen.  If it find the file then it prints a [header](https://github.com/STOzaki/WebServer/blob/master/src/server/WebServer.java#L166), the [content type](https://github.com/STOzaki/WebServer/blob/master/src/server/WebServer.java#L163), and then the [content](https://github.com/STOzaki/WebServer/blob/master/src/server/WebServer.java#L172) of the file.  If the file does not exist, then it give a error [header](https://github.com/STOzaki/WebServer/blob/master/src/server/WebServer.java#L190), told you that the [file you requested does not exists](https://github.com/STOzaki/WebServer/blob/master/src/server/WebServer.java#L192), and a [list of the files](https://github.com/STOzaki/WebServer/blob/master/src/server/WebServer.java#L194) that are on the server.

### sendBytes
This method makes a [byte array](https://github.com/STOzaki/WebServer/blob/master/src/server/WebServer.java#L206) that contains 1024 bytes that will contain parts of the text.  Then, it sends the byte array through the [socket to the client](https://github.com/STOzaki/WebServer/blob/master/src/server/WebServer.java#L210).

### contentType
[This method](https://github.com/STOzaki/WebServer/blob/master/src/server/WebServer.java#L232) takes the a file name and determines which application to use on the file requested.

#### Credit
[nzooleh](http://students.engr.scu.edu/~nzooleh/COEN146S05/lab3/WebServer.java)
