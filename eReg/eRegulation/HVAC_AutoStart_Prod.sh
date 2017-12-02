if ! [ -e /home/pi/etc_Setup/autostart_ORI ]; then
	
	echo First run : make backups
	
	cp /home/pi/.config/lxsession/LXDE-pi/autostart /home/pi/etc_Setup/autostart_ORI
	cp /home/pi/.config/lxsession/LXDE-pi/autostart /home/pi/etc_Setup/autostart_HVAC
	
	echo First run : update autostart_HVAC with extra line
	
	echo '@lxterminal --command /home/pi/HVAC/eReg/eRegulation/HVAC_Debug_Wait.sh' >> /home/pi/etc_Setup/autostart_HVAC
#	echo '@lxterminal --command /home/pi/HVAC/eReg/eRegulation/HVAC_Run.sh'        >> /home/pi/etc_Setup/autostart_HVAC
fi

echo Copy autostart_HVAC to .config directory

cp -f /home/pi/etc_Setup/autostart_HVAC /home/pi/.config/lxsession/LXDE-pi/autostart

echo Finished

sleep 5