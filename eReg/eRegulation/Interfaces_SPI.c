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

//================================================================
//
// JNI Includes
//

#include <jni.h>

//include "Relay.h"

JNIEXPORT void 		JNICALL Java_eRegulation_Relay_On  			(JNIEnv *, jobject, jint, jint);
JNIEXPORT void 		JNICALL Java_eRegulation_Relay_Off 			(JNIEnv *, jobject, jint, jint);
JNIEXPORT void 		JNICALL Java_eRegulation_Relay_AllOff  		(JNIEnv *, jobject, jint);
JNIEXPORT jboolean 	JNICALL Java_eRegulation_Relay_IsOn  		(JNIEnv *, jobject, jint, jint);

//include "Relays.h"

JNIEXPORT void 		JNICALL Java_eRegulation_Relays_OffAll  	(JNIEnv *, jobject, jint);
JNIEXPORT void 		JNICALL Java_eRegulation_Relays_ScanAndSet  (JNIEnv *, jobject, jint);
//
//================================================================

//#define ARRAY_SIZE(a)(sizeof(a) / sizeof((a)[0]))				What is this for


//================================================================
//
//  Define external for debugging by Scan.c
//
extern  void debug(void);
//
//================================================================

//================================================================
//
//  Avoid C compiler issuing warnings
//
//static void Relay_Open(int Relay_Bank);
//
//================================================================



static uint8_t 	spi_mode;
static uint8_t 	bits 		= 8;
static uint32_t speed 		= 50000;							//Was 100000. Brought down to 50 000, which no longer worked from oct 2016.
static uint16_t delay 		= 2;

static int 		spi_fd;
static int 		spi_port 	= 0x9C;
static char 	*spi_device = " ";


//================================================================
//
// General subroutines & functions
//

//----------------------------------------------------------
static void pabort(const char *s)
{
	perror(s);
	exit(0);		// Status returned is 0. Application stops and returns to bash
}
//----------------------------------------------------------
char mkprintable (char ch)			// for debugging... copied from bw_tool
{
  if (ch < ' ') 	return '.';
  if (ch <= '~') 	return ch;
  return '.';
}
//----------------------------------------------------------
void printout(char *buff)			// for debugging... copied from bw_tool
{
	int i;
	for (i = 0; i < 0x20; i++)
	{
		if (mkprintable (buff[i]) != '.') 		break;
	}
	if (i < 0x20)
	{
		for (i = 0; i < 0x20; i++)
		{
			if (buff[i] == 0)	break;
			putchar (mkprintable (buff[i]));
		}
		printf("\n");
	}
}
//----------------------------------------------------------

//
//================================================================



//----------------------------------------------------------
static void spi_open()
{
	// Opens SPI interface with variables held in static
	// If problem : aborts

//	int ret;

	spi_fd 					= open(spi_device, O_RDWR);
	if (spi_fd < 0)			 pabort("can't open device \n");

	if (	ioctl(spi_fd, SPI_IOC_WR_MODE, &spi_mode) 		== -1)		pabort("can't set spi mode \n");
	if (	ioctl(spi_fd, SPI_IOC_RD_MODE, &bits) 			== -1)		pabort("can't set bits per word \n");
	if (	ioctl(spi_fd, SPI_IOC_RD_BITS_PER_WORD, &bits) 	== -1)		pabort("can't get bits per word \n");
	if (	ioctl(spi_fd, SPI_IOC_WR_MAX_SPEED_HZ, &speed) 	== -1)		pabort("can't set max speed hz \n");
	if (	ioctl(spi_fd, SPI_IOC_RD_MAX_SPEED_HZ, &speed) 	== -1)		pabort("can't get max speed hz \n");
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

	tr.len 						= rlen + tlen;
	 
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
static void scanAndSet()
{
	// Procedure used to find Relay bank ess
	// When switching off a relay, the circuit can
	// loose its address
	// Here we scan to find it and, if necessary, reset
	// it to correct value
	

	// TODO Kludge untill this routine works properly
	//	return;
	// End Kludge

//	return;

	char buf[0x20];
	int port;
	
	buf[0] 						= spi_port | 1;
	buf[1] 						= 1;							// Ident
	spi_txrx(buf, 0x2, 0x20);
	
	char *found;
	found 						= strstr(buf, "spi_big");

	if (found == NULL)
	{
		// The Relay board has lost its address
		// Scan all addresses to find where it is

		for(port = 0; port < 255; port += 2)					// Loop through addresses
		{
			buf[0] 				= port | 1;						// OR 1 means return status info
			buf[1] 				= 1;							// Ident
			spi_txrx(buf, 0x2, 0x20);

			found = strstr(buf, "spi_big");
			if (found != NULL)
			{
				// We now have to set the address
				buf[0] 			= port;							// Use the found port
				buf[1] 			= 0xf0;							// Set address
				buf[2] 			= spi_port;
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
//	int ret;

	if (Relay_Bank == 0)
	{
		spi_device 	 		= "/dev/spidev0.0";
		spi_port			= 0x9C;
	}
	else
	{
		spi_device   		= "/dev/spidev0.1";
		spi_port	 		= 0xFE;
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
	
	buf[0] 					= spi_port;
	buf[1] 					= 0x20 + Relay_Number;
	buf[2] 					= 1;
	spi_txrx(buf, 3, 0);

	close(spi_fd);
}
//----------------------------------------------------------
void Relay_Off(int Relay_Bank, int Relay_Number)
{
	// Routine to switch off a relay within a relay bank
	// (channel 0 or 1). Relay number can be from 0 to 5
	char buf[5]; 

	Relay_Open(Relay_Bank);
	
	buf[0] 					= spi_port;
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

	buf[0] 					= spi_port | 1;
	buf[1] 					= 0x20 + Relay_Number;
	buf[2] 					= 0;
	spi_txrx(buf, 2, 1);

	close(spi_fd);
	return buf[2];
}
void Relays_OffAll(int Relay_Bank)
{
	// Routine to switch of all relays in one go.
	// Avoid using as can generate spike on mains supply.
	char buf[5];
	Relay_Open(Relay_Bank);

	buf[0] 					= spi_port;
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

JNIEXPORT void 		JNICALL Java_eRegulation_Relay_On			(JNIEnv *env, jobject obj, jint Relay_Bank, jint Relay_Number)	{	Relay_On(Relay_Bank, Relay_Number);		}
JNIEXPORT void 		JNICALL Java_eRegulation_Relay_Off			(JNIEnv *env, jobject obj, jint Relay_Bank, jint Relay_Number)	{	Relay_Off(Relay_Bank, Relay_Number);	}
JNIEXPORT jboolean 	JNICALL Java_eRegulation_Relay_IsOn			(JNIEnv *env, jobject obj, jint Relay_Bank, jint Relay_Number)	{	return Is_On(Relay_Bank, Relay_Number);	}
JNIEXPORT void 		JNICALL Java_eRegulation_Relays_OffAll		(JNIEnv *env, jobject obj, jint Relay_Bank)						{	Relays_OffAll(Relay_Bank);				}
JNIEXPORT void 		JNICALL Java_eRegulation_Relays_ScanAndSet	(JNIEnv *env, jobject obj, jint Relay_Bank)						{	Relays_ScanAndSet(Relay_Bank);			}


//================================================================
//
//  Debug here from Scan.c
//
void debug()
{
	Relay_Open(0);
	scanAndSet();
}
//
//================================================================


