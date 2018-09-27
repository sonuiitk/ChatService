package com.sharechat.server;


import java.security.Key;
import java.util.Base64;
import java.util.logging.Logger;

import com.sharechat.client.ChatServiceGrpc;
import com.sharechat.client.ChatServiceGrpc.ChatService;

import io.grpc.Server;
import io.grpc.ServerInterceptors;
import io.grpc.netty.NettyServerBuilder;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import redis.clients.jedis.Jedis;

public class ChatServer  {
    private static final Logger logger = Logger.getLogger(ChatServer.class.getName());

    /* The port on which the server should run */
    private int port = 50051;
    private Server server;
    private ChatServiceGrpc.ChatService impl;
    
    private static final Key secret = MacProvider.generateKey(SignatureAlgorithm.HS256);
    private static final byte[] secretBytes = secret.getEncoded();
    private static final String base64SecretBytes = Base64.getEncoder().encodeToString(secretBytes);

    public ChatServer(ChatServiceGrpc.ChatService service) {
        this(service, 50051);
    }

    public ChatServer (ChatServiceGrpc.ChatService service, int port) {
    	this.impl = service;
        this.port = port;
    }

    public void start() throws Exception {
    	
    	server = NettyServerBuilder.forPort(port)
    			.addService(ServerInterceptors.intercept(ChatServiceGrpc.bindService(impl), new JwtServerInterceptor(base64SecretBytes)))
    			.build().start();
    	
    	server.awaitTermination();
        
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                ChatServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Main launches the server from the command line.
     */
    public static void main(String[] args) throws Exception {
    	Jedis jedis = new Jedis("localhost"); 
        ChatServiceGrpc.ChatService impl = new ChatServiceImpl(jedis, base64SecretBytes);
        final ChatServer server = new ChatServer(impl);
        server.start();
    }
}