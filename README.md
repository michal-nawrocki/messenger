# Messenger

Based on the client-server architecture with server threads and
socket communication.

## How to use 

The server should be run as: 

    $ java Server

The clients should be run as: 

    $ java Client server-address

Once the client is running, you need to <code>login</code> providing username and password.
You can also register using:

    $ register
    $ <nickname>
    $ <password>

To send a message use:

    $ message
    $ <recipient's name>
    $ <message>

Everything is also explained by typing <code>help</code> in the program.  

## Solution outline

```
 user types  +--------------+     socket    +----------------+  queue for suitable user
 --------->  | ClientSender | ------------> | ServerReceiver | ------------------------>
             | thread       |               | thread         |  (determine using table)
             +--------------+               +----------------+
                                                           
                                                           
                                                           
                                                           
                                                           
 user reads  +----------------+    socket   +--------------+  my user's queue
 <---------  | ClientReceiver | <---------- | ServerSender | <-----------------
             | thread         |             | thread       |
             +----------------+             +--------------+ 
```