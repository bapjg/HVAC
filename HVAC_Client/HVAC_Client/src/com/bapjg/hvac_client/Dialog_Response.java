package com.bapjg.hvac_client;

public interface Dialog_Response
{
	void onReturnTemperature	(int fieldId, Integer 	reponse);
	void onReturnTime			(int fieldId, String 	reponse);
	void onReturnTime			(int fieldId, Long 		reponse);
}

