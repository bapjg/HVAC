PACKAGE = eRegulation
EXECUTE = Control
PROJECT = /home/pi/HVAC/eReg/eRegulation

%.h:                    		%.class
	javah -jni -classpath ..  -o $*.h    $(PACKAGE).$*		


%.class:                		%.java
	javac      -classpath ..	$*.java


jni:
	make Relay.h
	make Relays.h
	make ADC.h
	make LCD.h
	make Buttons.h

so:
	rm -f *.so
	rm -f *.o
	gcc -I/usr/lib/jvm/java-6-openjdk-armhf/include -I/usr/lib/jvm/java-6-openjdk-armhf/include/linux -c Interfaces.c -o Interfaces.o
	gcc -shared -o libInterfaces.so Interfaces.o

complete:
	rm -f *.so
	rm -f *.o
	rm -f *.class
	rm -f *.h
	make

ex:
	git pull
	sudo java -cp "../:../../HVAC_Messages/bin/:javax.mail.jar" -Djava.library.path=./            eRegulation.Control

help:
	echo ex........... just execute as copiled in IDE
	echo so........... interface code in C only
	echo jni.......... interface code in Java only
	echo complete..... everything
	
scan:
	rm -f *.o
	rm -f Scan

	git stash save
	git stash drop
	git pull

	echo ====================
	gcc -I/usr/lib/jvm/java-6-openjdk-armhf/include -I/usr/lib/jvm/java-6-openjdk-armhf/include/linux -c Interfaces.c -o Interfaces.o
	
	echo ====================
	gcc  -c -o Scan.o Scan.c
	gcc     -o Scan   Scan.o Interfaces.o
	
	echo ====================
	./Scan
	
