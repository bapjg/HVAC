#include <stdint.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>
#include <fcntl.h>
#include <string.h>
#include <sys/ioctl.h>
#include <sys/stat.h>
#include <linux/types.h>
#include <linux/spi/spidev.h>
#include <linux/i2c-dev.h>

extern void debug(void);


main()
{
	printf("Scan.main \n");
//	printf("Scan.main again \n");		// Removed Make a change
	debug();
	printf("Done Scan.main\n");
	exit(0);
}
