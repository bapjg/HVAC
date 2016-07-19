cd /home/pi/HVAC/eReg/eRegulation/

sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 12:04
sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 13:08

STATUS=1
while [ $STATUS -eq 1 -o $STATUS -eq 6 -o $STATUS -eq 7]
do
	git stash save
	git stash drop
	git pull
	
	/home/pi/HVAC/eReg/_Documents/update.sh

	if [ $STATUS -eq 7 ];then
    	sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Djava.library.path=./            eRegulation.Control
    	STATUS=$?
    fi
    
    if [ $STATUS -eq 6 ];then
		sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005  -Djava.library.path=./  eRegulation.Control
    	STATUS=$?
	fi
	
	if [ $STATUS -eq 7 ];then
		sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=n,suspend=y,address=5005  -Djava.library.path=./  eRegulation.Control
    	STATUS=$?
	fi
done

if [ $STATUS -eq 2 ];then
	sudo shutdown -r now
fi

if [ $STATUS -eq 5 ];then
	sudo shutdown -h now
fi
