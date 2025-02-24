package com.glroland;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import io.quarkus.runtime.QuarkusApplication;

public class VNCProxyApp implements QuarkusApplication
{
    public void startProxy(String host, int portNumber)
    {
        System.out.println("Starting VNC Proxy on " + host + ":" + portNumber);
        ServerSocket serverSocket = null;
        try
        {            
            try
            {
                serverSocket = new ServerSocket(portNumber);
            }
            catch(IOException e)
            {
                System.out.println("Error: " + e.getMessage());
                throw new RuntimeException("Error while listening for connections: ", e);
            }
            while(true)
            {
                Socket clientSocket = null;
                try
                {
                    clientSocket = serverSocket.accept();
                }
                catch(IOException e)
                {
                    System.out.println("Error: " + e.getMessage());
                    throw new RuntimeException("Error while listening for connections: ", e);
                }

                VNCConnectionThread thread = new VNCConnectionThread(clientSocket);
                thread.start();
            }
        }
        finally
        {
            try
            {
                if (serverSocket != null)
                {
                    serverSocket.close();
                    System.out.println("Server socket closed");
                }
            }
            catch(IOException e)
            {
                System.out.println("Error: " + e.getMessage());
                throw new RuntimeException("Error closing server socket: ", e);
            }
        }
    }

    @Override
    public int run(String... args) throws Exception {
        System.out.println("Starting VNC Proxy Server...");
        this.startProxy("0.0.0.0", 5900);

//        Quarkus.waitForExit();
        return 0;
    }
}
