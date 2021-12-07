#!/usr/bin/python3

import socket


ip = '127.0.0.1'
port = 9001

# Ouvrir une communication TCP sur un port arbitraire
with socket.socket() as client:

  # Se connecter au serveur
  client.connect((ip, port))

  # Envoyer et lire du texte via TCP
  while True:
    text = input('Texte à envoyer ?')
    if 'quit' == text.lower():
        print('ok, j\'arrête')
        break
    client.send(text.encode('utf-8'))
    print(f'Envoyé texte {text}')
    data,_ = client.recvfrom(8192)
    print(data.decode("utf-8"))