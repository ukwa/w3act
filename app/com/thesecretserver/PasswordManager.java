package com.thesecretserver;

import com.thesecretserver.service.AddSecretResult;
import com.thesecretserver.service.ArrayOfInt;
import com.thesecretserver.service.ArrayOfString;
import com.thesecretserver.service.GetSecretResult;
import com.thesecretserver.service.SSWebService;
import com.thesecretserver.service.SSWebServiceSoap;
import com.thesecretserver.service.SecretItem;

public class PasswordManager {
	
	private String username;
	private String password;
	private String organization;
	private String domain;
	private SSWebServiceSoap ssWebServiceSoap;
	private static final int USERNAME_FIELD_ID = 39;
	private static final int PASSWORD_FIELD_ID = 40;
	private static final int SECRET_TYPE_ID = 9;
	private static final int FOLDER_ID = 12;
	
	public PasswordManager(String username, String password, String organization, String domain) {
		this.username = username;
		this.password = password;
		this.organization = organization;
		this.domain = domain;
		ssWebServiceSoap = new SSWebService().getSSWebServiceSoap();
	}

	public int addLoginCredentials(String secretName, LoginCredentials loginCredentials) {		
		AddSecretResult addSecretResult = ssWebServiceSoap.addSecret(authenticate(),
				SECRET_TYPE_ID, secretName,
				getSecretFieldIds(), getSecretItemValues(loginCredentials),
				FOLDER_ID);
		return addSecretResult.getSecret().getId();
	}
	
	public LoginCredentials getLoginCredentials(int secretId) {
		GetSecretResult getSecretResult = ssWebServiceSoap.getSecret(authenticate(), secretId, false, null);
		LoginCredentials loginCredentials = new LoginCredentials();
		for (SecretItem secretItem : getSecretResult.getSecret().getItems().getSecretItem()) {
			if (secretItem.getFieldId().equals(USERNAME_FIELD_ID)) {
				loginCredentials.username = secretItem.getValue();
			} else if (secretItem.getFieldId().equals(PASSWORD_FIELD_ID)) {
				loginCredentials.password = secretItem.getValue();
			}
		}
		return loginCredentials;
	}
	
	private String authenticate() {
		return ssWebServiceSoap.authenticate(username, password, organization, domain).getToken();
	}
	
	private ArrayOfInt getSecretFieldIds() {
		ArrayOfInt secretFieldIds = new ArrayOfInt();
		secretFieldIds.getInt().add(USERNAME_FIELD_ID);
		secretFieldIds.getInt().add(PASSWORD_FIELD_ID);
		return secretFieldIds;
	}
	
	private ArrayOfString getSecretItemValues(LoginCredentials loginCredentials) {
		ArrayOfString secretItemValues = new ArrayOfString();
		secretItemValues.getString().add(loginCredentials.username);
		secretItemValues.getString().add(loginCredentials.password);
		return secretItemValues;
	}
}