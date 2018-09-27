package com.sharechat.client;

import static io.grpc.stub.ServerCalls.asyncUnaryCall;

import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;

@javax.annotation.Generated("by gRPC proto compiler")
public class ChatServiceGrpc {

  // Static method descriptors that strictly reflect the proto.
  @SuppressWarnings("deprecation")
public static final io.grpc.MethodDescriptor<com.sharechat.proto.Rpcchat.JoinRequest,
      com.sharechat.proto.Rpcchat.Response> METHOD_JOIN =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          MethodDescriptor.generateFullMethodName("ChatService", "join"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.sharechat.proto.Rpcchat.JoinRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.sharechat.proto.Rpcchat.Response.getDefaultInstance()));
  @SuppressWarnings("deprecation")
public static final io.grpc.MethodDescriptor<com.sharechat.proto.Rpcchat.LeaveRequest,
      com.sharechat.proto.Rpcchat.Response> METHOD_LEAVE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          MethodDescriptor.generateFullMethodName("ChatService", "leave"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.sharechat.proto.Rpcchat.LeaveRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.sharechat.proto.Rpcchat.Response.getDefaultInstance()));
  @SuppressWarnings("deprecation")
public static final io.grpc.MethodDescriptor<com.sharechat.proto.Rpcchat.SendRequest,
      com.sharechat.proto.Rpcchat.Response> METHOD_SEND =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          MethodDescriptor.generateFullMethodName("ChatService", "send"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.sharechat.proto.Rpcchat.SendRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.sharechat.proto.Rpcchat.Response.getDefaultInstance()));
  @SuppressWarnings("deprecation")
