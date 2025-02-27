package com.glroland;

import java.util.ArrayList;

public class VNCMessageFactory 
{
    private static final VNCMessageFactory instance = new VNCMessageFactory();

    public static VNCMessageFactory getInstance()
    {
        return instance;
    }

    public ArrayList<VNCMessage>  forMessage(byte [] buffer)
    {
        ArrayList<VNCMessage> messages = new ArrayList<VNCMessage>();
        if (buffer != null)
        {
            for (int i=0; i<buffer.length; i++)
            {
                if (buffer[i] == 3)
                {
                    i += 9;
                }
                else if (buffer[i] == 4)
                {
                    byte [] key_buffer = new byte[7];
                    int length = key_buffer.length;
                    if ((i + 1 + length ) <= buffer.length)
                    {
                        System.arraycopy(buffer, i+1, key_buffer, 0, length);

                        KeyPressVNCMessage message = new KeyPressVNCMessage(key_buffer);
                        messages.add(message);
                    }
    
                    i += 7;
                }
            }
        }


        return messages;
    }
}
