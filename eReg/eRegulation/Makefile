PACKAGE = eRegulation
EXECUTE = Control
PROJECT = /home/pi/HVAC/eReg/eRegulation

%.h:                    		%.class
	javah -jni -classpath ..  -o $*.h    $(PACKAGE).$*		


%.class:                		%.java
	javac      -classpath ..	$*.java


#jni:						# header files are already present in git.
#	make Relay.h
#	make Relays.h
#	make ADC.h
#	make LCD.h
#	make Buttons.h

so:							# This is still relevant as may need to rebuild Interfaces.so file
	rm -f *.so
	rm -f *.o
	gcc -Wall -O2 -I/usr/lib/jvm/java-6-openjdk-armhf/include -I/usr/lib/jvm/java-6-openjdk-armhf/include/linux -c Interfaces.c -o Interfaces.o
	gcc -Wall -O2 -shared -o libInterfaces.so Interfaces.o

spi:						# Compiles Interfaces_spi
	rm -f Interfaces_SPI.so
	rm -f Interfaces_SPI.o
	gcc -Wall -O2 -I/usr/lib/jvm/java-6-openjdk-armhf/include -I/usr/lib/jvm/java-6-openjdk-armhf/include/linux -c Interfaces_SPI.c -o Interfaces_SPI.o
	gcc -Wall -O2 -shared -o libInterfaces_SPI.so Interfaces_SPI.o

#complete:					# class files are now made on PC as Pi is too slow
#	rm -f *.so
#	rm -f *.o
#	rm -f *.class
#	rm -f *.h
#	make

#ex:						# to execute use go (alias) or Run_HVAC.sh or Debug_HVAC.sh
#	git pull
#	sudo java -cp "../:../../HVAC_Messages/bin/:javax.mail.jar" -Djava.library.path=./            eRegulation.Control

help:
	echo ex........... just execute as copiled in IDE
	echo so........... interface code in C only
	echo jni.......... interface code in Java only
	echo complete..... everything
	
scan:				# lower case so that file never exists and make says nothing to update. This is for debugging Interface.c purposes
	rm -f *.o
	rm -f Scan

	git stash save
	git stash drop
	git pull

	gcc -Wall -O2 -I/usr/lib/jvm/java-6-openjdk-armhf/include -I/usr/lib/jvm/java-6-openjdk-armhf/include/linux -c Interfaces.c -o Interfaces.o
	
	gcc -Wall -O2 -c -o Scan.o Scan.c
	gcc              -o Scan   Scan.o Interfaces.o
	
	sudo ./Scan
	
