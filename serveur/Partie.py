import _thread,TileMap,Pile,Partie,Equipe,Poketudiant,random,Combat,Init,ConstantMessage

NB_JOUEURS = 4
ENCODING = "UTF-8"

# Classe qui s'occupe de la gestion d'une partie
class Partie:

    def __init__(self,nom):
        self.__nom = nom
        self.__joueurs = [None,None,None,None]
        self.__equipeJoueurs = [None,None,None,None]
        # Permet à un joueur rejoignant avec un indice déjà utilisé de revenir au départ comme donné par la carte
        self.__startJoueur = [None,None,None,None]
        self.__nbJoueurs = 0
        self.indiceJoueur = Pile.Pile()
        self.__posJoueur = {}
        self.__map = []
        self.__mutexSend = _thread.allocate_lock()
        self.__mutexMap = _thread.allocate_lock()
        
        # Création de la carte
        with open("World.map", "r") as f:
            for l in f:
                ligne = []
                for case in l:
                    if case in {"0","1","2","3"}:
                        c = TileMap.TileMap(" ",True)
                        ligne.append(c)
                        self.__posJoueur[case] = c
                        self.__startJoueur[int(case)] = c
                    else:
                        ligne.append(TileMap.TileMap(case,False))
                self.__map.append(list(ligne))
        self.__tailleMap = (len(self.__map),len(self.__map[0]))

    def getNomPartie(self):
        return self.__nom

    def getNbJoueurs(self):
        return self.__nbJoueurs
    
    # Méthode qui envoie la carte a un joueur
    def sendMap(self,joueur):
        clientConn = joueur.getClientConn()
        indiceJoueur = str(joueur.getIndiceJoueur())
        indiceRivaux = 1
        response = "map " + str(self.__tailleMap[0]) + " " + str(self.__tailleMap[1] - 1) + "\n"
        clientConn.send(response.encode(ENCODING))
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
        clientConn.send(response.encode(ENCODING))
    
    # Méthode qui envoie la carte à tout le joueurs connectés et hors d'un combat
    def sendMapToAll(self):
        self.__mutexSend.acquire()
        for i in self.__joueurs:
            if i != None and not i.estEnCombat():
                self.sendMap(i)
        self.__mutexSend.release()

    # Méthode qui permet à un joueur de rejoindre la partie si il peut
    def rejoindrePartie(self,joueur):
        if not self.indiceJoueur.estVide():
            self.__mutexMap.acquire()
            indiceJoueur = self.indiceJoueur.pop()
            spawn = self.__startJoueur[indiceJoueur]
            moved = False
            
            # Si un joueur se situe sur la case ou un joueur doit rejoindre on le bouge
            if spawn.contientJoueur():
                for i in ["up","right","down","left"]:
                    if self.deplaceJoueurSpawn(self.__joueurs[int(spawn.getIndiceJoueur())],i):
                        moved = True
                        break
                if not moved:
                    return -1,False
    
            self.__posJoueur[str(indiceJoueur)] = spawn
            self.__posJoueur[str(indiceJoueur)].setJoueur(str(indiceJoueur))            
            self.__posJoueur[str(indiceJoueur)].setConnecte(True)
            self.__joueurs[indiceJoueur] = joueur

            # On lui crée une équipe avec un Enseignant-dresseur
            self.__equipeJoueurs[indiceJoueur] = Equipe.Equipe()

            self.__equipeJoueurs[indiceJoueur].ajoutePoketudiant(Poketudiant.Poketudiant("Enseignant-dresseur"))
            self.__nbJoueurs += 1
            self.__mutexMap.release()
            return (indiceJoueur),True
        return -1,False

    # Méthode qui envoie l'équipe à son propriétaire
    def sendEquipe(self,joueur):
        self.__mutexSend.acquire()
        clientConn = joueur.getClientConn()
        indiceJoueur = joueur.getIndiceJoueur()
        response = ConstantMessage.ConstantMessage["SERVER_SENDTEAM"] + str(self.__equipeJoueurs[indiceJoueur].getTailleEquipe()) + "\n"
        clientConn.send(response.encode(ENCODING))
        clientConn.send(self.__equipeJoueurs[indiceJoueur].getEquipeInfo().encode(ENCODING))
        self.__mutexSend.release()

    # Méthode trouve la case où se situe le joueur et renvoie ces coordonnées
    def findTileAvecJoueur(self,indiceJoueur):
        for lig in range(self.__tailleMap[0]):
            for col in range(self.__tailleMap[1]):
                if self.__map[lig][col].getIndiceJoueur() != "":
                    pass
                if self.__map[lig][col].contientJoueur() and self.__map[lig][col].getIndiceJoueur() == indiceJoueur:
                    return lig,col

    # Méthode qui déplace un joueur si il se trouve sur la case d'un joueurs qui rejoins
    def deplaceJoueurSpawn(self,joueur,direction):
        
        indiceJoueur = joueur.getIndiceJoueur()
        strIndiceJoueur = str(indiceJoueur)
        yJoueur, xJoueur = self.findTileAvecJoueur(strIndiceJoueur)
        caseJoueur = self.__posJoueur[strIndiceJoueur]
        moved = False

        if direction == "left":
            if xJoueur - 1 >= 0 and not self.__map[yJoueur][xJoueur-1].contientJoueur():
                caseJoueur.removeJoueur()
                caseJoueur.setConnecte(False)
                caseJoueur = self.__map[yJoueur][xJoueur-1]
                caseJoueur.setJoueur(strIndiceJoueur)
                caseJoueur.setConnecte(True)
                self.__posJoueur[strIndiceJoueur] = caseJoueur
                moved = True

        elif direction == "right":
            if xJoueur + 1 < self.__tailleMap[1] and not self.__map[yJoueur][xJoueur+1].contientJoueur():
                caseJoueur.removeJoueur()
                caseJoueur.setConnecte(False)
                caseJoueur = self.__map[yJoueur][xJoueur+1]
                caseJoueur.setJoueur(strIndiceJoueur)
                caseJoueur.setConnecte(True)
                self.__posJoueur[strIndiceJoueur] = caseJoueur
                moved = True

        elif direction == "up":
            if yJoueur - 1 >= 0 and not self.__map[yJoueur-1][xJoueur].contientJoueur():
                caseJoueur.removeJoueur()
                caseJoueur.setConnecte(False)
                caseJoueur = self.__map[yJoueur-1][xJoueur]
                caseJoueur.setJoueur(strIndiceJoueur)
                caseJoueur.setConnecte(True)
                self.__posJoueur[strIndiceJoueur] = caseJoueur
                moved = True

        elif direction == "down":
            if yJoueur + 1 < self.__tailleMap[0] and not self.__map[yJoueur+1][xJoueur].contientJoueur():
                caseJoueur.removeJoueur()
                caseJoueur.setConnecte(False)
                caseJoueur = self.__map[yJoueur+1][xJoueur]
                caseJoueur.setJoueur(strIndiceJoueur)
                caseJoueur.setConnecte(True)
                self.__posJoueur[strIndiceJoueur] = caseJoueur
                moved = True

        if moved:
            if caseJoueur.getTypeTile() == "+":
                self.__equipeJoueurs[indiceJoueur].soigneTous()
                self.sendEquipe(joueur)
            
        return moved

    # Méthode qui renvoie l'équipe à son propriétaire
    def getEquipeJoueur(self,indiceJoueur):
        if indiceJoueur >= 0 and indiceJoueur < NB_JOUEURS and self.__equipeJoueurs[indiceJoueur] != None:
            return self.__equipeJoueurs[indiceJoueur]

    # Méthode qui renvoie les informations sur les poketudiants qui vont s'affronter aux joueurs
    def sendInfoPokeActuel(self,joueur1,joueur2):
        indiceJoueur = joueur1.getIndiceJoueur()
        indiceRival = joueur2.getIndiceJoueur()
        response = ConstantMessage.ConstantMessage["SERVER_NEWRIVAL"] + str(self.__equipeJoueurs[indiceRival].getTailleEquipe()) +"\n"
        joueur1.getClientConn().send(response.encode(ENCODING))
        response = ConstantMessage.ConstantMessage["SERVER_POKEPLAYERINFO"] + self.__equipeJoueurs[indiceJoueur].getCombatInfo(self.__equipeJoueurs[indiceJoueur].getIndicePremierVivant())
        joueur1.getClientConn().send(response.encode(ENCODING))
        response = ConstantMessage.ConstantMessage["SERVER_SENDTEAM"] + str(self.__equipeJoueurs[indiceJoueur].getTailleEquipe()) + "\n"
        joueur1.getClientConn().send(response.encode(ENCODING))
        response = self.__equipeJoueurs[indiceJoueur].getEquipeInfo()
        joueur1.getClientConn().send(response.encode(ENCODING))
        response = ConstantMessage.ConstantMessage["SERVER_POKEOPPOINFO"] + self.__equipeJoueurs[indiceRival].getPoketudiant(self.__equipeJoueurs[indiceRival].getIndicePremierVivant()).getCombatInfoAdv()
        joueur1.getClientConn().send(response.encode(ENCODING))

    # Méthode qui gère le mouvement des joueurs
    def movePlayer(self,joueur,direction):
        self.__mutexMap.acquire()
        indiceJoueur = joueur.getIndiceJoueur()
        strIndiceJoueur = str(indiceJoueur)
        yJoueur, xJoueur = self.findTileAvecJoueur(strIndiceJoueur)
        caseJoueur = self.__posJoueur[strIndiceJoueur]
        moved = False
        if direction == "left":
            if xJoueur - 1 >= 0:
                if not self.__map[yJoueur][xJoueur-1].contientJoueur():
                    caseJoueur.removeJoueur()
                    caseJoueur.setConnecte(False)
                    caseJoueur = self.__map[yJoueur][xJoueur-1]
                    caseJoueur.setJoueur(strIndiceJoueur)
                    caseJoueur.setConnecte(True)
                    self.__posJoueur[strIndiceJoueur] = caseJoueur
                    moved = True

                # Le rival et le joueur ont encore des poketudiants vivants et ne sont pas en combat
                elif self.__map[yJoueur][xJoueur-1].contientJoueur() and not self.__joueurs[int(self.__map[yJoueur][xJoueur-1].getIndiceJoueur())].estEnCombat() and self.__equipeJoueurs[indiceJoueur].nbPokeVivant() > 0 and self.__equipeJoueurs[int(self.__map[yJoueur][xJoueur-1].getIndiceJoueur())].nbPokeVivant() > 0:
                    self.__mutexSend.acquire()
                    rival = self.__joueurs[int(self.__map[yJoueur][xJoueur-1].getIndiceJoueur())]
                    self.sendInfoPokeActuel(joueur,rival)
                    self.sendInfoPokeActuel(rival,joueur)

                    combat = Combat.Combat(self,joueur,self.__equipeJoueurs[indiceJoueur].getIndicePremierVivant(),None,self.__joueurs[rival.getIndiceJoueur()],self.__equipeJoueurs[rival.getIndiceJoueur()].getIndicePremierVivant())
                    combat.run()

                    rival.setCommandType("BATTLE")
                    rival.entreEnCombat(combat)
                    self.__mutexSend.release()
                    self.__mutexMap.release()
                    return combat

        elif direction == "right":
            if xJoueur + 1 < self.__tailleMap[1]:
                if not self.__map[yJoueur][xJoueur+1].contientJoueur():
                    caseJoueur.removeJoueur()
                    caseJoueur.setConnecte(False)
                    caseJoueur = self.__map[yJoueur][xJoueur+1]
                    caseJoueur.setJoueur(strIndiceJoueur)
                    caseJoueur.setConnecte(True)
                    self.__posJoueur[strIndiceJoueur] = caseJoueur
                    moved = True

                # Le rival et le joueur ont encore des poketudiants vivants et ne sont pas en combat
                elif self.__map[yJoueur][xJoueur+1].contientJoueur() and not self.__joueurs[int(self.__map[yJoueur][xJoueur+1].getIndiceJoueur())].estEnCombat() and self.__equipeJoueurs[indiceJoueur].nbPokeVivant() > 0 and self.__equipeJoueurs[int(self.__map[yJoueur][xJoueur+1].getIndiceJoueur())].nbPokeVivant() > 0:
                    self.__mutexSend.acquire()
                    rival = self.__joueurs[int(self.__map[yJoueur][xJoueur+1].getIndiceJoueur())]
                    self.sendInfoPokeActuel(joueur,rival)
                    self.sendInfoPokeActuel(rival,joueur)

                    combat = Combat.Combat(self,joueur,self.__equipeJoueurs[indiceJoueur].getIndicePremierVivant(),None,self.__joueurs[rival.getIndiceJoueur()],self.__equipeJoueurs[rival.getIndiceJoueur()].getIndicePremierVivant())
                    combat.run()

                    rival.setCommandType("BATTLE")
                    rival.entreEnCombat(combat)
                    self.__mutexSend.release()
                    self.__mutexMap.release()
                    return combat

        elif direction == "up":
            if yJoueur - 1 >= 0:
                if not self.__map[yJoueur-1][xJoueur].contientJoueur():
                    caseJoueur.removeJoueur()
                    caseJoueur.setConnecte(False)
                    caseJoueur = self.__map[yJoueur-1][xJoueur]
                    caseJoueur.setJoueur(strIndiceJoueur)
                    caseJoueur.setConnecte(True)
                    self.__posJoueur[strIndiceJoueur] = caseJoueur
                    moved = True

                # Le rival et le joueur ont encore des poketudiants vivants et ne sont pas en combat
                elif self.__map[yJoueur-1][xJoueur].contientJoueur() and not self.__joueurs[int(self.__map[yJoueur-1][xJoueur].getIndiceJoueur())].estEnCombat() and self.__equipeJoueurs[indiceJoueur].nbPokeVivant() > 0 and self.__equipeJoueurs[int(self.__map[yJoueur-1][xJoueur].getIndiceJoueur())].nbPokeVivant() > 0:
                    self.__mutexSend.acquire()
                    rival = self.__joueurs[int(self.__map[yJoueur-1][xJoueur].getIndiceJoueur())]
                    self.sendInfoPokeActuel(joueur,rival)
                    self.sendInfoPokeActuel(rival,joueur)

                    combat = Combat.Combat(self,joueur,self.__equipeJoueurs[indiceJoueur].getIndicePremierVivant(),None,self.__joueurs[rival.getIndiceJoueur()],self.__equipeJoueurs[rival.getIndiceJoueur()].getIndicePremierVivant())
                    combat.run()
                    
                    rival.setCommandType("BATTLE")
                    rival.entreEnCombat(combat)
                    self.__mutexSend.release()
                    self.__mutexMap.release()
                    return combat
        elif direction == "down":
            if yJoueur + 1 < self.__tailleMap[0]:
                if not self.__map[yJoueur+1][xJoueur].contientJoueur():
                    caseJoueur.removeJoueur()
                    caseJoueur.setConnecte(False)
                    caseJoueur = self.__map[yJoueur+1][xJoueur]
                    caseJoueur.setJoueur(strIndiceJoueur)
                    caseJoueur.setConnecte(True)
                    self.__posJoueur[strIndiceJoueur] = caseJoueur
                    moved = True
                    # Le rival et le joueur ont encore des poketudiants vivants et ne sont pas en combat
                elif self.__map[yJoueur+1][xJoueur].contientJoueur() and not self.__joueurs[int(self.__map[yJoueur+1][xJoueur].getIndiceJoueur())].estEnCombat() and self.__equipeJoueurs[indiceJoueur].nbPokeVivant() > 0 and self.__equipeJoueurs[int(self.__map[yJoueur+1][xJoueur].getIndiceJoueur())].nbPokeVivant() > 0:
                    self.__mutexSend.acquire()
                    rival = self.__joueurs[int(self.__map[yJoueur+1][xJoueur].getIndiceJoueur())]
                    self.sendInfoPokeActuel(joueur,rival)
                    self.sendInfoPokeActuel(rival,joueur)

                    combat = Combat.Combat(self,joueur,self.__equipeJoueurs[indiceJoueur].getIndicePremierVivant(),None,self.__joueurs[rival.getIndiceJoueur()],self.__equipeJoueurs[rival.getIndiceJoueur()].getIndicePremierVivant())
                    combat.run()

                    rival.setCommandType("BATTLE")
                    rival.entreEnCombat(combat)
                    self.__mutexSend.release()
                    self.__mutexMap.release()
                    return combat
        # On envoie la carte à tout les joueurs même si aucun déplacement n'a été effectué
        self.sendMapToAll()
        # Si il y a eu mouvement on vérifie la case sur laquelle se trouve le joueur
        if moved:
            # Il faut soigner l'équipe du joueur
            if caseJoueur.getTypeTile() == "+":
                self.__equipeJoueurs[indiceJoueur].soigneTous()
                self.sendEquipe(joueur)

            # On regarde si le joueur déclenche un combat avec un poketudiant sauvage
            elif caseJoueur.getTypeTile() == "*" and self.__equipeJoueurs[indiceJoueur].nbPokeVivant() > 0:
                probaCombat = random.randrange(0,100)

                if probaCombat < 30:
                    # On choisit le poketudiant sauvage alétoirement parmis les poketudiants capturables
                    varietePoketudiant = Init.listeCapturable[random.randrange(0,len(Init.listeCapturable))]
                    wildPoketudiant = Poketudiant.Poketudiant(varietePoketudiant,random.randrange(1,4))
                    self.__mutexSend.acquire()

                    # Informations envoyées pour démarrer le combat avec le poketudiant sauvage
                    response = ConstantMessage.ConstantMessage["SERVER_NEWWILD"]
                    joueur.getClientConn().send(response.encode(ENCODING))
                    response = ConstantMessage.ConstantMessage["SERVER_POKEPLAYERINFO"] + self.__equipeJoueurs[indiceJoueur].getCombatInfo(self.__equipeJoueurs[indiceJoueur].getIndicePremierVivant())
                    joueur.getClientConn().send(response.encode(ENCODING))
                    response = ConstantMessage.ConstantMessage["SERVER_SENDTEAM"] + str(self.__equipeJoueurs[indiceJoueur].getTailleEquipe()) + "\n"
                    joueur.getClientConn().send(response.encode(ENCODING))
                    response = self.__equipeJoueurs[indiceJoueur].getEquipeInfo()
                    joueur.getClientConn().send(response.encode(ENCODING))
                    response = ConstantMessage.ConstantMessage["SERVER_POKEOPPOINFO"] + wildPoketudiant.getCombatInfoAdv()
                    joueur.getClientConn().send(response.encode(ENCODING))

                    combat = Combat.Combat(self,joueur,self.__equipeJoueurs[indiceJoueur].getIndicePremierVivant(),wildPoketudiant)
                    combat.run()

                    self.__mutexSend.release()
                    self.__mutexMap.release()
                    return combat
        self.__mutexMap.release()
        return None
        
    # Méthode qui gère l'envoi des messages des joueurs
    def sendMessage(self,message,encoding,clientAddrr):
        senderAddr = clientAddrr[0] + "/" + str(clientAddrr[1]) + " "
        response = ConstantMessage.ConstantMessage["SERVER_SENDMSG"] + senderAddr + message + "\n"
        for i in self.__joueurs:
            if i != None:
                self.__mutexSend.acquire()
                conn = i.getClientConn()
                conn.send(response.encode(encoding))
                self.__mutexSend.release()

    # Méthode qui gère la deconnexion d'un joueur à la partie
    def removeJoueur(self,indiceJoueur):
        self.__joueurs[indiceJoueur] = None
        self.__equipeJoueurs[indiceJoueur] = None
        self.indiceJoueur.push(indiceJoueur)
        caseJoueur = self.__posJoueur[str(indiceJoueur)]
        caseJoueur.setConnecte(False)
        caseJoueur.setJoueur("")
        self.__posJoueur[str(indiceJoueur)] = self.__startJoueur[indiceJoueur]
        self.__nbJoueurs -= 1
        self.sendMapToAll()
        return self.__nbJoueurs

    # Méthode qui demande la libération du poketudiant
    def liberePoke(self,joueur,indicePoke):
        indiceJoueur = joueur.getIndiceJoueur()
        self.__equipeJoueurs[indiceJoueur].supprimePoketudiant(int(indicePoke))
        self.sendEquipe(joueur)

    # Méthode qui demande le changement d'indice d'un poketudiant
    def movePoke(self,joueur,indicePoke,direction):
        indiceJoueur = joueur.getIndiceJoueur()
        self.__equipeJoueurs[indiceJoueur].movePoketudiant(int(indicePoke),direction)
        self.sendEquipe(joueur)
