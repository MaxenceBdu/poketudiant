#!/usr/bin/python3

import socket,_thread,Partie,Client,CommandType,ConstantMessage

# Module Principal qui s'occupe de la gestion réseau du projet

ENCODING = "utf-8"
ip = ""
portUDP = 9000
portTCP = 9001
listeParties = []
NB_PARTIES_MAX = 4


# Méthode thréadée pour revecoir et envoyer des messages en broadcast UDP
def listenUDP(ip,port):
  with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
    s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    s.bind((ip, port))

    while True:
      data, client = s.recvfrom(4096)
      print("DEBUG :",f'Données : {data.decode("utf-8")} reçues de {client}')
      if data.decode("utf-8").strip() == ConstantMessage.ConstantMessage["CLIENT_SEARCH"]:
        s.sendto(ConstantMessage.ConstantMessage["SERVER_SEARCH_RESPONSE"].encode(ENCODING),client)

# Méthode thréadée pour revecoir  des message en TCP et les transmettre au reste du jeu pour la gestion
def listenClientTCP(client):
  clientConn = client.getClientConn()
  while True:
        try:
          data = clientConn.recv(4096)
        except ConnectionResetError:
          nbRestants = client.deconnexion()
          if nbRestants == 0:
            print("DEBUG : Suppression Partie")
            idPartie = client.getNomPartie()
            for ind in range(len(listeParties)):
              if idPartie == listeParties[ind].getNomPartie():
                listeParties.pop(ind)
                break
          break
        if len(data) == 0:
          nbRestants = client.deconnexion()
          if nbRestants == 0:
            print("DEBUG : Suppression Partie")
            idPartie = client.getNomPartie()
            for ind in range(len(listeParties)):
              if idPartie == listeParties[ind].getNomPartie():
                listeParties.pop(ind)
                break
          break
        
        data = data.decode(ENCODING).strip()
        if len(data) != 0:
          
          if client.getCommandType() == CommandType.CommandType["CONNECT"]:
            client.handleConnectCommand(data,listeParties,NB_PARTIES_MAX)
          elif client.getCommandType() == CommandType.CommandType["GAME"]:
            client.handleGameCommand(data)
          elif client.getCommandType() == CommandType.CommandType["BATTLE"]:
            client.handleBattleCommand(data)
          elif client.getCommandType() == CommandType.CommandType["SWITCH"]:
            client.handleSwitchCommand(data)
          elif client.getCommandType() == CommandType.CommandType["SWITCH_MORT"]:
            client.handleSwitchMortCommand(data)

# Méthode thréadée qui gère la connexion des joueurs au serveur
def listenTCP(ip,port):
  with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    s.bind((ip, port))
    s.listen(1)
    while True:
      client, addr = s.accept()
      print("DEBUG : Client trouvé")
      _thread.start_new_thread(listenClientTCP,(Client.Client(client,s,addr),))

# Méthode principale qui lance l'ecoute en UDP et TCP
def main():
  _thread.start_new_thread(listenTCP,(ip,portTCP))
  _thread.start_new_thread(listenUDP,(ip,portUDP))
  while 1:
   pass

main()
