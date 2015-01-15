package cn.edu.swu;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.edu.swu.Message.MessageType;

public class ClientNode {
	
	private String stableNodeHost;
	private String clientId;
	private int stableNodePort;
	private int nodePort = Utils.getRandomPort();
	private DatagramSocket currentSocket = null; 

	public static void main(String[] args) throws IOException {
		System.out.println("Please input startup parameters ");
		String ip = Utils.getLineInput("Stable Node IP : ");
		String port = Utils.getLineInput("Stable Node Port : ");
			
		ip = "127.0.0.1";
		port = "1234";

		ClientNode client = new ClientNode();
		client.setStableNodeHost(ip);
		client.setStableNodePort(Integer.parseInt(port));

		client.startup();
	}

	public void startup() throws IOException {
		NetworkCollapse networkCollapse = new NetworkCollapse(6000){
			@Override
			protected void collapse() {
				nodePort = Utils.getRandomPort();
				//System.out.println("Switch to new port : " + clientNodePort);
			}
		};		
		(new Thread(networkCollapse)).run();
		
		Timer t = new Timer();
		t.scheduleAtFixedRate(
			new TimerTask(){
				@Override public void run() {
					try {
						excuteGet();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}, 
			1000, 3000
		);		
		
		while (true) {
			String command = Utils.getLineInput("-> ");			
			if(command.toUpperCase().startsWith(Constant.CMD_WRITE)){
				this.excutePost();
			}else if(command.toUpperCase().equals(Constant.CMD_LOGIN)){
				this.exceuteLogin();
			}else if(command.toUpperCase().equals(Constant.CMD_USERS)){
				this.executeUsers();
			}			
		}
	}
	
	private DatagramSocket getCurrentSocket() throws SocketException {
		// Check port number before send protocol
		synchronized(this){
			if( this.currentSocket == null ){
				this.currentSocket = new DatagramSocket(this.getNodePort());				
			}else if(currentSocket.getLocalPort() != this.getNodePort()) {
				this.currentSocket.close();
				this.currentSocket = new DatagramSocket(this.getNodePort());
			}else{
				
			}		
			return this.currentSocket;
		}
		
	}

	private void excutePost() throws IOException {
		if(this.getClientId() == null){
			System.out.println("Please login first.");
			return;
		}
		
		String receiver = Utils.getLineInput("To Receiver : ");
		String message = Utils.getUserInput();
		message = ProtocolBuilder.buildPost(this.getClientId(), receiver, message);
		this.sendUDP(message);		
	}
	
	private void excuteGet() throws IOException  {
		if(this.getClientId() == null){
			//System.out.println("No user login.");
			return;
		}
		
		this.sendUDP(ProtocolBuilder.buildGet(this.getClientId()));
		
		byte[] receiveBuffer = new byte[4096];
		DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);
		this.getCurrentSocket().receive(packet);
		String receiveData = new String(packet.getData(), 0, packet.getLength());
		if(receiveData != null && !receiveData.isEmpty()){
			//System.out.println(receiveData);
			List<Message<?>> messages = Message.parseMessages(receiveData);
			for(Message<?> msg : messages){
				MessageType type = msg.getType();			
				switch(type){
				case USERS:
					doUsers(msg);
					break;
				default:
					showMessage(msg);
				}
			}
		}
	}
	
	private void exceuteLogin() throws IOException{
		if(this.getClientId() != null){
			System.out.println(this.getClientId() + " current is login.");
			return;
		}
		String username = Utils.getLineInput("User name: ");
		String password = Utils.getLineInput("Password : ");		
		String ptcLoing = ProtocolBuilder.buildLogin(this.getClientId(), username, password);
		this.sendUDP(ptcLoing);
		this.setClientId(username);
	}
	
	private void executeUsers() throws UnknownHostException, IOException{
		if(this.getClientId() == null){
			System.out.println("Please login first.");
			return;
		}
		this.sendUDP(ProtocolBuilder.buildUsers(this.getClientId()));
	}
	
	private void doUsers(Message<?> msg){
		String str = msg.getContent().toString();
		String[] users = str.split("\r\n");
		System.out.println("\r\n---------------------------------------");
		System.out.println("\t   Online User List");
		System.out.println("---------------------------------------");		
		for(int i=1; i<=users.length; i++){
			System.out.println("  " + i + ". " + users[i-1]);
		}
		System.out.println("---------------------------------------");
	}
	
	private void showMessage(Message<?> msg){	
		System.out.println("\r\n[ " + msg.getSender() + " ] : " + msg.getContent().toString());
	}
	
	private void sendUDP(String data) throws IOException{
		byte[] buffer = data.getBytes();
		this.getCurrentSocket().send(new DatagramPacket(
			buffer, buffer.length, 
			InetAddress.getByName(this.getStableNodeHost()), this.getStableNodePort()
		));
	}

	public int getNodePort() {
		return this.nodePort;
	}
	
	public String getStableNodeHost() {
		return stableNodeHost;
	}

	public void setStableNodeHost(String stableNodeHost) {
		this.stableNodeHost = stableNodeHost;
	}

	public int getStableNodePort() {
		return stableNodePort;
	}

	public void setStableNodePort(int stableNodePort) {
		this.stableNodePort = stableNodePort;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

}
