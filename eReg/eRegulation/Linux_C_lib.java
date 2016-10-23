package eRegulation;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Structure;

public interface Linux_C_lib extends com.sun.jna.Library 
{
    public long memcpy(int[] dst, short[] src, long n);
    public int memcpy(int[] dst, short[] src, int n);
    public int pipe(int[] fds);
    public int tcdrain(int fd);
    public int fcntl(int fd, int cmd, int arg);
    public int ioctl(int fd, int cmd, int[] arg);
    public int open(String path, int flags);
    public int close(int fd);
    public int write(int fd, byte[] buffer, int count);
    public int read(int fd, byte[] buffer, int count);
    public long write(int fd, byte[] buffer, long count);
    public long read(int fd, byte[] buffer, long count);
    public int select(int n, int[] read, int[] write, int[] error, timeval timeout);
    public int poll(int[] fds, int nfds, int timeout);
    public int tcflush(int fd, int qs);
    public void perror(String msg);
    public int tcsendbreak(int fd, int duration);

    static public class timeval extends Structure 
	{
        public NativeLong tv_sec;
        public NativeLong tv_usec;

        @Override
        protected List getFieldOrder() 
		{
            return Arrays.asList(//
                    "tv_sec",//
                    "tv_usec"//
                    );
        }

//        public timeval(jtermios.TimeVal timeout) 
//		{
//            tv_sec = new NativeLong(timeout.tv_sec);
//            tv_usec = new NativeLong(timeout.tv_usec);
//        }
    }
}
