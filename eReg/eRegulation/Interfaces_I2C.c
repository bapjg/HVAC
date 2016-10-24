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

// #include "LCD.h"
JNIEXPORT void JNICALL Java_eRegulation_LCD_Clear  		(JNIEnv *, jobject);
JNIEXPORT void JNICALL Java_eRegulation_LCD_Position  	(JNIEnv *, jobject, jint, jint);
JNIEXPORT void JNICALL Java_eRegulation_LCD_BlickOn  	(JNIEnv *, jobject);
JNIEXPORT void JNICALL Java_eRegulation_LCD_BlickOff  	(JNIEnv *, jobject);
JNIEXPORT void JNICALL Java_eRegulation_LCD_Write  		(JNIEnv *, jobject, jstring);

// #include "Buttons.h"
JNIEXPORT jint JNICALL Java_eRegulation_Buttons_Read  	(JNIEnv *, jobject);
//
//================================================================

//#define ARRAY_SIZE(a)(sizeof(a) / sizeof((a)[0])) What is this for



//static uint8_t 	spi_mode;
//static uint8_t 	bits 		= 8;
//static uint32_t speed 		= 50000;							//Was 100000. Brought down to 50 000, which no longer worked from oct 2016.
//static uint16_t delay 		= 2;

static int 		i2c_fd;
static int 		i2c_port	= 0x94;
static char 	*i2c_device = " ";



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
	i2c_device 		= "/dev/i2c-1";
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

JNIEXPORT jint JNICALL Java_eRegulation_Buttons_Read	(JNIEnv *env, jobject obj)							{	return Buttons_Read();		}
JNIEXPORT void JNICALL Java_eRegulation_LCD_Clear		(JNIEnv *env, jobject obj)							{	LCD_Clear();				}
JNIEXPORT void JNICALL Java_eRegulation_LCD_Position	(JNIEnv *env, jobject obj, jint Line, jint Column)	{	LCD_Position(Line, Column);	}
JNIEXPORT void JNICALL Java_eRegulation_LCD_BlinkOn		(JNIEnv *env, jobject obj)							{	LCD_BlinkOn();				}
JNIEXPORT void JNICALL Java_eRegulation_LCD_BlinkOff	(JNIEnv *env, jobject obj)							{	LCD_BlinkOff();				}
JNIEXPORT void JNICALL Java_eRegulation_LCD_Write		(JNIEnv *env, jobject obj, jstring Message)
{
	char *nativestring =(*env)->GetStringUTFChars(env, Message, 0);
	char *anotherString = nativestring;
	LCD_Write(nativestring);
	(*env)->ReleaseStringUTFChars(env, Message, nativestring);
}
//
//================================================================



