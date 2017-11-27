cd /home/pi/HVAC/eReg/eRegulation/

git stash save
git stash drop
git pull
	
/home/pi/HVAC/eReg/_Documents/update.sh

sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005  -Djava.library.path=./  eRegulation.Control
