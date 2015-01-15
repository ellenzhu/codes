package cn.edu.swu;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.edu.swu.Message.MessageType;

public class ServerNode {
	
	private String stableNodeHost;
	private String nodeId;
	private int stableNodePort;
	private int nodePort = Utils.getRandomPort();
	private int refreshInterval = 1000;
	private DatagramSocket currentSocket = null;
	private List<User> users = new ArrayList<User>();

	public static void main(String[] args) throws IOException {
		System.out.println("Please input startup parameters ");
		String ip = Utils.getLineInput("Stable Node IP : ");
		String port = Utils.getLineInput("Stable Node Port : ");
		String interval = Utils.getLineInput("Refresh interval : ");
		String id = Constant.SERVER_ID;
		
		ip = "127.0.0.1";
		port = "1234";

		ServerNode server = new ServerNode();
		server.setStableNodeHost(ip);
		server.setStableNodePort(Integer.parseInt(port));
		server.setNodeId(id);
		server.setRefreshInterval(Integer.parseInt(interval));
		server.startup();
	}

	public void startup() throws IOException {
		NetworkCollapse networkCollapse = new NetworkCollapse(6000){
			@Override
			protected void collapse() {
				nodePort = Utils.getRandomPort();
			}
		};		
		(new Thread(networkCollapse)).run();
		
		Timer t = new Timer();
		t.scheduleAtFixedRate(
			new TimerTask(){
				@Override public void run() {
					try {
						getDataFromStableNode();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}, 
			1000, this.getRefreshInterval()
		);
	}
	
	private void getDataFromStableNode() throws IOException{		 
		DatagramSocket socket = this.getCurrentSocket();
		
		byte[] buffer = ProtocolBuilder.buildGet(this.getNodeId()).getBytes();
		socket.send(new DatagramPacket(
			buffer, buffer.length, 
			InetAddress.getByName(this.getStableNodeHost()), this.getStableNodePort()
		));
		
		byte[] receiveBuffer = new byte[4096];
		DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);
		socket.receive(packet);
		String receiveData = new String(packet.getData(), 0, packet.getLength());
		//System.out.println(receiveData);
		List<Message<?>> messages = Message.parseMessages(receiveData);

		for(Message<?> msg : messages){
			MessageType type = msg.getType();			
			switch(type){
				case LOGIN:
					doLogin(msg);
					break;
				case USERS:
					doUsers(msg);
					break;
				default:
			}
		}
	}
	
	private void doLogin(Message<?> message){
		//System.out.println(message.toString());
		User newuser = (User)message.getContent();
		for(User user : this.users){
			if(user.getName().equals(newuser.getName())){
				System.out.println("User already loged in.");
				return;
			}
		}
		this.users.add(newuser);
	}
	
	private void doUsers(Message<?> message) throws IOException{
		String result = ProtocolBuilder.buildUserList(users, message.getSender());
		//System.out.println("UserList : " + result);
		byte[] buffer = result.getBytes();
		this.getCurrentSocket().send(new DatagramPacket(
			buffer, buffer.length, 
			InetAddress.getByName(this.getStableNodeHost()), this.getStableNodePort()
		));
	}
	
	private DatagramSocket getCurrentSocket() throws SocketException {
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
	
	public int getNodePort() {
		return this.nodePort;
	}
	
	public void setNodePort(int nodePort) {
		this.nodePort = nodePort;
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

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String clientId) {
		this.nodeId = clientId;
	}

	public int getRefreshInterval() {
		return refreshInterval;
	}

	public void setRefreshInterval(int refreshInterval) {
		this.refreshInterval = refreshInterval;
	}

}
