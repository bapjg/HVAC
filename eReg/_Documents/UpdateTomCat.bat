@echo off

D:
cd D:\HVAC_Repository\HVAC\HVAC_Server\build\classes
copy /y *.class                   \\Nas\Users\AD\2_Projects\HVAC_Server\hw\WEB-INF\classes\


REM cd D:\HVAC_Repository\HVAC\eReg\eRegulation
REM copy /y Message*.class            \\Nas\Users\AD\2_Projects\HVAC_Server\hw\WEB-INF\classes\eRegulation

REM cd D:\HVAC_Repository\HVAC\HVAC_Client\HVAC_Client\bin\classes\com\bapjg\hvac_client
REM copy /y Mgmt_Msg_*.class          \\Nas\Users\AD\2_Projects\HVAC_Server\hw\WEB-INF\classes\com\bapjg\hvac_client

cd D:\HVAC_Repository\HVAC\HVAC_Common\bin\HVAC_Common
copy /y *.class                   \\Nas\Users\AD\2_Projects\HVAC_Server\hw\WEB-INF\classes\HVAC_Common

pause