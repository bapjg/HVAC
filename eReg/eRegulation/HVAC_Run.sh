cd /home/pi/HVAC/eReg/eRegulation/

sudo git stash save
sudo git stash drop
sudo git pull

# sudo chmod 777 *.sh
	
/home/pi/HVAC/eReg/_Documents/update.sh

sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Djava.library.path=./            eRegulation.Control
 