#!/bin/bash

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

while (( STATUS == 1 || STATUS == 6 || STATUS == 7));		# while start/restart/debug (wait or nowait)
do
	cd /home/pi/HVAC/eReg/eRegulation/
	
	sudo git stash save
	sudo git stash drop
	sudo git pull
	
	/home/pi/HVAC/eReg/eRegulation/update.sh

	if [ $STATUS -eq 1 ];	# Normal Restart application
	then
 		echo Now restarting
    	sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Djava.library.path=./            eRegulation.Control
    	NEWSTATUS=$?
		sleep 5
    fi
    
    if [ $STATUS -eq 6 ];	# Debug Restart Wait
    then
		echo Now Debug/Wait
		sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005  -Djava.library.path=./  eRegulation.Control
    	NEWSTATUS=$?
		sleep 5
	fi
	
	if [ $STATUS -eq 7 ];	# Debug Restart No Wait
	then
		echo Now Debug/NoWait
		sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005  -Djava.library.path=./  eRegulation.Control
    	NEWSTATUS=$?
		sleep 5
	fi
	
	STATUS=$NEWSTATUS
	
done	# EndWhile

