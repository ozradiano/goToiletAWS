package dataTypes;

import enums.ELoginStatus;

public class UserData {
	private String userName;
	private String pass;
	private ELoginStatus userType;
	private String userId;

	public UserData() {

	}

	public UserData(String userName, String userPass, String id, String userType) {
		this.userName = userName;
		this.pass = userPass;
		this.setUserId(id);
		if(userType.equalsIgnoreCase("1")) {
			this.userType = ELoginStatus.EUserType_Garden;
		} else if(userType.equalsIgnoreCase("2")) {
			this.userType = ELoginStatus.EUserType_Parent;
		} else {
			this.userType = ELoginStatus.EInvalid;
		}
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public ELoginStatus getUserType() {
		return userType;
	}

	public void setUserType(ELoginStatus userType) {
		this.userType = userType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
