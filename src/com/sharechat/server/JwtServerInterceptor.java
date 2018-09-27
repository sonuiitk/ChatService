/*
 * Copyright 2016 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sharechat.server;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;




public class JwtServerInterceptor implements ServerInterceptor {
  

  private final String secret;

  public JwtServerInterceptor(String secret) {
    this.secret = secret;
  }

@Override
public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metaData,
		ServerCallHandler<ReqT, RespT> serverCallHandler) {
	// TODO Auto-generated method stub
	 String authHeader = metaData.get(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER));
	 Boolean proceedFurthur = false;
	       
	 if(!serverCall.getMethodDescriptor().getFullMethodName().trim().equalsIgnoreCase("ChatService/join")) {
		 if(authHeader != null) {
			 // "bearer "
	        String token = authHeader.substring(7);
	        
	        if(token != null && !token.trim().equalsIgnoreCase("")) {
		        Claims claims = Jwts.parser()
		       	       .setSigningKey(secret)
		       	       .parseClaimsJws(token).getBody();
		        proceedFurthur = true;
	        }
		 }
		 
		 if(proceedFurthur) {
			 return serverCallHandler.startCall(serverCall, metaData);
		 } else {
			 serverCall.close(Status.CANCELLED, metaData);
			 return new ServerCall.Listener() {};
		 }
	 } else {
		 return serverCallHandler.startCall(serverCall, metaData);
	 }
}
}