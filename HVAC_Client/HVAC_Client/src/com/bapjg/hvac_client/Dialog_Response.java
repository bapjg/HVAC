package com.bapjg.hvac_client;

public interface Dialog_Response
{
	void processFinishDialogInteger	(int fieldId, Integer 	reponse);
	void processFinishDialogString	(int fieldId, String 	reponse);
	void processFinishDialogLong	(int fieldId, Long 		reponse);
}

