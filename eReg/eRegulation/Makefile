PACKAGE = eRegulation
EXECUTE = Control
PROJECT = /home/pi/HVAC/eReg/eRegulation


so:							# This is still relevant as may need to rebuild Interfaces.so file Compiles and Links Interfaces_SPI and Interfaces_I2C
	rm -f *.so
	rm -f *.o
	gcc -Wall -O2 -I/usr/lib/jvm/java-6-openjdk-armhf/include -I/usr/lib/jvm/java-6-openjdk-armhf/include/linux -c Interfaces_SPI.c -o Interfaces_SPI.o
	gcc -Wall -O2 -I/usr/lib/jvm/java-6-openjdk-armhf/include -I/usr/lib/jvm/java-6-openjdk-armhf/include/linux -c Interfaces_I2C.c -o Interfaces_I2C.o
	gcc -Wall -O2 -shared -o libInterfaces_SPI.so Interfaces_SPI.o
	gcc -Wall -O2 -shared -o libInterfaces_I2C.so Interfaces_I2C.o

SPI:						# Compiles Interfaces_SPI
	make spi
spi:						# Compiles Interfaces_SPI
	rm -f Interfaces_SPI.so
	rm -f Interfaces_SPI.o
	gcc -Wall -O2 -I/usr/lib/jvm/java-6-openjdk-armhf/include -I/usr/lib/jvm/java-6-openjdk-armhf/include/linux -c Interfaces_SPI.c -o Interfaces_SPI.o
	gcc -Wall -O2 -shared -o libInterfaces_SPI.so Interfaces_SPI.o

I2C:						# Compiles Interfaces_I2C
	make i2c
i2c:						# Compiles Interfaces_I2C
	rm -f Interfaces_I2C.so
	rm -f Interfaces_I2C.o
	gcc -Wall -O2 -I/usr/lib/jvm/java-6-openjdk-armhf/include -I/usr/lib/jvm/java-6-openjdk-armhf/include/linux -c Interfaces_I2C.c -o Interfaces_I2C.o
	gcc -Wall -O2 -shared -o libInterfaces_I2C.so Interfaces_I2C.o


help:
#	echo ex........... just execute as copiled in IDE
	echo so........... all interface code in C only
	echo spi.......... SPI interface code in C only
	echo I2C.......... I2C interface code in C only



#%.h:                    		%.class
#	javah -jni -classpath ..  -o $*.h    $(PACKAGE).$*		
#
#
#%.class:                		%.java
#	javac      -classpath ..	$*.java


#jni:						# header files are already present in git.
#	make Relay.h
#	make Relays.h
#	make ADC.h
#	make LCD.h
#	make Buttons.h
	
#scan:				# lower case so that file never exists and make says nothing to update. This is for debugging Interface.c purposes
#	rm -f *.o
#	rm -f Scan
#
#	git stash save
#	git stash drop
#	git pull
#
#	gcc -Wall -O2 -I/usr/lib/jvm/java-6-openjdk-armhf/include -I/usr/lib/jvm/java-6-openjdk-armhf/include/linux -c Interfaces.c -o Interfaces.o
#	
#	gcc -Wall -O2 -c -o Scan.o Scan.c
#	gcc              -o Scan   Scan.o Interfaces.o
#	
#	sudo ./Scan

#complete:					# class files are now made on PC as Pi is too slow
#	rm -f *.so
#	rm -f *.o
#	rm -f *.class
#	rm -f *.h
#	make

	
