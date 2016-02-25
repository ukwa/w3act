package models;

import play.mvc.Controller;

public class FlashMessage {
	
	public Type messageType;
	public String text;
	
	public FlashMessage(Type messageType, String text) {
		this.messageType = messageType;
		this.text = text;
	}
	
	public FlashMessage(String message) {
		text = message;
		messageType = Type.WARNING;
		if (message.contains(": ")) {
			String[] messageParts = message.split(": ", 2);
			Type[] types = Type.values();
			for (int i=0; i < types.length; i++) {
				if (types[i].toString().equals(messageParts[0])) {
					messageType = types[i];
					text = messageParts[1];
					break;
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return messageType + ": " + text;
	}
	
	public void send() {
		Controller.flash("message", this.toString());
	}
	
	public static FlashMessage updateSuccess = new FlashMessage(FlashMessage.Type.SUCCESS, "Your changes have been saved.");
	
	public static FlashMessage validationWarning = new FlashMessage(FlashMessage.Type.WARNING, "Please fill out all the required fields.");
	
	public static FlashMessage sysAdminOnlyWarning = new FlashMessage(FlashMessage.Type.WARNING, "Only the system administrator can do that!");
	
	public enum Type {
		SUCCESS("success"), INFO("info"), WARNING("warning"), ERROR("danger");
	
		private String value;
	
		private Type(String value) {
			this.value = value;
		}
	
		public String extractText(String message) {
			return value;
		}
	
		@Override
		public String toString() {
			return value;
		}
	}
}