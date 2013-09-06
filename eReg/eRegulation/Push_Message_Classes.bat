REM     This is in                                D: \HVAC_Repository\git\HVAC\eReg\eRegulation

PROMPT  -
D:
CD      \HVAC_Repository\HVAC\eReg\eRegulation
REM COPY    Message_*.class                           D:\HVAC_Project\HVAC_Server\bin\eRegulation
COPY     Message_*.class                             \\NAS\Users_AD\2_Projects\HVAC_Server\hw\WEB-INF\classes\eRegulation


CD      \HVAC_Repository\HVAC\HVAC_Server\build\classes
COPY    *.class                                     \\NAS\Users_AD\2_Projects\HVAC_Server\hw\WEB-INF\classes

PAUSE

CD		\HVAC_Repository\HVAC\eReg\eRegulation