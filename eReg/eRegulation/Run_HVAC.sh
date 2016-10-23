cd /home/pi/HVAC/eReg/eRegulation/

sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 12:04
sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 13:08

echo $STATUS

STATUS=1
while (( STATUS != 0 && STATUS != 2 && STATUS != 5));
do
	git stash save
	git stash drop
	git pull
	
	/home/pi/HVAC/eReg/_Documents/update.sh

	if [ $STATUS -eq 1 ];	# Normal Restart application
	then
    	sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Djava.library.path=./            eRegulation.Control
    	STATUS=$?
    	sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:40			# Position Line 2 (ie 2 x 32 Hex) (0 to 3)
    	sudo bw_tool -I -D /dev/i2c-1 -a 94 -t HVAC/eReg Quit.
   		sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:60			# Position Line 3 (ie 3 x 32 Hex) (0 to 3)
     	sudo bw_tool -I -D /dev/i2c-1 -a 94 -t Now restarting
    fi
    
    if [ $STATUS -eq 6 ];	# Debug Restart Wait
    then
		sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005  -Djava.library.path=./  eRegulation.Control
    	STATUS=$?
    	sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:40
    	sudo bw_tool -I -D /dev/i2c-1 -a 94 -t HVAC/eReg Quit.
   		sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:60
     	sudo bw_tool -I -D /dev/i2c-1 -a 94 -t Now Debug/Wait
	fi
	
	if [ $STATUS -eq 7 ];	# Debug Restart No Wait
	then
		sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005  -Djava.library.path=./  eRegulation.Control
    	STATUS=$?
    	sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:40
    	sudo bw_tool -I -D /dev/i2c-1 -a 94 -t HVAC/eReg Quit.
   		sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:60
     	sudo bw_tool -I -D /dev/i2c-1 -a 94 -t Now Debug/NoWait
	fi
done

if [ $STATUS -eq 2 ];	# Reboot
then
    sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:40
    sudo bw_tool -I -D /dev/i2c-1 -a 94 -t HVAC/eReg Quit.
   	sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:60
    sudo bw_tool -I -D /dev/i2c-1 -a 94 -t Now Rebooting
	sudo shutdown -r now
fi

if [ $STATUS -eq 5 ];	# Shutdown
then
   	sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:40
  	sudo bw_tool -I -D /dev/i2c-1 -a 94 -t HVAC/eReg Quit.
	sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:60
   	sudo bw_tool -I -D /dev/i2c-1 -a 94 -t Now Halting
	sudo shutdown -h now
fi

if [ $STATUS -eq 0 ];	# GoTo Bash
then
   	sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:40
   	sudo bw_tool -I -D /dev/i2c-1 -a 94 -t HVAC/eReg Quit.
	sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:60
   	sudo bw_tool -I -D /dev/i2c-1 -a 94 -t Going to Bash
	echo Going to bash
fi
