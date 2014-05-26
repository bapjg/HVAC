cd /home/pi/HVAC/eReg/eRegulation/

STATUS=1
while [ $STATUS -eq 1 ]
do
	git stash save
	git stash drop
	git pull
	
	/home/pi/HVAC/eReg/_Documents/update.sh

#    sudo java -cp "../:HVAC_Common.jar:javax.mail.jar:gson-2.2.4.jar" -Djava.library.path=./            eRegulation.Control
#    sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Djava.library.path=./            eRegulation.Control
    STATUS=$?
done

if [ $STATUS -eq 2 ];then
	sudo shutdown -r now
fi