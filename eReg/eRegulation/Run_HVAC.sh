cd /home/pi/HVAC/eReg/eRegulation/

STATUS=1
while [ $STATUS -eq 1 ]
do
	git stash save
	git stash drop
	git pull
	
	/home/pi/HVAC/eReg/_Documents/update.sh

    sudo java -cp "../:../../HVAC_Messages/bin/:javax.mail.jar" -Djava.library.path=./            eRegulation.Control
    STATUS=$?
done

if [ $STATUS -eq 2 ];then
	shutdown -r now
fi