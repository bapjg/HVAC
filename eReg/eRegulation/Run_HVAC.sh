cd /home/pi/HVAC/eReg/eRegulation/

# sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 12:04
# sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 13:08

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

while (( STATUS == 1 || STATUS == 6 || STATUS == 7));
do
	git stash save
	git stash drop
	git pull
	
	/home/pi/HVAC/eReg/_Documents/update.sh

	if [ $STATUS -eq 1 ];	# Normal Restart application
	then
    	sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Djava.library.path=./            eRegulation.Control
    	STATUS=$?
#     	sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:40			# Position Line 2 (ie 2 x 32 Hex) (0 to 3)
#     	sudo bw_tool -I -D /dev/i2c-1 -a 94 -t HVAC/eReg Quit.
#  		sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:60			# Position Line 3 (ie 3 x 32 Hex) (0 to 3)
#      	sudo bw_tool -I -D /dev/i2c-1 -a 94 -t Now restarting
		echo Now restarting
    fi
    
    if [ $STATUS -eq 6 ];	# Debug Restart Wait
    then
		sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005  -Djava.library.path=./  eRegulation.Control
    	STATUS=$?
#     	sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:40
#     	sudo bw_tool -I -D /dev/i2c-1 -a 94 -t HVAC/eReg Quit.
#  		sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:60
#      	sudo bw_tool -I -D /dev/i2c-1 -a 94 -t Now Debug/Wait
		echo Now Debug/Wait
	fi
	
	if [ $STATUS -eq 7 ];	# Debug Restart No Wait
	then
		sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005  -Djava.library.path=./  eRegulation.Control
    	STATUS=$?
#     	sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:40
#     	sudo bw_tool -I -D /dev/i2c-1 -a 94 -t HVAC/eReg Quit.
#  		sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:60
#      	sudo bw_tool -I -D /dev/i2c-1 -a 94 -t Now Debug/NoWait
		echo Now Debug/NoWait
	fi
	
	echo "Status is " $STATUS
# ??????????????????????
done
# ??????????????????????

if [ $STATUS -eq 2 ];	# Reboot
then
#	sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:40
#   sudo bw_tool -I -D /dev/i2c-1 -a 94 -t HVAC/eReg Quit.
#  	sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:60
#   sudo bw_tool -I -D /dev/i2c-1 -a 94 -t Now Rebooting
	echo Now Rebooting
	sudo shutdown -r now
fi

if [ $STATUS -eq 5 ];	# Shutdown
then
#  	sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:40
#  	sudo bw_tool -I -D /dev/i2c-1 -a 94 -t HVAC/eReg Quit.
# 	sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:60
#  	sudo bw_tool -I -D /dev/i2c-1 -a 94 -t Now Halting
	echo Now Halting
	sudo shutdown -h now
fi

if [ $STATUS -eq 0 ];	# GoTo Bash
then
#  	sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:40
#  	sudo bw_tool -I -D /dev/i2c-1 -a 94 -t HVAC/eReg Quit.
# 	sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 11:60
#  	sudo bw_tool -I -D /dev/i2c-1 -a 94 -t Going to Bash
	echo Going to bash
else
	echo "Status is " $STATUS
	echo Going to bash
fi
