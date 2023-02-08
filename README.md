# Projet Réseau

## Start the server

Go to serveur foler and execute server.py  
Make sure to have python3 on your computer

## Start the client

Go to client folder and executes client.jar

## How to play

In the start menu, click on "Chercher des serveurs" on the left  
Then select your server and create a room by clicking on "Créer une partie" on the right  
Choose a name for your room, validate and you are in the game  
  
You can move in the map using ZQSD  
Plants are dangerous places where you can encounter wild creatures  
Your poketudiants can recover their energy at shrines  
You can fight against other players when you are on the same cell of the map  
On the left side of the window, you have multiple options with your team of poketudiants  

## Technical

The server is made with python and the client with Java. They communicate via TCP and UDP messages.  
UDP is used for broadcast when the client is looking for servers.  
