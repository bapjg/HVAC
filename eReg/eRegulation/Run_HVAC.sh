cd /home/pi/HVAC/eReg/eRegulation/
git stash save
git stash drop
update



STATUS=1
while [ $STATUS -eq 1 ]
do
    sudo java -cp "../:../../HVAC_Messages/bin/:javax.mail.jar" -Djava.library.path=./            eRegulation.Control
    STATUS=$?
done