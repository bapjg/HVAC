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


#define ARRAY_SIZE(a)(sizeof(a) / sizeof((a)[0]))

static uint8_t 	spi_mode;
static uint8_t 	bits 		= 8;
static uint32_t speed 		= 50000;							//Was 100000
static uint16_t delay 		= 2;

static int 		spi_fd;
static int 		i2c_fd;
static int 		i2c_port	= 0x94;								//Production

static char 	*device 	= " ";
static int 		addr 		= 0x9C;

static int 		readmode 	= 0;

static int 		reg 		= -1;
static int 		val 		= -1;
static int 		cls 		= 0;

//================================================================
//
// General subroutines & functions
//

//----------------------------------------------------------
static void pabort(const char *s)
{
	perror(s);
	abort();
}
//----------------------------------------------------------

//
//================================================================

//================================================================
//
// SPI Interface : Relays
//

//----------------------------------------------------------
static void spi_open()
{
	// Opens SPI interface with variables held in static
	// If problem : aborts

	int ret;

	spi_fd 					= open(device, O_RDWR);
	if (spi_fd < 0)			 pabort("can't open device \n");

	ret 					= ioctl(spi_fd, SPI_IOC_WR_MODE, &spi_mode);
	if (ret == -1)			pabort("can't set spi mode \n");

	ret 					= ioctl(spi_fd, SPI_IOC_RD_MODE, &spi_mode);
	if (ret == -1)			pabort("can't get spi mode \n");

	ret 					= ioctl(spi_fd, SPI_IOC_WR_BITS_PER_WORD, &bits);
	if (ret == -1)			pabort("can't set bits per word \n");

	ret 					= ioctl(spi_fd, SPI_IOC_RD_BITS_PER_WORD, &bits);
	if (ret == -1)			pabort("can't get bits per word \n");

	ret 					= ioctl(spi_fd, SPI_IOC_WR_MAX_SPEED_HZ, &speed);
	if (ret == -1)			pabort("can't set max speed hz \n");

	ret 					= ioctl(spi_fd, SPI_IOC_RD_MAX_SPEED_HZ, &speed);
	if (ret == -1)			pabort("can't get max speed hz \n");
}
static void spi_txrx(char *buf, int tlen, int rlen)
{
	// Writes command to SPI channel and gets returned data (if any)
	// In the event of an error, closes the channel, reopens and retries
	// the I/O.

	int    ret;
	struct spi_ioc_transfer tr = 
	{
		.delay_usecs 			= delay,
		.speed_hz 				= speed,
		.bits_per_word 			= bits,
	};

	if (rlen > tlen) tr.len 	= rlen;
	else			 tr.len 	= tlen;
	 
	tr.tx_buf 					=(unsigned long) buf;
	tr.rx_buf 					=(unsigned long) buf;
	
	ret 						= ioctl(spi_fd, SPI_IOC_MESSAGE(1), &tr);
	
	if (ret < 1)
	{
		printf("can't send spi message, retrying \n");
		
		close(spi_fd);
		
		spi_open();
		
		tr.delay_usecs			= delay;
		tr.speed_hz				= speed;
		tr.bits_per_word		= bits;		

		if (rlen > tlen) tr.len = rlen;
		else			 tr.len = tlen;
		
		tr.tx_buf 				=(unsigned long) buf;
		tr.rx_buf 				=(unsigned long) buf;
	
		ret 					= ioctl(spi_fd, SPI_IOC_MESSAGE(1), &tr);
		if (ret < 1) 			pabort("Couldn't recover from the error \n");
		else 					printf("Recovered \n");
	}
}
//----------------------------------------------------------
static int get_reg_value8(int reg)
{
	// Routine from BW_Tools, not used

	char buf[5]; 

	buf[0] = addr | 1;
	buf[1] = reg;
	spi_txrx(buf, 2, 1);

	return buf[2];
}
//----------------------------------------------------------
static int get_reg_value16(int reg)
{
	// Routine from BW_Tools, not used

	char buf[5];

	buf[0] 						= addr | 1;
	buf[1] 						= reg;
	spi_txrx(buf, 2, 2);

	return buf[2] | (buf[3] << 8);
}
static void scanAndSet()
{
	// Procedure used to find Relay bank address
	// When switching off a relay, the circuit can
	// loose its address
	// Here we scan to find it and, if necessary, reset
	// it to correct value
	
	char buf[0x20];
	int add;
	int i;
	
	buf[0] 						= addr | 1;
	buf[1] 						= 1;							// Ident
	spi_txrx(buf, 0x2, 0x20);
	
	char * found;
	found 						= strstr(buf, "spi_big");
	if (found == NULL)
	{
		// The Relay board has lost its address
		// Scan all addresses to find where it is

		for(add = 0; add < 255; add += 2)						// Loop through addresses
		{
			buf[0] 				= add | 1;						// OR 1 means return status info
			buf[1] 				= 1;							// Ident
			spi_txrx(buf, 0x2, 0x20);
			
			found = strstr(buf, "spi_big");
			if (found != NULL)
			{
				// We now have to set the address
				buf[0] 			= add;							// No Or means do action
				buf[1] 			= 0xf0;							// Set address
				buf[2] 			= addr;
				spi_txrx(buf, 3, 0);
				return;
			}
		}
		// Relay has no address at all. There's no point continuing
		// As nothing (pump/burner/valve) can be actioned
		printf("scanAndSet found no relays - will now abort \n");
		pabort("Aborting by scanAndSet \n");
	}
}
//----------------------------------------------------------
void Relay_Open(int Relay_Bank)
{
	// Routine to open relay bank (channel 0 or 1)
	int ret;

	if (Relay_Bank == 0)
	{
		device 	 			= "/dev/spidev0.0";
		addr				= 0x9C;
	}
	else
	{
		device   			= "/dev/spidev0.1";
		addr	 			= 0xFE;
	}

	spi_open();
}
//----------------------------------------------------------
void Relay_On(int Relay_Bank, int Relay_Number)
{
	// Routine to switch on a relay within a relay bank
	// (channel 0 or 1). Relay number can be from 0 to 5
	char buf[5]; 
	Relay_Open(Relay_Bank);
	
	buf[0] 					= addr;
	buf[1] 					= 0x20 + Relay_Number;
	buf[2] 					= 1;
	spi_txrx(buf, 3, 0);
	printf("---relay open %d \n", Relay_Number);

	close(spi_fd);
}
//----------------------------------------------------------
void Relay_Off(int Relay_Bank, int Relay_Number)
{
	// Routine to switch off a relay within a relay bank
	// (channel 0 or 1). Relay number can be from 0 to 5
	char buf[5]; 

	Relay_Open(Relay_Bank);
	
	buf[0] 					= addr;
	buf[1] 					= 0x20 + Relay_Number;
	buf[2] 					= 0;
	spi_txrx(buf, 3, 0);
	
	scanAndSet();
	close(spi_fd);
}
//----------------------------------------------------------
int Is_On(int Relay_Bank, int Relay_Number)
{
	// Routine to interrogate whether a relay is switched on or off
	// Relay_Bank = 0 or 1. Relay number can be from 0 to 5
	char buf[5];
	Relay_Open(Relay_Bank);

	buf[0] 					= addr | 1;
	buf[1] 					= 0x20 + Relay_Number;
	buf[2] 					= 0;
	spi_txrx(buf, 2, 1);

	close(spi_fd);
	printf("---relay %d is %d \n", Relay_Number, buf[2]);
	return buf[2];
}
void Relays_OffAll(int Relay_Bank)
{
	// Routine to switch of all relays in one go.
	// Avoid using as can generate spike on mains supply.
	char buf[5];
	Relay_Open(Relay_Bank);

	buf[0] 					= addr;
	buf[1] 					= 0x10;
	buf[2] 					= 0;
	spi_txrx(buf, 3, 0);
	
	scanAndSet();
	close(spi_fd);
}
void Relays_ScanAndSet(int Relay_Bank)
{
	// JNI interface routine to scanAndSet
	Relay_Open(Relay_Bank);

	scanAndSet();
	close(spi_fd);
}


//
//================================================================

int main(int argc, char *argv[])
{
	int result = 0;
	result = Is_On(1, 3);
}

