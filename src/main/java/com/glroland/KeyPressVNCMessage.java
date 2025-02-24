package com.glroland;

public class KeyPressVNCMessage extends VNCMessage
{
    public int key = -1;
    public boolean isDown = false;
    public boolean isBackspace = false;
    
    public KeyPressVNCMessage(byte [] event)
    {
        if (event == null || event.length != 7)
        {
            throw new IllegalArgumentException("Invalid key press event");
        }
        if (event[0] == 1)
            isDown = true;
        if ((event[5] == -1) && (event[6] == 8))
            isBackspace = true;
        else
            key = event[6];
    }
}
