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
#include "Relay.h"
#include "Relays.h"
#include "LCD.h"
#include "ADC.h"
#include "Buttons.h"
//
//================================================================

#define ARRAY_SIZE(a)(sizeof(a) / sizeof((a)[0]))


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
static void scanAndSet(void);
static void Relay_Open(int Relay_Bank);
//
//================================================================

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


static uint8_t 	spi_mode;
static uint8_t 	bits 		= 8;
static uint32_t speed 		= 50000;							//Was 100000. Brought down to 50 000, which no longer worked from oct 2016.
static uint16_t delay 		= 2;

static int 		i2c_fd;
static int 		i2c_port	= 0x94;
static char 	*i2c_device = " ";

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

//================================================================
//
// SPI Interface : Relays
//

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
//----------------------------------------------------------
//static int get_reg_value8(int reg)
//{
//	// Routine from BW_Tools, not used
//
//	char buf[5];
//
//	buf[0] = spi_port | 1;
//	buf[1] = reg;
//	spi_txrx(buf, 2, 1);
//
//	return buf[2];
//}
//----------------------------------------------------------
//static int get_reg_value16(int reg)
//{
//	// Routine from BW_Tools, not used
//
//	char buf[5];
//
//	buf[0] 						= spi_port | 1;
//	buf[1] 						= reg;
//	spi_txrx(buf, 2, 2);
//
//	return buf[2] | (buf[3] << 8);
//}
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

	return;

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

JNIEXPORT void JNICALL Java_eRegulation_Relay_On(JNIEnv *env, jobject obj, jint Relay_Bank, jint Relay_Number)
{
	Relay_On(Relay_Bank, Relay_Number);
}
JNIEXPORT void JNICALL Java_eRegulation_Relay_Off(JNIEnv *env, jobject obj, jint Relay_Bank, jint Relay_Number)
{
	Relay_Off(Relay_Bank, Relay_Number);
}
JNIEXPORT jboolean JNICALL Java_eRegulation_Relay_IsOn(JNIEnv *env, jobject obj, jint Relay_Bank, jint Relay_Number)
{
	return Is_On(Relay_Bank, Relay_Number);
}
JNIEXPORT void JNICALL Java_eRegulation_Relays_OffAll(JNIEnv *env, jobject obj, jint Relay_Bank)
{
	Relays_OffAll(Relay_Bank);
}
JNIEXPORT void JNICALL Java_eRegulation_Relays_ScanAndSet(JNIEnv *env, jobject obj, jint Relay_Bank)
{
	Relays_ScanAndSet(Relay_Bank);
}
//
//================================================================

//================================================================
//
// I2C Interface : LCD & Buttons
//

static int 		i2c_adr;

