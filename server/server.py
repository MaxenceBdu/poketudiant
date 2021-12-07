#!/usr/bin/python3

import socket,_thread,Partie,Client,CommandType,ConstantMessage

ENCODING = "utf-8"
ip = ""
portUDP = 9000
portTCP = 9001
listeParties = []

def listenUDP(ip,port):
  with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
    s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    # Ecoute sur un port random
    s.bind((ip, port))
    print('Le serveur écoute en UDP')

    while True:
      print('-- j\'attends UDP --')
      data, client = s.recvfrom(4096)
      print(f'Données : {data.decode("utf-8")} reçues de {client}')
      if data.decode("utf-8").strip() == ConstantMessage.ConstantMessage["CLIENT_SEARCH"]:
        s.sendto(ConstantMessage.ConstantMessage["SERVER_SEARCH_RESPONSE"].encode(ENCODING),client)
      
def listenClientTCP(client):
  clientConn = client.getClientConn()
  while True:
        print('-- j\'attends --')
        try:
          data = clientConn.recv(4096)
        except ConnectionResetError:
          print('Le client s\'est déconnecté, j\'arrête')
          break
        if len(data) == 0:
          nbRestants = client.deconnexion()
          if nbRestants == 0:
            print("Suppression Partie")
            idPartie = client.getNomPartie()
            for ind in range(len(listeParties)):
              if idPartie == listeParties[ind].getNomPartie():
                listeParties.pop(ind)
                break

          print('Le client s\'est déconnecté, j\'arrête')
          break
        
        data = data.decode(ENCODING).strip()
        print('Données reçues : ' + data )
        if client.getCommandType() == CommandType.CommandType["CONNECT"]:
          print("Connect")
          client.handleConnectCommand(data,listeParties)
        elif client.getCommandType() == CommandType.CommandType["GAME"]:
          print("Game")
          client.handleGameCommand(data)
        elif client.getCommandType() == CommandType.CommandType["BATTLE"]:
          client.handleBattleCommand()

def listenTCP(ip,port):
  with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    s.bind((ip, port))
    print('Le serveur écoute en TCP')
    s.listen(1)
    while True:
      print('-- j\'attends TCP --')
      client, addr = s.accept()
      print("Client trouvé")
      _thread.start_new_thread(listenClientTCP,(Client.Client(client,s,addr),))

def main():
  _thread.start_new_thread(listenTCP,(ip,portTCP))
  _thread.start_new_thread(listenUDP,(ip,portUDP))
  while 1:
   pass

main()