public static final io.grpc.MethodDescriptor<com.sharechat.proto.Rpcchat.RecvAllRequest,
      com.sharechat.proto.Rpcchat.ChatResponse> METHOD_RECV_ALL =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          MethodDescriptor.generateFullMethodName("ChatService", "recvAll"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.sharechat.proto.Rpcchat.RecvAllRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.sharechat.proto.Rpcchat.ChatResponse.getDefaultInstance()));

  public static ChatServiceStub newStub(io.grpc.Channel channel) {
    return new ChatServiceStub(channel);
  }

  public static ChatServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ChatServiceBlockingStub(channel);
  }

  public static ChatServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ChatServiceFutureStub(channel);
  }

  public static interface ChatService {

    public void join(com.sharechat.proto.Rpcchat.JoinRequest request,
        io.grpc.stub.StreamObserver<com.sharechat.proto.Rpcchat.Response> responseObserver);

    public void leave(com.sharechat.proto.Rpcchat.LeaveRequest request,
        io.grpc.stub.StreamObserver<com.sharechat.proto.Rpcchat.Response> responseObserver);

    public void send(com.sharechat.proto.Rpcchat.SendRequest request,
        io.grpc.stub.StreamObserver<com.sharechat.proto.Rpcchat.Response> responseObserver);

    public void recvAll(com.sharechat.proto.Rpcchat.RecvAllRequest request,
        io.grpc.stub.StreamObserver<com.sharechat.proto.Rpcchat.ChatResponse> responseObserver);
  }

  public static interface ChatServiceBlockingClient {

    public com.sharechat.proto.Rpcchat.Response join(com.sharechat.proto.Rpcchat.JoinRequest request);

    public com.sharechat.proto.Rpcchat.Response leave(com.sharechat.proto.Rpcchat.LeaveRequest request);

    public com.sharechat.proto.Rpcchat.Response send(com.sharechat.proto.Rpcchat.SendRequest request);

    public com.sharechat.proto.Rpcchat.ChatResponse recvAll(com.sharechat.proto.Rpcchat.RecvAllRequest request);
  }

  public static interface ChatServiceFutureClient {

    public com.google.common.util.concurrent.ListenableFuture<com.sharechat.proto.Rpcchat.Response> join(
        com.sharechat.proto.Rpcchat.JoinRequest request);

    public com.google.common.util.concurrent.ListenableFuture<com.sharechat.proto.Rpcchat.Response> leave(
        com.sharechat.proto.Rpcchat.LeaveRequest request);

    public com.google.common.util.concurrent.ListenableFuture<com.sharechat.proto.Rpcchat.Response> send(
        com.sharechat.proto.Rpcchat.SendRequest request);

    public com.google.common.util.concurrent.ListenableFuture<com.sharechat.proto.Rpcchat.ChatResponse> recvAll(
        com.sharechat.proto.Rpcchat.RecvAllRequest request);
  }

  public static class ChatServiceStub extends io.grpc.stub.AbstractStub<ChatServiceStub>
      implements ChatService {
    private ChatServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ChatServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChatServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ChatServiceStub(channel, callOptions);
    }

    @java.lang.Override
    public void join(com.sharechat.proto.Rpcchat.JoinRequest request,
        io.grpc.stub.StreamObserver<com.sharechat.proto.Rpcchat.Response> responseObserver) {
    	ClientCalls.asyncUnaryCall(
          getChannel().newCall(METHOD_JOIN, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void leave(com.sharechat.proto.Rpcchat.LeaveRequest request,
        io.grpc.stub.StreamObserver<com.sharechat.proto.Rpcchat.Response> responseObserver) {
    	ClientCalls.asyncUnaryCall(
          getChannel().newCall(METHOD_LEAVE, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void send(com.sharechat.proto.Rpcchat.SendRequest request,
        io.grpc.stub.StreamObserver<com.sharechat.proto.Rpcchat.Response> responseObserver) {
    	ClientCalls.asyncUnaryCall(
          getChannel().newCall(METHOD_SEND, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void recvAll(com.sharechat.proto.Rpcchat.RecvAllRequest request,
        io.grpc.stub.StreamObserver<com.sharechat.proto.Rpcchat.ChatResponse> responseObserver) {
    	ClientCalls.asyncUnaryCall(
          getChannel().newCall(METHOD_RECV_ALL, getCallOptions()), request, responseObserver);
    }
  }

  public static class ChatServiceBlockingStub extends io.grpc.stub.AbstractStub<ChatServiceBlockingStub>
      implements ChatServiceBlockingClient {
    private ChatServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ChatServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChatServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ChatServiceBlockingStub(channel, callOptions);
    }

    @java.lang.Override
    public com.sharechat.proto.Rpcchat.Response join(com.sharechat.proto.Rpcchat.JoinRequest request) {
      return  ClientCalls.blockingUnaryCall(
          getChannel().newCall(METHOD_JOIN, getCallOptions()), request);
    }

    @java.lang.Override
    public com.sharechat.proto.Rpcchat.Response leave(com.sharechat.proto.Rpcchat.LeaveRequest request) {
      return ClientCalls.blockingUnaryCall(
          getChannel().newCall(METHOD_LEAVE, getCallOptions()), request);
    }

    @java.lang.Override
    public com.sharechat.proto.Rpcchat.Response send(com.sharechat.proto.Rpcchat.SendRequest request) {
      return ClientCalls.blockingUnaryCall(
          getChannel().newCall(METHOD_SEND, getCallOptions()), request);
    }

    @java.lang.Override
    public com.sharechat.proto.Rpcchat.ChatResponse recvAll(com.sharechat.proto.Rpcchat.RecvAllRequest request) {
      return ClientCalls.blockingUnaryCall(
          getChannel().newCall(METHOD_RECV_ALL, getCallOptions()), request);
    }
  }

  public static class ChatServiceFutureStub extends io.grpc.stub.AbstractStub<ChatServiceFutureStub>
      implements ChatServiceFutureClient {
    private ChatServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ChatServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChatServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ChatServiceFutureStub(channel, callOptions);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<com.sharechat.proto.Rpcchat.Response> join(
        com.sharechat.proto.Rpcchat.JoinRequest request) {
      return ClientCalls.futureUnaryCall(
          getChannel().newCall(METHOD_JOIN, getCallOptions()), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<com.sharechat.proto.Rpcchat.Response> leave(
        com.sharechat.proto.Rpcchat.LeaveRequest request) {
      return ClientCalls.futureUnaryCall(
          getChannel().newCall(METHOD_LEAVE, getCallOptions()), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<com.sharechat.proto.Rpcchat.Response> send(
        com.sharechat.proto.Rpcchat.SendRequest request) {
      return ClientCalls.futureUnaryCall(
          getChannel().newCall(METHOD_SEND, getCallOptions()), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<com.sharechat.proto.Rpcchat.ChatResponse> recvAll(
        com.sharechat.proto.Rpcchat.RecvAllRequest request) {
      return ClientCalls.futureUnaryCall(
          getChannel().newCall(METHOD_RECV_ALL, getCallOptions()), request);
    }
  }

  public static io.grpc.ServerServiceDefinition bindService(
      final ChatService serviceImpl) {
    return io.grpc.ServerServiceDefinition.builder("ChatService")
      .addMethod(io.grpc.ServerMethodDefinition.create(
          METHOD_JOIN,
          asyncUnaryCall(
            new io.grpc.stub.ServerCalls.UnaryMethod<
                com.sharechat.proto.Rpcchat.JoinRequest,
                com.sharechat.proto.Rpcchat.Response>() {
              @java.lang.Override
              public void invoke(
                  com.sharechat.proto.Rpcchat.JoinRequest request,
                  io.grpc.stub.StreamObserver<com.sharechat.proto.Rpcchat.Response> responseObserver) {
                serviceImpl.join(request, responseObserver);
              }
            })))
      .addMethod(io.grpc.ServerMethodDefinition.create(
          METHOD_LEAVE,
          asyncUnaryCall(
            new io.grpc.stub.ServerCalls.UnaryMethod<
                com.sharechat.proto.Rpcchat.LeaveRequest,
                com.sharechat.proto.Rpcchat.Response>() {
              @java.lang.Override
              public void invoke(
                  com.sharechat.proto.Rpcchat.LeaveRequest request,
                  io.grpc.stub.StreamObserver<com.sharechat.proto.Rpcchat.Response> responseObserver) {
                serviceImpl.leave(request, responseObserver);
              }
            })))
      .addMethod(io.grpc.ServerMethodDefinition.create(
          METHOD_SEND,
          asyncUnaryCall(
            new io.grpc.stub.ServerCalls.UnaryMethod<
                com.sharechat.proto.Rpcchat.SendRequest,
                com.sharechat.proto.Rpcchat.Response>() {
              @java.lang.Override
              public void invoke(
                  com.sharechat.proto.Rpcchat.SendRequest request,
                  io.grpc.stub.StreamObserver<com.sharechat.proto.Rpcchat.Response> responseObserver) {
                serviceImpl.send(request, responseObserver);
              }
            })))
      .addMethod(io.grpc.ServerMethodDefinition.create(
          METHOD_RECV_ALL,
          asyncUnaryCall(
            new io.grpc.stub.ServerCalls.UnaryMethod<
                com.sharechat.proto.Rpcchat.RecvAllRequest,
                com.sharechat.proto.Rpcchat.ChatResponse>() {
              @java.lang.Override
              public void invoke(
                  com.sharechat.proto.Rpcchat.RecvAllRequest request,
                  io.grpc.stub.StreamObserver<com.sharechat.proto.Rpcchat.ChatResponse> responseObserver) {
                serviceImpl.recvAll(request, responseObserver);
              }
            }))).build();
  }
}
