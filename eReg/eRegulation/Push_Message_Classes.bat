D:
CD      \HVAC_Repository\git\HVAC\eReg\eRegulation
COPY    Message_*.class                           D:\HVAC_Project\HVAC_Server\bin\eRegulation
COPY    Message_*.class                           \\NAS\Users_AD\2_Projects\HVAC_Server\hw\WEB-INF\classes\eRegulation

CD 		D:\HVAC_Project\HVAC_Server\bin
COPY    Calendar.class                            \\NAS\Users_AD\2_Projects\HVAC_Server\hw\WEB-INF\classes
COPY    Monitor.class                             \\NAS\Users_AD\2_Projects\HVAC_Server\hw\WEB-INF\classes

PAUSE