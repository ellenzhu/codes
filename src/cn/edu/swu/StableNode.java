package cn.edu.swu;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class StableNode {
	
	private DatagramSocket socket;
	private byte buffer[] = new byte[4096];
	private List<Message<?>> messages = new ArrayList<Message<?>>();
		
	public static void main(String[] args) {
		StableNode stableNode = new StableNode();
		try {
			stableNode.startup();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startup() throws IOException {	
		socket = new DatagramSocket(Constant.STABLE_NODE_PORT);	
		System.out.println("startup .....");
		while (true) {
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			//System.out.println(packet.getAddress().toString() + "\t" + packet.getPort());
			String data = new String(packet.getData(), 0, packet.getLength());
			//System.out.println(data);
			Message<?> message = Message.wrap(data);
			
			switch(message.getType()){
			case GET:
				this.doGet(message, socket, packet.getAddress(), packet.getPort());	
				break;
			case POST:
				this.doPost(message);
				break;
			case LOGIN:
				this.doLogin(message);				
				break;
			case USERS:
				this.doUsers(message);				
			}
		}
	}
	
	private void doPost(Message<?> message) throws IOException {
		this.saveMessage(message);	
	}
	
	private void doLogin(Message<?> message) throws IOException {
		this.saveMessage(message);	
	}	
	
	private void doUsers(Message<?> message){
		this.saveMessage(message);
	}
	
	private void doGet(Message<?> getCmd, DatagramSocket socket, InetAddress ip, int port) throws IOException {
		List<Message<?>> result = new ArrayList<Message<?>>();
		synchronized(this){
			List<Message<?>> remain = new ArrayList<Message<?>>();
			for(Message<?> msg : this.messages){
				System.out.println(msg.getType() + "\t" + msg.getReceiver() + "\t" + getCmd.getSender());
				if(msg.getReceiver().equals(getCmd.getSender())){
					result.add(msg);
				}else{
					remain.add(msg);
				}
				this.messages = remain;
			}		
		}
		this.sendBackMessages(result, socket, ip, port);
	}
		
	
	private void sendBackMessages(List<Message<?>> result, DatagramSocket socket, InetAddress clientIp, int clientPort) throws IOException{
		String data = ProtocolBuilder.serializeMessages(result);
		byte[] buffer = data.getBytes();
		socket.send(new DatagramPacket(
			buffer, buffer.length,  clientIp, clientPort
		));
	}

	private void saveMessage(Message<?> msg){
		this.messages.add(msg);
	}
	
}
