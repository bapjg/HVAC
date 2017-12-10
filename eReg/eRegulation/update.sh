cd   /home/pi/HVAC/eReg/eRegulation

sudo cp -f Bashrc /home/pi/Bashrc
sudo cp -f HVAC_Auto*.sh  /home/pi/Desktop
sudo cp -f HVAC_Debug*.sh /home/pi/Desktop
sudo cp -f HVAC_Run*.sh   /home/pi/Desktop

cd /home/pi/Desktop
# remove old files if they are still there
rm HVAC_Prod.sh  2> /dev/null
rm HVAC_Reset.sh 2> /dev/null
