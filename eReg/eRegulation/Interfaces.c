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

static uint8_t 	spi_mode;
static uint8_t 	bits 		= 8;
static uint32_t speed 		= 50000;							//Was 100000
static uint16_t delay 		= 2;

static int 		spi_fd;
static int 		i2c_fd;
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
	if(spi_fd < 0)			 pabort("can't open device \n");

	ret 					= ioctl(spi_fd, SPI_IOC_WR_MODE, &spi_mode);
	if(ret == -1)			pabort("can't set spi mode \n");

	ret 					= ioctl(spi_fd, SPI_IOC_RD_MODE, &spi_mode);
	if(ret == -1)			pabort("can't get spi mode \n");

	ret 					= ioctl(spi_fd, SPI_IOC_WR_BITS_PER_WORD, &bits);
	if(ret == -1)			pabort("can't set bits per word \n");

	ret 					 = ioctl(spi_fd, SPI_IOC_RD_BITS_PER_WORD, &bits);
	if(ret == -1)			pabort("can't get bits per word \n");

	ret 					 = ioctl(spi_fd, SPI_IOC_WR_MAX_SPEED_HZ, &speed);
	if(ret == -1)			pabort("can't set max speed hz \n");

	ret 					 = ioctl(spi_fd, SPI_IOC_RD_MAX_SPEED_HZ, &speed);
	if(ret == -1)			pabort("can't get max speed hz \n");
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

	if(rlen > tlen) tr.len 	= rlen;
	else			 tr.len 	= tlen;
	 
	tr.tx_buf 					=(unsigned long) buf;
	tr.rx_buf 					=(unsigned long) buf;
	
	ret 						= ioctl(spi_fd, SPI_IOC_MESSAGE(1), &tr);
	
	if(ret < 1)
	{
		printf("can't send spi message, retrying \n");
		
		close(spi_fd);
		
		spi_open();
		
		tr.delay_usecs			= delay;
		tr.speed_hz				= speed;
		tr.bits_per_word		= bits;		

		if(rlen > tlen) tr.len = rlen;
		else			 tr.len = tlen;
		
		tr.tx_buf 				=(unsigned long) buf;
		tr.rx_buf 				=(unsigned long) buf;
	
		ret 					= ioctl(spi_fd, SPI_IOC_MESSAGE(1), &tr);
		if(ret < 1) 			pabort("Couldn't recover from the error \n");
		else 					printf("Recovered \n");
	}
}
//----------------------------------------------------------
static int get_reg_value8(int reg)
{
	// Routine from WB_Tools, not used

	char buf[5]; 

	buf[0] = addr | 1;
	buf[1] = reg;
	spi_txrx(buf, 2, 1);

	return buf[2];
}
//----------------------------------------------------------
static int get_reg_value16(int reg)
{
	// Routine from WB_Tools, not used

	char buf[5];

	buf[0] 						= addr | 1;
	buf[1] 						= reg;
	spi_txrx(buf, 2, 2);

	return buf[2] |(buf[3] << 8);
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
	if(found == NULL)
	{
		// The Relay board has lost its address
		// Scan all addresses to find where it is

		for(add = 0; add < 255; add += 2)						// Loop through addresses
		{
			buf[0] 				= add | 1;						// OR 1 means return status info
			buf[1] 				= 1;							// Ident
			spi_txrx(buf, 0x2, 0x20);
			
			found = strstr(buf, "spi_big");
			if(found != NULL)
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
		printf("scanAndSet found no ralays - will now abord \n");
		pabort("Aborting by scanAndSet \n");
	}
}
//----------------------------------------------------------
void Relay_Open(int Relay_Bank)
{
	// Routine to open relay bank (channel 0 or 1)
	int ret;

	if(Relay_Bank == 0)
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
		case 1:
		{
			printf("From ButtonsRead ");
			break;
		}
		case 2:
		{
			printf("From ADC_Initialise ");
			break;
		}
		case 3:
		{
			printf("From ADC_Read ");
			break;
		}
		case 4:
		{
			printf("From ADC_ReadAverage ");
			break;
		}
		case 5:
		{
			printf("From LCD_Clear ");
			break;
		}
		case 6:
		{
			printf("From LCD_Write ");
			break;
		}
		case 7:
		{
			printf("From LCD_Position ");
			break;
		}
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
				print_from_whom(caller);
				pabort("can't set i2c_adr addr");
			}
			i2c_adr = buf[0];
	}
	if (write(i2c_fd, buf+1, tlen-1) != (tlen-1))
	{
		print_from_whom(caller);
		pabort("can't write i2c");
	}

	if (rlen)
	{
		resultCode = read(i2c_fd, buf+tlen, rlen);

//		if (read(i2c_fd, buf+tlen, rlen) != rlen)
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
	char *device 		= "/dev/i2c-1";
	i2c_adr				= -1;
	i2c_fd			 	= open(device, O_RDWR);

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
	buf[0] 				= 0x94 | 1;								// Address of UI Board + 1 for Read
	buf[1] 				= 0x31;									// Command Read Buttons
	buf[2] 				= 0x00;									// Any data(required for command to take effect
	i2c_txrx(buf, 2, 1, 1);
	close(i2c_fd);
	return buf[2];
}
//----------------------------------------------------------
int ADC_Initialise(int Channels, int Samples, int Bits_To_Shift)
{
	UI_Open(2);
	
	char buf[5];
//	buf[0] 				= 0x94;									// Address of UI Board + 0 for Write
	buf[0] 				= 0x00;									// Address of UI Board + 0 for Write
	buf[1] 				= 0x80;									// Command : Set Number of channels to monitor
	buf[2] 				= Channels;								// Any data(required for command to take effect
	i2c_txrx(buf, 3, 0, 2);

	buf[0] 				= 0x00;									// Address of UI Board + 0 for Write
	buf[1] 				= 0x81;									// Command : Set Sample size(2 bytes)
	buf[2] 				= Samples;								// Number of samples over 2 bytes
	buf[3] 				= Samples >> 8;							// so do some bit shifting
	i2c_txrx(buf, 4, 0, 2);

	buf[0] 				= 0x00;									// Address of UI Board + 0 for Write
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
	buf[0] 				= 0x94 | 1;								// Address of UI Board + 1 for Read
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
	buf[0] 				= 0x94 | 1;								// Address of UI Board + 1 for Read
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
	buf[0] 				= 0x94;									// Address of UI Board
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
	buf[0] 				= 0x94;									// Address of UI Board
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
	//	 Bottom     8 bits : Column
	
	UI_Open(7);

	char buf[5];
	buf[0] 				= 0x94;									// Address of LCD
	buf[1] 				= 0x11;									// Command Position Cursor
	buf[2] 				= line*32 + col;						// Data : Position(3MSB=line, 5LSB=Col)
	
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

//
//================================================================

