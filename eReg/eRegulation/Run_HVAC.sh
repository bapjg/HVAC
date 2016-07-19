cd /home/pi/HVAC/eReg/eRegulation/

sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 12:04
sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 13:08

STATUS=1
while (( STATUS != 2 && STATUS != 5));
do
	git stash save
	git stash drop
	git pull
	
	/home/pi/HVAC/eReg/_Documents/update.sh

	if [ $STATUS -eq 1 ];	# Normal Restart
	then
    	sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Djava.library.path=./            eRegulation.Control
    	STATUS=$?
    fi
    
    if [ $STATUS -eq 6 ];	# Debug Restart Wait
    then
		sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005  -Djava.library.path=./  eRegulation.Control
    	STATUS=$?
	fi
	
	if [ $STATUS -eq 7 ];	# Debug Restart No Wait
	then
		sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005  -Djava.library.path=./  eRegulation.Control
    	STATUS=$?
	fi
done

if [ $STATUS -eq 2 ];	# Reboot
then
	sudo shutdown -r now
fi

if [ $STATUS -eq 5 ];	# Shutdown
then
	sudo shutdown -h now
fi
