package com.glroland;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

class VNCConnectionThread extends Thread 
{
    Socket clientSocket;

    StringBuffer inputText = new StringBuffer();

    public VNCConnectionThread(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    private void logRawBuffer(byte [] buffer)
    {
        StringBuffer logOutput = new StringBuffer();
        logOutput.append("RAW [");
        for(int i = 0; i < buffer.length; i++)
        {
            logOutput.append(buffer[i]);
            if (i < buffer.length - 1)
            {
                logOutput.append(" ");
            }
        }
        logOutput.append("]  STRING '").append(new String(buffer)).append("'");
        System.out.println(logOutput.toString());       
    }

    private void handleInputBuffer(byte [] buffer)
    {
//        logRawBuffer(buffer);

        ArrayList<VNCMessage> vncMessages = VNCMessageFactory.getInstance().forMessage(buffer);
        for (VNCMessage vncMessage : vncMessages)
        {
            if (vncMessage instanceof KeyPressVNCMessage)
            {
                KeyPressVNCMessage keyPressMessage = (KeyPressVNCMessage)vncMessage;
                if (keyPressMessage.isDown)
                {
                    if (keyPressMessage.isBackspace)
                    {
                        if (inputText.length() > 0)
                        {
                            inputText.deleteCharAt(inputText.length() - 1);
                        }
                    }
                    else
                    {
                        if (keyPressMessage.key == 13)
                        {
                            System.out.println("Received input: " + inputText.toString());
                            inputText.setLength(0);
                        }
                        else
                        {
                            inputText.append((char)keyPressMessage.key);
                        }
                    }
                    System.out.println("Running Buffer: " + inputText.toString());
                }
            }
        }
    }

    @Override
    public void run() 
    {
        System.out.println("Thread " + Thread.currentThread().getName() + " is running");

        Socket remoteVncSocket = null;
        try
        {
            try
            {
                remoteVncSocket = new Socket("eclipse", 5900);
            }
            catch(IOException e)
            {
                System.out.println("Error: " + e.getMessage());
                throw new RuntimeException("Error while listening for connections: ", e);
            }

            try
            {
                DataInputStream incomingInputStream = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream incomingOutputStream = new DataOutputStream(clientSocket.getOutputStream());

                DataInputStream vncInputStream = new DataInputStream(remoteVncSocket.getInputStream());
                DataOutputStream vncOutputStream = new DataOutputStream(remoteVncSocket.getOutputStream());

                while(true) 
                {
                    if (incomingInputStream.available() > 0)
                    {
                        byte[] buffer = new byte[incomingInputStream.available()];
                        incomingInputStream.read(buffer);
                        handleInputBuffer(buffer);
                        vncOutputStream.write(buffer);
                    }
                    if (vncInputStream.available() > 0)
                    {
                        byte[] buffer = new byte[vncInputStream.available()];
                        vncInputStream.read(buffer);
                        incomingOutputStream.write(buffer);
                    }
                    Thread.sleep(100);
                }        
            }
            catch(IOException e)
            {
                System.out.println("Error: " + e.getMessage());
                throw new RuntimeException("Error while listening for connections: ", e);
            }
            catch(InterruptedException e)
            {
                System.out.println("Error: " + e.getMessage());
                throw new RuntimeException("Thread interrupted! ", e);
            }
        }
        finally
        {
            if (remoteVncSocket != null)
            {
                try
                {
                    remoteVncSocket.close();
                    System.out.println("Client Connection closed for Thread " + Thread.currentThread().getName());
                }
                catch(IOException e)
                {
                    System.out.println("Error: " + e.getMessage());
                    throw new RuntimeException("Error closing connection to VNC host: ", e);
                }
            }
        }
    }
}
