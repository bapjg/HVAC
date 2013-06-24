PACKAGE = eRegulation
EXECUTE = Burner_Control
PROJECT = /home/pi/Eclipse/eRegulation/src

all: Thermometer.class Thermometers.class TemperatureGradient.class Control.class Relay.class Relays.class ADC.class Circuit_Abstract.class Circuit_HotWater.class Circuit_Mixer.class Circuit_Gradient.class TemperatureGradient.class Circuits.class  CircuitTask.class Boiler.class Burner.class Calendars.class LogIt.class Mixer.class Thread_Thermometers.class Thread_Mixer.class PID.class

libInterfaces.so: Interfaces.o
	gcc -shared -o libInterfaces.so Interfaces.o

Interfaces.o: Interfaces.c  Relay.h Relays.h ADC.h LCD.h Buttons.h
	gcc -I/usr/lib/jvm/java-6-openjdk-armhf/include -I/usr/lib/jvm/java-6-openjdk-armhf/include/linux -c Interfaces.c -o Interfaces.o

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


complete:
	rm -f *.so
	rm -f *.o
	rm -f *.class
	rm -f *.h
	make

ex:
	make
	sudo java -cp .. -Djava.library.path=./ eRegulation.Control

help:
	echo so........... interface code in C only
	echo jni.......... interface code in Java only
	echo complete..... everything
	
	
	
