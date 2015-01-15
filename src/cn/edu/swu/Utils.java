package cn.edu.swu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Utils {
	
	public static String getLineInput(String label) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		while (true) {
			System.out.print(label);
			line = reader.readLine();
			if (line != null && !line.trim().equals("")) {
				return line;
			}else{
				//System.out.println("input nothing, try again.");
			}
		}
	}
	
	public static String getUserInput() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		StringBuffer sb = new StringBuffer();
		while (true) {
			line = reader.readLine();
			if (line != null && !line.trim().equals("")) {
				if(line.toUpperCase().equals(Constant.CMD_SEND)){
					return sb.toString();
				}else{
					sb.append(line).append("\r\n");
				}
			}else{
				System.out.println("input nothing, try again.");
			}
		}
	}
	
	public static void printMessages(List<Message<?>> msgs){
		for(Message<?> msg : msgs){
			System.out.println(msg.getType() + "\t" + msg.getSender()+"\t" + msg.getReceiver() + "\t" + msg.getContent());
		}
	}
	
	public static int getRandomPort() {
		return Double.valueOf(1024 + Math.random() * 1235).intValue();
	}
	
}
