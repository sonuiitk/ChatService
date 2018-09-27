package com.sharechat.client;

import java.util.Calendar;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sharechat.proto.Rpcchat;
import com.sharechat.proto.Rpcchat.ChatResponse;
import com.sharechat.proto.Rpcchat.Message;
import com.sharechat.proto.Rpcchat.Response;

import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import redis.clients.jedis.Jedis;

public class ChatClient {
    private static final Logger logger = Logger.getLogger(ChatClient.class.getName());

    private final io.grpc.Channel channel;
    private final ChatServiceGrpc.ChatServiceBlockingStub blockingStub;
    private Jedis jedis;

    public ChatClient(String host, int port) {
        channel = NettyChannelBuilder
        			.forAddress(host, port)
        			.negotiationType(NegotiationType.PLAINTEXT)
        			.build();
        blockingStub = ChatServiceGrpc.newBlockingStub(channel);
        jedis = new Jedis("localhost"); 
    }

    public Response login(Integer userId, String password) {
        try {
            Rpcchat.JoinRequest request = Rpcchat.JoinRequest.newBuilder()
            		.setUserid(userId)
            		.setPassword(password)
            		.build();
            Rpcchat.Response response = blockingStub.join(request);
           
            if(!response.getStatus().equalsIgnoreCase("ERROR")) {
            
	            /*
	             * Using this as a temporary storage for client. We can use cookies for this
	             */
	            String jwtToken = response.getToken();
	            jedis.set(userId + "_token", jwtToken);
	            return response;
            } else {
            	return response;
            }
        } catch (RuntimeException e) {
            logger.log(Level.WARNING, "RPC failed", e);
            return null;
        }
    }

    public Response leave(Integer userId) {
        try {
            Rpcchat.LeaveRequest request = Rpcchat.LeaveRequest.newBuilder().setUserid(userId).build();
            Rpcchat.Response response = blockingStub.withInterceptors(new JwtClientInterceptor(userId, jedis)).leave(request);
            return response;
        } catch (RuntimeException e) {
            logger.log(Level.WARNING, "RPC failed", e);
            return null;
        }
    }

    public Response sendMessage(Integer fromUserId, Message message) {
        try {
            Rpcchat.SendRequest request = Rpcchat.SendRequest.newBuilder()
            		.setUserid(fromUserId)
            		.setChat(message)
            		.build();
            Rpcchat.Response response = blockingStub.withInterceptors(new JwtClientInterceptor(fromUserId, jedis)).send(request);
            return response;
        } catch (RuntimeException e) {
            logger.log(Level.WARNING, "RPC failed", e);
            return null;
        }
    }

    public ChatResponse receiveMessage(Integer userId) {
        try {
            Rpcchat.RecvAllRequest request = Rpcchat.RecvAllRequest.newBuilder()
            		.setUserid(userId)
            		.build();
            
            Rpcchat.ChatResponse response = blockingStub
            		.withInterceptors(new JwtClientInterceptor(userId, jedis))
            		.recvAll(request);
            
            return response;
        } catch (RuntimeException e) {
            logger.log(Level.WARNING, "RPC failed", e);
            return null;
        }
    }


    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient("localhost", 50051);
        Scanner sc = null;
        try {
        	int userId = 1;
        	String password = "password";
            boolean stop = false;
            boolean firstRequest = true;
            do{
                if(firstRequest) {
	                Response response = client.login(userId, password);
	                if (response == null) {
	                    System.out.println("Some error");
	                } else if (response.getStatus().equalsIgnoreCase("error")) {
	                	System.out.println("Wrong password");
	                	break;
	                } else {
	                	System.out.println("Joined with userId: " + userId);
	                	firstRequest = false;
	                	ChatResponse chatResponse = client.receiveMessage(userId);
	                	
	                	System.out.println("Chat receive all response: " + chatResponse);
	                	
	                    if (chatResponse == null) {
	                        System.out.println("Some error");
	                        continue;
	                    }
	                    System.out.println("["+chatResponse.getStatus()+"]");
	                    for(Message message:chatResponse.getChatsList()){
	                        System.out.println("[From: "+message.getFromuserid()+"] " + message.getContent());
	                    }
	                    System.out.println("["+response.getStatus()+"] "+response.getMessage());
	                }
                }
            	
            	
                System.out.print("Command: ");
                sc = new Scanner(System.in);
                String str = sc.nextLine();
                String[] splited = str.split("\\s+");
                
                if (splited[0].equals("/LEAVE")){
                    if (splited.length != 1){
                        System.out.println("Usage: /LEAVE");
                    } else {
                        Response response = client.leave(userId);
                        if (response == null) {
                            System.out.println("Some error");
                            continue;
                        }
                        System.out.println("[" + response.getStatus() + "] " + response.getMessage());
                    }
                } else if (splited[0].equals("/EXIT")){
                    stop = true;
                } else {
                    StringBuffer message = new StringBuffer();
                    if (splited[0].startsWith("@")){
                        if (splited.length < 2){
                            System.out.println("Usage: @<userId> <text>");
                        } else {
                        	int toUserId = 0;
                        	try{
                        		toUserId = Integer.parseInt(splited[0].substring(1));
                        	} catch (Exception e) {
                        		System.out.println("After '@' please enter a number");
                        	}
                            for(int i = 1; i < splited.length; i++){
                                if (i > 1) message.append(" ");
                                message.append(splited[i]);
                            }
                            
                            if(toUserId != 0) {
	                            Message m = Rpcchat.Message.newBuilder()
	                            .setContent(message.toString())
	                            .setFromuserid(userId)
	                            .setTouserid(toUserId)
	                            .setTimestamp(Calendar.getInstance().getTimeInMillis())
	                            .build();
	                            
	                            
	                            Response response = client.sendMessage(userId, m);
	                            if (response == null) {
	                                System.out.println("Some error");
	                                continue;
	                            }
	                            System.out.println("["+response.getStatus()+"] "+response.getMessage());
                            }
                        }
                    } 
                }
                
                if (!stop){
                    ChatResponse chatResponse = client.receiveMessage(userId);
                    if (chatResponse == null) {
                        System.out.println("Some error");
                        continue;
                    }
                    System.out.println("["+chatResponse.getStatus()+"]");
                    for(Message message:chatResponse.getChatsList()){
                        System.out.println("[From: "+message.getFromuserid()+"] " + message.getContent());
                    }
                }
                
            } while (!stop);
            System.out.println("bye");
        } finally {
            if(sc != null) {
            	sc.close();
            }
        }
    }
}
