REM     This is in                                D: \HVAC_Repository\git\HVAC\eReg\eRegulation

@ECHO OFF

PROMPT  -
D:

ECHO    Copying eRegulation classes
ECHO    ===========================

CD      \HVAC_Repository\HVAC\eReg\eRegulation
COPY     Message_*.class                             \\NAS\Users_AD\2_Projects\HVAC_Server\hw\WEB-INF\classes\eRegulation

ECHO    Copying HVAC_Server classes
ECHO    ===========================

CD      \HVAC_Repository\HVAC\HVAC_Server\build\classes
COPY    *.class                                     \\NAS\Users_AD\2_Projects\HVAC_Server\hw\WEB-INF\classes

ECHO    Copying HVAC_Client classes
ECHO    ===========================

CD      \HVAC_Repository\HVAC\HVAC_Client\HVAC_Client\bin\classes\com\bapjg\hvac_client
COPY    Mgmt_Msg_*.class                            \\NAS\Users_AD\2_Projects\HVAC_Server\hw\WEB-INF\classes\com\bapjg\hvac_client


PAUSE

CD      \HVAC_Repository\HVAC\eReg\eRegulation