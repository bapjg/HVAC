#!/bin/bash

cd /home/pi/HVAC/eReg/eRegulation/

sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Djava.library.path=./            eRegulation.Control
 

# Status	Description
#   0       Stop application and go to bash
#   1       Normal Start/Restart application
#   2       Reboot controler
#   3       
#   4       
#   5       Shutdown controler
#   6       Debug mode and    wait for debugger to connect
#   7       Debug mode and NO wait for debugger to connect


STATUS=1

# while start/restart/debug (wait or nowait)

while (( STATUS == 1 || STATUS == 6 || STATUS == 7));
do
	sudo git stash save
	sudo git stash drop
	sudo git pull
	
	/home/pi/HVAC/eReg/eRegulation/update.sh

	if [ $STATUS -eq 1 ];	# Normal Restart application
	then
    	sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Djava.library.path=./            eRegulation.Control
    	STATUS=$?
		echo Now restarting
    fi
    
    if [ $STATUS -eq 6 ];	# Debug Restart Wait
    then
		sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005  -Djava.library.path=./  eRegulation.Control
    	STATUS=$?
		echo Now Debug/Wait
	fi
	
	if [ $STATUS -eq 7 ];	# Debug Restart No Wait
	then
		sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005  -Djava.library.path=./  eRegulation.Control
    	STATUS=$?
		echo Now Debug/NoWait
	fi
	
done	# EndWhile

