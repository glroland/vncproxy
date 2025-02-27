package com.glroland;

// +-----------------+--------------------+
// | Key name        | Keysym value (hex) |
// +-----------------+--------------------+
// | BackSpace       | 0xff08             |
// | Tab             | 0xff09             |
// | Return or Enter | 0xff0d             |
// | Escape          | 0xff1b             |
// | Insert          | 0xff63             |
// | Delete          | 0xffff             |
// | Home            | 0xff50             |
// | End             | 0xff57             |
// | Page Up         | 0xff55             |
// | Page Down       | 0xff56             |
// | Left            | 0xff51             |
// | Up              | 0xff52             |
// | Right           | 0xff53             |
// | Down            | 0xff54             |
// | F1              | 0xffbe             |
// | F2              | 0xffbf             |
// | F3              | 0xffc0             |
// | F4              | 0xffc1             |
// | ...             | ...                |
// | F12             | 0xffc9             |
// | Shift (left)    | 0xffe1             |
// | Shift (right)   | 0xffe2             |
// | Control (left)  | 0xffe3             |
// | Control (right) | 0xffe4             |
// | Meta (left)     | 0xffe7             |
// | Meta (right)    | 0xffe8             |
// | Alt (left)      | 0xffe9             |
// | Alt (right)     | 0xffea             |
// +-----------------+--------------------+

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