//----------------------------------------------------------//----------------------------------------------------------
void print_from_whom(int caller)
{
	switch (caller)
	{
		case 1:		{	printf("From ButtonsRead \n");			break;		}
		case 2:		{	printf("From ADC_Initialise \n");		break;		}
		case 3:		{	printf("From ADC_Read \n");				break;		}
		case 4:		{	printf("From ADC_ReadAverage \n");		break;		}
		case 5:		{	printf("From LCD_Clear \n");			break;		}
		case 6:		{	printf("From LCD_Write \n");			break;		}
		case 7:		{	printf("From LCD_Position \n");			break;		}
		case 8:		{	printf("From LCD_BlickOn \n");			break;		}
		case 9:		{	printf("From LCD_BlickOff \n");			break;		}
	}
}
//----------------------------------------------------------//----------------------------------------------------------
static void i2c_txrx(char *buf, int tlen, int rlen, int caller)
{
	int resultCode = 0;

	if (buf[0] != i2c_adr)
	{
			if (ioctl(i2c_fd, I2C_SLAVE, buf[0] >> 1) < 0)
			{
				// Just try again
				if (ioctl(i2c_fd, I2C_SLAVE, buf[0] >> 1) < 0)
				{
					printf("i2c write failed to recover from set i2c addr error \n");
					print_from_whom(caller);
					pabort("can't set i2c_adr addr");
				}
				printf("i2c write successfully recovered from i2c addr error \n");
			}
			i2c_adr = buf[0];
	}
	if (write(i2c_fd, buf+1, tlen-1) != (tlen-1))
	{
		// Just try again
		if (write(i2c_fd, buf+1, tlen-1) != (tlen-1))
		{
			printf("i2c write failed to recover from write error \n");
			print_from_whom(caller);
			pabort("can't write i2c");
		}
		print_from_whom(caller);
		printf("i2c write successfully recovered from write error \n");
	}

	if (rlen)
	{
		resultCode = read(i2c_fd, buf+tlen, rlen);

		if (resultCode != rlen)
		{
			printf("read failed rc = %d \n", resultCode);
			print_from_whom(caller);
			pabort("can't read i2c");
		}
	}
}
//----------------------------------------------------------
void UI_Open(int caller)
{
//	char *i2c_device 	= "/dev/i2c-1";
	*i2c_device 		= "/dev/i2c-1";
	i2c_adr				= -1;
	i2c_fd			 	= open(i2c_device, O_RDWR);

	if (i2c_fd < 0)
	{
		print_from_whom(caller);
		pabort("can't open I2C-1");
	}
}
int Buttons_Read()
{
	UI_Open(1);

	char buf[5];
	buf[0] 				= i2c_port | 1;							// Address of UI Board + 1 for Read
	buf[1] 				= 0x31;									// Command Read Buttons
	buf[2] 				= 0x00;									// Any data(required for command to take effect
	i2c_txrx(buf, 2, 1, 1);
	close(i2c_fd);
	return buf[2];
}
//----------------------------------------------------------
void ADC_Initialise(int Channels, int Samples, int Bits_To_Shift)
{
	UI_Open(2);
	
	char buf[5];
	buf[0] 				= i2c_port;								// Address of UI Board + 0 for Write
	buf[1] 				= 0x80;									// Command : Set Number of channels to monitor
	buf[2] 				= Channels;								// Any data(required for command to take effect
	i2c_txrx(buf, 3, 0, 2);

	buf[0] 				= i2c_port;								// Address of UI Board + 0 for Write
	buf[1] 				= 0x81;									// Command : Set Sample size(2 bytes)
	buf[2] 				= Samples;								// Number of samples over 2 bytes
	buf[3] 				= Samples >> 8;							// so do some bit shifting
	i2c_txrx(buf, 4, 0, 2);

	buf[0] 				= i2c_port;								// Address of UI Board + 0 for Write
	buf[1] 				= 0x82;									// Command : Set Number of bit to shift when polling for result
	buf[2] 				= Bits_To_Shift;						// Number of bits to shift
	i2c_txrx(buf, 3, 0, 2);

	close(i2c_fd);
}
//----------------------------------------------------------
int ADC_Read()
{
	UI_Open(3);

	char buf[5];
	buf[0] 				= i2c_port | 1;							// Address of UI Board + 1 for Read
	buf[1] 				= 0x61;									// Command Read Analog input
	buf[2] 				= 0x00;									// Any data(required for command to take effect
	i2c_txrx(buf, 2, 2, 3);
	close(i2c_fd);

	return buf[2] | (buf[3] << 8); 								// Return with bytes reordered
}
//----------------------------------------------------------
int ADC_ReadAverage()
{
	UI_Open(4);

	char buf[5];
	buf[0] 				= i2c_port | 1;							// Address of UI Board + 1 for Read
	buf[1] 				= 0x69;									// Command Read bit shifted sum of Analog input
	buf[2] 				= 0x00;									// Any data(required for command to take effect
	i2c_txrx(buf, 2, 2, 4);
	close(i2c_fd);

	return buf[2] | (buf[3] << 8); 								// Return with bytes reordered
}
//----------------------------------------------------------
void LCD_Clear()
{
	UI_Open(5);

	char buf[5];
	buf[0] 				= i2c_port;								// Address of UI Board
	buf[1] 				= 0x10;									// Command Clear Screen
	buf[2] 				= 0xaa;									// Any data(required for command to take effect
	i2c_txrx(buf, 3, 0, 5);
	close(i2c_fd);
}
//----------------------------------------------------------
void LCD_Write(char message[])
{
	UI_Open(6);

	int l;
	l 					= strlen(message);
	char buf[l+2];
	buf[0] 				= i2c_port;								// Address of UI Board
	buf[1] 				= 0x00;									// Command Write Data(Data follows)
	strcpy(buf+2, message);
	i2c_txrx(buf, l + 2, 0, 6);
	close(i2c_fd);
}
//----------------------------------------------------------
void LCD_Position(int line, int col)
{
	//   Position is coded as
	//	 Top		3 bits : Line
	//	 Bottom     5 bits : Column
	
	UI_Open(7);

	char buf[5];
	buf[0] 				= i2c_port;								// Address of LCD
	buf[1] 				= 0x11;									// Command Position Cursor
	buf[2] 				= line*32 + col;						// Data : Position(3MSB=line, 5LSB=Col)
	
	i2c_txrx(buf, 3, 0, 7);
	close(i2c_fd);
}
//----------------------------------------------------------
void LCD_BlinkOn()
{
	UI_Open(8);

	char buf[5];
	buf[0] 				= i2c_port;								// Address of LCD
	buf[1] 				= 0x01;									// Command ???
	buf[2] 				= 0x0d;									// Data : Blick On

	i2c_txrx(buf, 3, 0, 7);
	close(i2c_fd);
}
//----------------------------------------------------------
void LCD_BlinkOff()
{
	UI_Open(9);

	char buf[5];
	buf[0] 				= i2c_port;								// Address of LCD
	buf[1] 				= 0x01;									// Command ???
	buf[2] 				= 0x0c;									// Data : Blick Off

	i2c_txrx(buf, 3, 0, 7);
	close(i2c_fd);
}
JNIEXPORT void JNICALL Java_eRegulation_ADC_Initialise(JNIEnv *env, jobject obj, jint Channels, jint Samples, jint Bits_To_Shift)
{
	ADC_Initialise(Channels, Samples, Bits_To_Shift);
}
JNIEXPORT jint JNICALL Java_eRegulation_ADC_Read(JNIEnv *env, jobject obj)
{
	return ADC_Read();
}
JNIEXPORT jint JNICALL Java_eRegulation_ADC_ReadAverage(JNIEnv *env, jobject obj)
{
	return ADC_ReadAverage();
}
JNIEXPORT jint JNICALL Java_eRegulation_Buttons_Read(JNIEnv *env, jobject obj)
{
	return Buttons_Read();
}
JNIEXPORT void JNICALL Java_eRegulation_LCD_Clear(JNIEnv *env, jobject obj)
{
	LCD_Clear();
}
JNIEXPORT void JNICALL Java_eRegulation_LCD_Write(JNIEnv *env, jobject obj, jstring Message)
{
	char *nativestring =(*env)->GetStringUTFChars(env, Message, 0);
	LCD_Write(nativestring);
	(*env)->ReleaseStringUTFChars(env, Message, nativestring);
}
JNIEXPORT void JNICALL Java_eRegulation_LCD_Position(JNIEnv *env, jobject obj, jint Line, jint Column)
{
	LCD_Position(Line, Column);
}
JNIEXPORT void JNICALL Java_eRegulation_LCD_BlinkOn(JNIEnv *env, jobject obj)
{
	LCD_BlinkOn();
}
JNIEXPORT void JNICALL Java_eRegulation_LCD_BlinkOff(JNIEnv *env, jobject obj)
{
	LCD_BlinkOff();
}
//
//================================================================


