cd /home/pi/HVAC/eReg/eRegulation/

sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 12:04
sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 13:08

STATUS=1

git stash save
git stash drop
git pull
	
/home/pi/HVAC/eReg/_Documents/update.sh

# change suspend=n to =y if required

sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" \
-Xdebug -Xnoagent                                                       \
-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005           \
-Djava.library.path=./            eRegulation.Control






