package cn.edu.swu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class ProtocolBuilder {

	/*
	 * POST Protocol: data from client and server
	 * 
	 * POST
	 * SENDER_ID
	 * RECIVER_ID
	 * MESSAGE
	 */
	
	public static String buildPost(String sender, String receiver, String message) {
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.PROTOCOL_POST).append("\r\n");
		sb.append(sender).append("\r\n");
		sb.append(receiver).append("\r\n");
		sb.append(message);
		return sb.toString();
	}
	
	
	/*
	 *  
	 * GET Protocol :  get data from stable node server
	 * 
	 * GET
	 * SENDER_ID 
	 */
	public static String buildGet(String sender){
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.PROTOCOL_GET).append("\r\n");
		sb.append(sender);
		return sb.toString();
	}
	
	/*
	 *  
	 * USER_LIST Protocol :  get data from stable node server
	 * 
	 * USER_LIST
	 * SENDER_ID 
	 */
	public static String buildUsers(String sender){
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.CMD_USERS).append("\r\n");		
		sb.append(sender).append("\r\n");
		sb.append(Constant.SERVER_ID);
		return sb.toString();
	}
	
	/*
	 * Login Protocol :  get data from stable node server

	 * LOGIN
	 * SENDER
	 * RECIVER
	 * USER
	 * PASSWORD
	 */
	public static String buildLogin(String sender, String username, String password){
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.CMD_LOGIN).append("\r\n");
		sb.append(sender).append("\r\n");
		sb.append(Constant.SERVER_ID).append("\r\n");
		sb.append(username).append("\r\n");
		sb.append(password);
		return sb.toString();
	}
	
	
	public static String buildUserList(List<User> users, String receiver){
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.CMD_USERS).append("\r\n");
		sb.append(Constant.SERVER_ID).append("\r\n");
		sb.append(receiver).append("\r\n");
		for(User user : users){
			sb.append(user.getName()).append("\r\n");
		}				
		return sb.toString();
	}
			
	public static String serializeMessages(List<Message<?>> messages){
		StringBuffer sb = new StringBuffer();
		for(Message<?> msg : messages){
			sb.append(msg.getType()).append("\r\n");
			sb.append(msg.getSender()).append("\r\n");
			sb.append(msg.getReceiver()).append("\r\n");
			if(msg.getContent() != null){
				sb.append(msg.getContent().toString()).append("\r\n");
			}
			sb.append(Constant.MESSAGE_SEPERATOR).append("\r\n");
		}
		return sb.toString();		
	}
}
