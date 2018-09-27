package com.sharechat.server;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONException;
import org.json.JSONObject;

import com.sharechat.client.ChatServiceGrpc;
import com.sharechat.client.ChatServiceGrpc.ChatService;
import com.sharechat.proto.Rpcchat;
import com.sharechat.proto.Rpcchat.Message;

import io.grpc.stub.StreamObserver;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import redis.clients.jedis.Jedis;

public class ChatServiceImpl implements ChatServiceGrpc.ChatService  {

    private Jedis jedis;
    private String secretBytes;

    public ChatServiceImpl(Jedis jedis, String secretBytes){
        this.jedis = jedis;
        this.secretBytes = secretBytes;
    }

    
    /*
     * This method is used for login
     */
    public com.sharechat.proto.Rpcchat.Response join(com.sharechat.proto.Rpcchat.JoinRequest request) {
        Integer userId = request.getUserid();
        String password = request.getPassword();
        String hashedPass = "";
        
        String passwordInDb = jedis.get(userId + "_password");
        
		try {
			byte[] bytesOfMessage = password.getBytes();
			MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] thedigest = md.digest(bytesOfMessage);
	        hashedPass = DatatypeConverter.printHexBinary(thedigest).toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        
        if(hashedPass.equalsIgnoreCase(passwordInDb)) {
	        String jjwt = getJwtToken(userId);
	
	        //check if user already joined
	        if (jedis.get((userId.toString())) != null){
	            return Rpcchat.Response.newBuilder()
	                    .setStatus("OK").setToken(jjwt)
	                    .setMessage("userId "+userId+" already joined.").build();
	        } else {
	        	jedis.set(userId.toString(), userId.toString());
	            return Rpcchat.Response.newBuilder()
	                    .setStatus("OK")
	                    .setToken(jjwt)
	                    .setMessage(userId+" has joined.").build();
	        }
	    } else {
	    	return Rpcchat.Response.newBuilder()
                    .setStatus("ERROR")
                    .setMessage("Invalid password").build();
	    }
    }
    
    private String getJwtToken(Integer userId) {
     
        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
        		.setSubject(userId.toString()).claim("userid", "1")
        		.setIssuedAt(Calendar.getInstance().getTime())
        		.signWith(SignatureAlgorithm.HS256, secretBytes);
        
        return builder.compact();
    }

    public com.sharechat.proto.Rpcchat.Response leave(com.sharechat.proto.Rpcchat.LeaveRequest request) {
        Integer userId = request.getUserid();
        
        if (!jedis.exists(userId.toString())){
            return Rpcchat.Response.newBuilder()
                    .setStatus("ERROR").setMessage("userId "+userId+" didn't joined.").build();
        } else {
        	jedis.del(userId.toString());
            return Rpcchat.Response.newBuilder()
            .setStatus("OK").setMessage(userId+" removed.").build();
        }
    }

    /*
     * This method is used to send message
     */
    public com.sharechat.proto.Rpcchat.Response send(com.sharechat.proto.Rpcchat.SendRequest request) {
        Integer fromUserId = request.getUserid();
        Message m = request.getChat();
        
        byte[] utf8Bytes = null;
        String messageContent = m.getContent();
        try {
			utf8Bytes = messageContent.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        if(utf8Bytes.length <= 4*1024) {
	        
        	if(isMessageInTimeWindow(fromUserId)) {
        		
		        JSONObject json = new JSONObject();
		        try {
					json.put("fromuserid", m.getFromuserid());
					json.put("touserid", m.getTouserid());
			        json.put("content", m.getContent());
			        json.put("timestamp", m.getTimestamp());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        jedis.lpush(m.getTouserid()+"_l", json.toString());
		        pushInRedisForTimeStamp(fromUserId, json.toString());
		        return Rpcchat.Response.newBuilder().setStatus("OK").setMessage("").build();
        	} else {
        		return Rpcchat.Response.newBuilder().setStatus("ERROR").setMessage("Too frequent messages").build();
        	}
        } else {
        	return Rpcchat.Response.newBuilder().setStatus("ERROR").setMessage("Message should be less than 4KB").build();
        }
    }
    
    private void pushInRedisForTimeStamp(Integer fromUserId, String jsonStringMessage) {
    	List<String> newMessageList = new ArrayList<String>();
    	if(jedis.exists(fromUserId + "_m")) {
    		List<String> messageList = jedis.lrange(fromUserId + "_m", 0, 2);
    		for(int i=1; i<messageList.size(); i++) {
    			newMessageList.add(messageList.get(i));
    		}
    		newMessageList.add(jsonStringMessage);
    	} else {
    		newMessageList.add(jsonStringMessage);
    	}
    	jedis.del(fromUserId + "_m");
    	jedis.lpush(fromUserId + "_m", jsonStringMessage.toString());
    }
    
    private Boolean isMessageInTimeWindow(Integer fromUserId) {
    	Boolean isMessageInTimeWindow = false;
    	if(jedis.exists(fromUserId + "_m")) {
        	List<String> messageList = jedis.lrange(fromUserId + "_m", 0, 2);
        	String messageJson = messageList.get(0);
        	try {
				JSONObject obj = new JSONObject(messageJson);
				Long timeStamp = obj.getLong("timestamp");
				if(Calendar.getInstance().getTimeInMillis() - timeStamp > 5000) {
					isMessageInTimeWindow = true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
        	isMessageInTimeWindow = true;
        }
    	
    	return isMessageInTimeWindow;
    }

    
    /*
     * This method is used to receive message
     */
    public Rpcchat.ChatResponse recvAll(Rpcchat.RecvAllRequest request) {
        Integer userId = request.getUserid();
        List<String> list = new ArrayList<String>();
        if(jedis.exists(userId + "_l") != null) {
        	list = jedis.lrange(userId + "_l", 0, Integer.MAX_VALUE);
        	jedis.del(userId + "_l");
        }

        Rpcchat.ChatResponse.Builder builder = Rpcchat.ChatResponse.newBuilder();
        
        for(int i = 0; i<list.size(); i++) { 
            JSONObject json;
			try {
				json = new JSONObject(list.get(i));
				builder.addChats(Rpcchat.Message.newBuilder()
	            		.setContent(json.getString("content"))
	            		.setFromuserid(json.getInt("fromuserid"))
	            		.setTouserid(json.getInt("touserid"))
	            		.setTimestamp(json.getLong("timestamp"))
	            		.build()
	            	);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
         } 
        
        return builder.setStatus("OK").build();
    }

    
    public void join(Rpcchat.JoinRequest request, StreamObserver<Rpcchat.Response> responseObserver) {
        responseObserver.onNext(join(request));
        responseObserver.onCompleted();
    }

    
    public void leave(Rpcchat.LeaveRequest request, StreamObserver<Rpcchat.Response> responseObserver) {
        responseObserver.onNext(leave(request));
        responseObserver.onCompleted();
    }

    
    public void send(Rpcchat.SendRequest request, StreamObserver<Rpcchat.Response> responseObserver) {
        responseObserver.onNext(send(request));
        responseObserver.onCompleted();
    }

    
    public void recvAll(Rpcchat.RecvAllRequest request, StreamObserver<Rpcchat.ChatResponse> responseObserver) {
        responseObserver.onNext(recvAll(request));
        responseObserver.onCompleted();
    }
}
