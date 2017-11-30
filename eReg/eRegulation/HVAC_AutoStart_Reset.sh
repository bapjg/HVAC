echo Copy back autostart_ORI to .config directory
cp -f /home/pi/etc_Setup/autostart_ORI /home/pi/.config/lxsession/LXDE-pi/autostart

echo Delete autostart_ORI and autostart_HVAC
cd /home/pi/etc_Setup
rm autostart_ORI
rm autostart_HVAC

echo Finished

sleep 5