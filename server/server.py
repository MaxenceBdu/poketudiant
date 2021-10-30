#!/usr/bin/python3

import socket

ip = ""
port = 0

with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
  s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
  # Ecoute sur un port random
  s.bind((ip, port))
  print('Le serveur écoute en UDP')

  while True:
    print('-- j\'attends --')
    data, client = s.recvfrom(4096)
    print(f'Données : {data.decode("utf-8")} reçues de {client}')
    s.sendto("i'm a poketudian server.".encode('utf-8'),client)