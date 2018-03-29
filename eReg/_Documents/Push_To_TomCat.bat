@echo OFF

echo Only to be executed from work station
echo ===== not the server
pause


echo Pushing Main Classes to TomCat
===================================

     D:
     CD             D:\HVAC_Repository\git\HVAC\HVAC_Server\build\classes
     COPY  /Y       *.class                                             \\NAS\Users\AD\2_Projects\HVAC_Server\hw\WEB-INF\classes

    pause

echo Pushing HVAC_Common Classes to TomCat
==========================================

    D:
    CD              D:\HVAC_Repository\git\HVAC\HVAC_Common\bin\HVAC_Common
    XCOPY  /S  /Y   *.class                                             \\Nas\Users\AD\2_Projects\HVAC_Server\hw\WEB-INF\classes\HVAC_Common

    pause

echo Finished
=============

    CD             D:\HVAC_Repository\git\HVAC\eReg\_Documents
    Pause

