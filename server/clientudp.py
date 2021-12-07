#!/usr/bin/python3

import socket,time


ip = '<broadcast>'
port = 9000

SEARCH_SERVER = "looking for poketudiant servers"
ANSWER_SEARCH_SERVER = "i'm a poketudiant server"


# Ouvrir une communication UDP sur un port arbitraire
with socket.socket(socket.AF_INET, socket.SOCK_DGRAM, 0) as client:
  client.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
  # Envoyer et lire du texte via UDP
  while True:
    client.sendto(SEARCH_SERVER.encode('utf-8'), (ip, port))
    print(f'Envoyé texte {SEARCH_SERVER}')
    data, server = client.recvfrom(4096)
    response = data.decode("utf-8")
    if response == ANSWER_SEARCH_SERVER:
          print(f'Answer : {response} reçues de {server}')
    time.sleep(1)