package com.sharechat.client;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ClientInterceptors;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.StatusException;
import redis.clients.jedis.Jedis;

public class JwtClientInterceptor implements ClientInterceptor {
	
	Integer userId;
	Jedis jedis;
	
	public JwtClientInterceptor(Integer userId, Jedis jedis) {
		// TODO Auto-generated constructor stub
		this.userId = userId;
		this.jedis = jedis;
	}

	@Override
	public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptior, 
			CallOptions callOptions, Channel channel) {
		// TODO Auto-generated method stub
		
		String jwtToken = jedis.get(userId + "_token");
		
		      return new ClientInterceptors.CheckedForwardingClientCall<ReqT, RespT>(channel.newCall(methodDescriptior, callOptions)) {
		        @Override
		        protected void checkedStart(Listener<RespT> responseListener, Metadata headers)
		            throws StatusException {
		            Metadata.Key<String> key = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
		            headers.put(key, "Header: " + jwtToken);
		          delegate().start(responseListener, headers);
		        }
		      };
		    }
	}
