import _thread,TileMap,Pile,Partie

NB_JOUEURS = 4

class Partie:

    def __init__(self,nom):
        self.__nom = nom
        self.__joueurs = [None,None,None,None]
        self.__nbJoueurs = 0
        self.indiceJoueur = Pile.Pile()
        self.__posJoueur = {}
        self.__map = []
        self.__mutexSend = _thread.allocate_lock()
        with open("World.map", "r") as f:
            for l in f:
                ligne = []
                for case in l:
                    if case in {"0","1","2","3"}:
                        c = TileMap.TileMap(" ",case)
                        ligne.append(c)
                        self.__posJoueur[case] = c
                    else:
                        ligne.append(TileMap.TileMap(case))
                self.__map.append(list(ligne))
        self.__tailleMap = (len(self.__map)-1,len(self.__map[0])-1)

    def getNomPartie(self):
        return self.__nom

    def getNbJoueurs(self):
        return self.__nbJoueurs
    
    def sendMap(self,joueur):
        clientConn = joueur.getClientConn()
        indiceJoueur = str(joueur.getIndiceJoueur())
        indiceRivaux = 1
        response = "map " + str(self.__tailleMap[0]) + " " + str(self.__tailleMap[1] - 1) + "\n"
        clientConn.send(response.encode("UTF-8"))
        response = ""
        for lig in self.__map: 
            for col in lig:
                if col.contientJoueur():
                    indiceJoueurCase = col.getIndiceJoueur()
                    if indiceJoueurCase == indiceJoueur:
                        response += "0"
                    else:
                        response += str(indiceRivaux)
                        indiceRivaux += 1
                else:
                    response += col.getTypeTile()
        clientConn.send(response.encode("UTF-8"))
        
    def sendMapToAll(self):
        self.__mutexSend.acquire()
        for i in self.__joueurs:
            if i != None:
                self.sendMap(i)
        self.__mutexSend.release()

    def rejoindrePartie(self,joueur):
        if not self.indiceJoueur.estVide():
            indiceJoueur = self.indiceJoueur.pop()
            self.__posJoueur[str(indiceJoueur)].setConnecte(True)
            self.__joueurs[indiceJoueur] = joueur
            self.__nbJoueurs += 1
            return (indiceJoueur),True
        return -1,False

    def findTileAvecJoueur(self,indiceJoueur):
        for lig in range(self.__tailleMap[0]+1):
            for col in range(self.__tailleMap[1]+1):
                if self.__map[lig][col].getIndiceJoueur() != "":
                    print(self.__map[lig][col].getIndiceJoueur())
                if self.__map[lig][col].contientJoueur() and self.__map[lig][col].getIndiceJoueur() == indiceJoueur:
                    return lig,col

    def movePlayer(self,indiceJoueur,direction):
        strIndiceJoueur = str(indiceJoueur)
        yJoueur, xJoueur = self.findTileAvecJoueur(strIndiceJoueur)
        caseJoueur = self.__posJoueur[strIndiceJoueur]
        moved = False
        if direction == "left":
            if xJoueur - 1 >= 0:
                caseJoueur.removeJoueur()
                caseJoueur.setConnecte(False)
                caseJoueur = self.__map[yJoueur][xJoueur-1]
                caseJoueur.setJoueur(strIndiceJoueur)
                caseJoueur.setConnecte(True)
                self.__posJoueur[strIndiceJoueur] = caseJoueur
                moved = True
        elif direction == "right":
            if xJoueur + 1 < self.__tailleMap[1]:
                caseJoueur.removeJoueur()
                caseJoueur.setConnecte(False)
                caseJoueur = self.__map[yJoueur][xJoueur+1]
                caseJoueur.setJoueur(strIndiceJoueur)
                caseJoueur.setConnecte(True)
                self.__posJoueur[strIndiceJoueur] = caseJoueur
                moved = True
        elif direction == "up":
            if yJoueur - 1 >= 0:
                caseJoueur.removeJoueur()
                caseJoueur.setConnecte(False)
                caseJoueur = self.__map[yJoueur-1][xJoueur]
                caseJoueur.setJoueur(strIndiceJoueur)
                caseJoueur.setConnecte(True)
                self.__posJoueur[strIndiceJoueur] = caseJoueur
                moved = True
        elif direction == "down":
            if yJoueur + 1 < self.__tailleMap[0]:
                caseJoueur.removeJoueur()
                caseJoueur.setConnecte(False)
                caseJoueur = self.__map[yJoueur+1][xJoueur]
                caseJoueur.setJoueur(strIndiceJoueur)
                caseJoueur.setConnecte(True)
                self.__posJoueur[strIndiceJoueur] = caseJoueur
                moved = True
        #if moved:
        #    if caseJoueur.getTypeTile() == "+"
        #        joueur.getEquipe().soigneTous()

    def sendMessage(self,message,encoding,clientAddrr):
        senderAddr = clientAddrr[0] + "/" + str(clientAddrr[1]) + " "
        response = "rival message " + senderAddr + message + "\n"
        for i in self.__joueurs:
            self.__mutexSend.acquire()
            conn = i.getClientConn()
            conn.send(response.encode(encoding))
            self.__mutexSend.release()

    def removeJoueur(self,indiceJoueur):
        self.__joueurs[indiceJoueur] = None
        self.indiceJoueur.push(indiceJoueur)
        caseJoueur = self.__posJoueur[str(indiceJoueur)]
        caseJoueur.setConnecte(False)
        self.__nbJoueurs -= 1
        return self.__nbJoueurs