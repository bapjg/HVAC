if ! [ -e /home/pi/etc_Setup/autostart_ORI ]; then
	cp /home/pi/.config/lxsession/LXDE-pi/autostart /home/pi/etc_Setup/autostart_ORI
	cp /home/pi/.config/lxsession/LXDE-pi/autostart /home/pi/etc_Setup/autostart_HVAC
	echo '@lxterminal --command /home/pi/HVAC/eReg/eRegulation/HVAC_Debug_Wait.sh' >> /home/pi/etc_Setup/autostart_HVAC
#	echo '@lxterminal --command /home/pi/HVAC/eReg/eRegulation/HVAC_Run.sh'        >> /home/pi/etc_Setup/autostart_HVAC
fi

cp -f /home/pi/etc_Setup/autostart_HVAC /home/pi/.config/lxsession/LXDE-pi/autostart


sleep 5