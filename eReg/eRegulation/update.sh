cd   /home/pi/HVAC/eReg/_Documents

sudo cp -f Bashrc /home/pi/Bashrc

cd   /home/pi/HVAC/eReg/eRegulation

sudo cp -f HVAC_A*.sh /home/pi/Desktop
sudo cp -f HVAC_C*.sh /home/pi/Desktop
sudo cp -f HVAC_R*.sh /home/pi/Desktop

cd /home/pi/Desktop
# remove old files if they are still there
rm HVAC_Prod.sh  2> /dev/null
rm HVAC_Reset.sh 2> /dev/null
