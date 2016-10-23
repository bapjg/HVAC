package eRegulation;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

//public static class Linux_C_lib_DirectMapping implements Linux_C_lib 
//public class Linux_C_lib_DirectMapping implements Linux_C_lib 
public class Linux_C_lib_DirectMapping implements com.sun.jna.Library 
{
    native public long memcpy (int[] dst, short[] src, long n);
    native public int  memcpy (int[] dst, short[] src, int n);
    native public int  pipe   (int[] fds);
    native public int  tcdrain(int fd);
    native public int  fcntl  (int fd, int cmd, int arg);
    native public int  ioctl  (int fd, int cmd, int[] arg);
    native public int  open   (String path, int flags);
    native public int  close  (int fd);
    native public int  write  (int fd, byte[] buffer, int count);
    native public int  read   (int fd, byte[] buffer, int count);
    native public long write  (int fd, byte[] buffer, long count);
    native public long read   (int fd, byte[] buffer, long count);
//    native public int  select(int n, int[] read, int[] write, int[] error, timeval timeout);
    native public int  poll   (int[] fds, int nfds, int timeout);
    native public int  tcflush(int fd, int qs);
    native public void perror (String msg);
    native public int  tcsendbreak(int fd, int duration);

    static 
	{
        try 
		{
            Native.register("c");
            System.out.println("registered to c library");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}
