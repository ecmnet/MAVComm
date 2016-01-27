#include<string.h>
#include<stdlib.h>
#include<stdio.h>
#include<errno.h>
#include<unistd.h>
#include<fcntl.h>
#include<termio.h>
#include <linux/serial.h>

static int fd;

   
   
static int rate_to_constant(int baudrate) {
#define B(x) case x: return B##x
        switch(baudrate) {
        B(50);     B(75);     B(110);    B(134);    B(150);
        B(200);    B(300);    B(600);    B(1200);   B(1800);
        B(2400);   B(4800);   B(9600);   B(19200);  B(38400);
        B(57600);  B(115200); B(230400); B(460800); B(500000);
        B(576000); B(921600); B(1000000);B(1152000);B(1500000);
    default: return 0;
    }
#undef B
}


int openAMA0(int rate) {

 struct termios options;
 struct serial_struct serinfo;
 
 int speed = 0;

   if ((fd = open("/dev/ttyAMA0",O_RDWR|O_NOCTTY)) == -1)
    {
        return -1;
    }


   // if you've entered a standard baud the function below will return it
    speed = rate_to_constant(rate);

    if (speed == 0) {
        /* Custom divisor */
        serinfo.reserved_char[0] = 0;
        if (ioctl(fd, TIOCGSERIAL, &serinfo) < 0)
            return -1;
        serinfo.flags &= ~ASYNC_SPD_MASK;
        serinfo.flags |= ASYNC_SPD_CUST;
        serinfo.custom_divisor = (serinfo.baud_base + (rate / 2)) / rate;
        if (serinfo.custom_divisor < 1)
            serinfo.custom_divisor = 1;
        if (ioctl(fd, TIOCSSERIAL, &serinfo) < 0)
            return -1;
        if (ioctl(fd, TIOCGSERIAL, &serinfo) < 0)
            return -1;
        if (serinfo.custom_divisor * rate != serinfo.baud_base) {
            warnx("actual baudrate is %d / %d = %f",
                  serinfo.baud_base, serinfo.custom_divisor,
                 ( (float)serinfo.baud_base / (float)serinfo.custom_divisor));
        }
    }

    fcntl(fd, F_SETFL, 0);
    tcgetattr(fd, &options);
    cfsetispeed(&options, speed ?: B38400);
    cfsetospeed(&options, speed ?: B38400);
    cfmakeraw(&options);
    options.c_cflag |= (CLOCAL | CREAD);
    options.c_cflag &= ~CRTSCTS;
    if (tcsetattr(fd, TCSANOW, &options) != 0)
    {
        return -1;
    }


    return fd;
}

void writeAMA0(char *ptr, int length) {
     write(fd,ptr,length);
 
}


int readAMA0(char* ptr, int length) {
   return read(fd, ptr, length);
}

void closeAMA0() {
  close(fd);
  }