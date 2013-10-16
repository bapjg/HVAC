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

#include <wiringPi.h>        // Fonctions WiringPi

int main (void)              // Fonction principale
{
	// For DS18B20 thermometer

	printf ("Premier test - Clignotement DEL\nFin par Ctrl-C\n");   	// Message d'accueil

	int gpioChannel = 7;



	wiringPiSetupGpio() ;                                              	// Choix de la numérotation des broches du RPi
	pinMode (gpioChannel, OUTPUT) ;                                     // Choix mode Sortie de la broche GPIO 4
	for (;;)                                                        	// Boucle infinie
	{
		digitalWrite (4, HIGH) ;                                      	// DEL allumée
		delay (500) ;                                                 	// Attente 500ms
		digitalWrite (4, LOW) ;                                       	// DEL eteinte
		delay (500) ;                                                 	// Attente 500ms
	}
	return 0 ;                                                      	// Code retour
}
