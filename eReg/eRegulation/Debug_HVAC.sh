cd /home/pi/HVAC/eReg/eRegulation/

sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 12:04
sudo bw_tool -I -D /dev/i2c-1 -a 94 -w 13:08

STATUS=1

git stash save
git stash drop
git pull
	
/home/pi/HVAC/eReg/_Documents/update.sh

sudo java -cp "../:../../HVAC_Common/bin:javax.mail.jar:gson-2.2.4.jar" \
-Dcom.sun.management.jmxremote                                          \
-Dcom.sun.management.jmxremote.port=5005                                \
-Dcom.sun.management.jmxremote.authenticate=false                       \
-Dcom.sun.management.jmxremote.ssl=false                                \
-Djava.rmi.server.hostname=raspberrypi                                  \
-Djava.library.path=./            eRegulation.Control

