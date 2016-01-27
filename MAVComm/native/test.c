#include<string.h>
#include<stdlib.h>
#include<stdio.h>
#include<errno.h>
#include<unistd.h>
#include<fcntl.h>
#include<termio.h>
#include <linux/serial.h>

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

int main() {

    struct termios options;
    struct serial_struct serinfo;
    int fd;
    int speed = 0;
    int rate = 921600;
    
     printf("Test at baudrate %d ",rate);

    /* Open and configure serial port */
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
                  (float)serinfo.baud_base / serinfo.custom_divisor);
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
        //return -1;
    }


    //return fd;

    char ping_cmd[] = {2,1,3,4,5,1,1};
    char ping_rec[7];

    write(fd,&ping_cmd,sizeof(ping_cmd));
    read(fd,&ping_rec,sizeof(ping_rec));

    int i;
    for (i = 0; i < sizeof(ping_rec); i++)
    {
        printf("%d ",ping_rec[i]);
    }


    close(fd);
    return 0;
}
