package cn.edu.swu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Message<T> {
	public static enum MessageType{
		POST,
		GET,
		LOGIN,
		USERS
	}
	
	private MessageType type;
	private String sender;
	private String receiver;
	private T content;
	private long timestamp;
	
	public Message() {
		
	}

	public Message(MessageType type, String sender, String receiver, T content){
		this.setType(type);
		this.setSender(sender);
		this.setReceiver(receiver);
		this.setContent(content);
	}

	public static Message<?> wrap(String data) throws IOException{
		StringReader stringReader = new StringReader(data);
		BufferedReader reader = new BufferedReader(stringReader);
		String protocol = reader.readLine();
		
		if(protocol.equals(Constant.CMD_LOGIN)){
			String sender = reader.readLine();
			String receiver = reader.readLine();
			String name = reader.readLine();
			String password = reader.readLine();
			User user = new User();
			user.setName(name);
			user.setPassword(password);
			
			Message<User> msg = new Message<User>();
			msg.setType(MessageType.LOGIN);
			msg.setSender(sender);
			msg.setReceiver(receiver);
			msg.setContent(user);
			msg.setTimestamp(Calendar.getInstance().getTimeInMillis());
			return msg;
		}else if(protocol.equals(Constant.PROTOCOL_POST)){
			String sender = reader.readLine();
			String receiver = reader.readLine();
			StringBuffer sb = new StringBuffer();
			String line = null;
			while((line = reader.readLine()) != null){
				sb.append(line).append("\r\n");
			}
			String result = sb.toString();
			if(result.endsWith("\r\n")){
				result = result.substring(0, result.length()-2);
			}
			
			Message<String> msg = new Message<String>();
			msg.setType(MessageType.POST);
			msg.setSender(sender);
			msg.setReceiver(receiver);
			msg.setContent(result);
			msg.setTimestamp(Calendar.getInstance().getTimeInMillis());
			return msg;
		}else if(protocol.equals(Constant.PROTOCOL_GET)){
			String sender = reader.readLine();			
			Message<String> msg = new Message<String>();
			msg.setType(MessageType.GET);
			msg.setSender(sender);
			msg.setReceiver("NONE");
			msg.setTimestamp(Calendar.getInstance().getTimeInMillis());
			return msg;
		}else if(protocol.equals(Constant.CMD_USERS)){
			String sender = reader.readLine();
			String receiver = reader.readLine(); 
			
			StringBuffer sb = new StringBuffer();
			String line = null;
			while((line = reader.readLine()) != null){
				sb.append(line).append("\r\n");
			}
			String result = sb.toString();
			if(result.endsWith("\r\n")){
				result = result.substring(0, result.length()-2);
			}
			
			Message<String> msg = new Message<String>();
			msg.setType(MessageType.USERS);
			msg.setSender(sender);
			msg.setReceiver(receiver);
			msg.setContent(result);
			msg.setTimestamp(Calendar.getInstance().getTimeInMillis());
			return msg;
		}
		return null;
	}
	
	public static List<Message<?>> parseMessages(String data) throws IOException {
		StringReader stringReader = new StringReader(data);
		BufferedReader reader = new BufferedReader(stringReader);
		String line = null;
		List<Message<?>> results = new ArrayList<Message<?>>();
		StringBuffer buffer = new StringBuffer();
		while((line = reader.readLine()) != null){
			if(line.equals(Constant.MESSAGE_SEPERATOR)){
				results.add(Message.wrap(buffer.toString()));
				buffer = new StringBuffer();
			}else{
				buffer.append(line).append("\r\n");
			}
		}
		return results;
	}
	
	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}
	
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}	
	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String toString(){
		switch(this.getType()){
		case GET:
			return "{" + this.getType().toString() + " : " + this.getSender() + " -> " 
			+ (this.getReceiver() == null ? "" : this.getReceiver()) + "}";			
		default:
			return "{\r\n" + this.getType().toString() + " : " + this.getSender() + " -> " 
			+ (this.getReceiver() == null ? "" : this.getReceiver()) + " : \r\n" 
			+ (this.getContent() == null ? "" : this.getContent().toString()) + "\r\n}";
		}
		
	}
	
}
