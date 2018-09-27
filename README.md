# ChatService

The code contains 3 folders. "proto" folder contains the code which was generated by protoc. "client" folder contains the client side code and "server" folder contains the server side code. 
To run the server, go to "server" folder and run "ChatServer" file. To run a client go to "client" folder and run the "ChatClient" file. If you want to run another client, go to main function in this file and change the userId and password in the "main" method.

Right now the everything is stored in Redis. For JWT tokens also, I am using redis as of now but it can be fetched from client side through some cookie. 
So send a message - type @<user-id> <message>

Not implemented the docker file with this code.
