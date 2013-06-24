PACKAGE = Calibration
EXECUTE = DS18B20
PROJECT = /home/pi/Eclipse/eRegulation/src

all: Thermometer.class Thermometers.class DS18B20.class 

libInterfaces.so: Interfaces.o
	gcc -shared -o libInterfaces.so Interfaces.o

Interfaces.o: Interfaces.c Relay.h Relays.h UserInterface.h
	gcc -I/usr/lib/jvm/java-6-openjdk-armhf/include -I/usr/lib/jvm/java-6-openjdk-armhf/include/linux -c Interfaces.c -o Interfaces.o

%.h:                    %.class
	cd		/home/pi/Eclipse/Burner_Control
	javah -jni      hvac.bapjg.com.$*


%.class:                %.java
	javac  -Xlint:unchecked        $*.java


ccode:
	rm -f *.so
	rm -f *.o
	make
	sudo java -Djava.library.path="."  Burner_Control

bc:
	rm Burner_Control.class
	make
	sudo java -Djava.library.path="."  Burner_Control

complete:
	rm -f *.so
	rm -f *.o
	rm -f *.class
	rm -f *.h
	make
	cd ..
	sudo java -cp .  $(PACKAGE).$(EXECUTE)

help:
	echo ccode........ interface code in C only
	echo bc........... just burner_class
	echo complete..... everything
