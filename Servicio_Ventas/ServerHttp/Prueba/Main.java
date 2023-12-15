/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ServerHttp.Prueba;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.logging.*;
*/

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getCanonicalName());
    private static final int NUM_THREADS = 50;
    private static final String INDEX_FILE = "index.html";
    private final File rootDirectory;
    private final int port;

    public static void main(String[] args) {
        // get the Document root
        File docroot;
        //docroot = File("c:/ejem");
        //docroot = File("C:\\ejem\\index.html");

        try {
            //docroot = new File(args[0]);
            //ocroot = File("c:\\ejem");
            //docroot = new File("C:\\ejem\\index.html");
            docroot = new File("/home/komiz/Desktop/FINAL-CONCURRENTE/Servicio_Almacen/ServerHttp/dir");
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Usage: java JHTTP docroot port");
            return;
        }

        System.out.println("docroot: " + docroot);
        
        // set the port to listen on
        int port = 8000;
        /*
        try {
            port = Integer.parseInt(args[1]);
            if (port < 0 || port > 65535) port = 80;
        } catch (RuntimeException ex) {
            port = 80;
        }
        */
        try {
            Main webserver = new Main(docroot, port);
            webserver.start();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Server could not start", ex);
        }
    }
    
    
    public Main(File rootDirectory, int port) throws IOException {
        if (!rootDirectory.isDirectory()) {
            throw new IOException(rootDirectory
            + " does not exist as a directory");
        }
        this.rootDirectory = rootDirectory;
        this.port = port;
    }
    
    public void start() throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
        try (ServerSocket server = new ServerSocket(port)) {
            logger.info("Accepting connections on port " + server.getLocalPort());
            logger.info("Document Root: " + rootDirectory);
            while (true) {
                try {
                    Socket request = server.accept();
                    Runnable r = new RequestProcessor(rootDirectory, INDEX_FILE, request);
                    pool.submit(r);
                } catch (IOException ex) {
                    logger.log(Level.WARNING, "Error accepting connection", ex);
                }
            }
        }
    }


    
}
/*

c:
  ejem
     index.html
     indexxx.html
     dibujo.html
     dj.jpg

-----------------
 <!DOCTYPE html>
<html>
<body>

<h1>aaaaa</h1>

<p>dibujo.</p>
<p><a href="www.gaf.cl"><img 
src="di.jpg" ></a></p>
</body>
</html>


*/