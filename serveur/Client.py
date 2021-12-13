import CommandType,ConstantMessage,Partie

ENCODING = "utf-8"

# Fonction pour savoir si un nom de partie est correcte ou non
def nomPartieCorrecte(nom):
    for i in nom:
        if not i.isalnum() and i != " ":
            return False
    return True

# Classe qui implémente la gestion du client
class Client:

    def __init__(self,clientConn,serverConn,address):
        self.__address = address
        self.__clientConn = clientConn
        self.__serverConn = serverConn
        self.__commandType = CommandType.CommandType["CONNECT"]
        self.__partie = None
        self.__ind = -1
        self.__combat = None

    # Méthode que gère la deconnexion d'un client
    def deconnexion(self):
        if self.__partie != None and self.__ind != -1:
            if self.__combat != None:
                self.__combat.deconnexion(self)
            return self.__partie.removeJoueur(self.__ind)

    def getClientConn(self):
        return self.__clientConn

    def getServerConn(self):
        return self.__serverConn
    
    def getCommandType(self):
        return self.__commandType

    def getAddress(self):
        return self.__address

    def getIndiceJoueur(self):
        return self.__ind

    def getNomPartie(self):
        return self.__partie.getNomPartie()

    # Méthode qui vérifie si la partie existe ou pas 
    def partieExisteDeja(self,nom,listeParties):
        for i in listeParties:
            if nom == i.getNomPartie():
                return True
        return False
    
    # Méthode qui permet à un Client d'entre dans un combat avec un rival quand il n'en est pas la source
    def entreEnCombat(self,combat):
        self.__combat = combat

    # Méthode qui permet de savoir si un client est en Combat ou non
    def estEnCombat(self):
        return self.__combat != None

    # Méthode qui enlève le combat d'un client (Après une victoire ou une défaite)
    def removeCombat(self):
        self.__combat = None

    # Méthode qui gère toutes les commandes liées à la connexion à une partie
    def handleConnectCommand(self,data,listeParties,nbMaxParties):
        if data == ConstantMessage.ConstantMessage["CLIENT_RGAMELIST"]:
            response = "number of games " + str(len(listeParties)) + "\n"
            self.__clientConn.send(response.encode(ENCODING))
            response = ""
            for i in listeParties:
                response += str(i.getNbJoueurs())  + " " +  i.getNomPartie()+ "\n"
            self.__clientConn.send(response.encode(ENCODING)) 
            return
            
        elif ConstantMessage.ConstantMessage["CLIENT_RGAMECREA"] in data:
            if len(listeParties) >= nbMaxParties:
                self.__clientConn.send(ConstantMessage.ConstantMessage["CLIENT_GAMEJOIN_ERR"].encode(ENCODING))
                return
            tmp = data.split()
            if len(tmp) >= 3:   
                gameName = ""
                for i in range(2,len(tmp)):
                    gameName += tmp[i] + " "
                    
                if nomPartieCorrecte(gameName) and not self.partieExisteDeja(gameName,listeParties):
                    listeParties.append(Partie.Partie(gameName))
                    self.__clientConn.send(ConstantMessage.ConstantMessage["SERVER_GAMECREA_SUCC"].encode(ENCODING))
                    ind, join = listeParties[len(listeParties)-1].rejoindrePartie(self)
                    if join:
                        self.__commandType = CommandType.CommandType["GAME"]
                        self.__partie = listeParties[len(listeParties)-1]
                        self.__ind = ind
                        self.__clientConn.send(ConstantMessage.ConstantMessage["CLIENT_GAMEJOIN_SUCC"].encode(ENCODING))
                        self.__partie.sendMapToAll()
                        self.__partie.sendEquipe(self)
                        return 
                    else:
                        self.__clientConn.send(ConstantMessage.ConstantMessage["CLIENT_GAMEJOIN_ERR"].encode(ENCODING))
                        return
                else:
                    self.__clientConn.send(ConstantMessage.ConstantMessage["SERVER_GAMECREA_ERR"].encode(ENCODING))
                    return
            else:
                self.__clientConn.send(ConstantMessage.ConstantMessage["SERVER_GAMECREA_ERR"].encode(ENCODING))
                return
        elif ConstantMessage.ConstantMessage["CLIENT_RGAMEJOIN"] in data:
            tmp = data.split()
            join = False
            if len(tmp) >= 3:
                gameName = ""
                for i in range(2,len(tmp)):
                    gameName += tmp[i] + " "
                
                for i in listeParties:
                    if i.getNomPartie() == gameName:
                        ind, join = i.rejoindrePartie(self)
                        if join:
                            self.__commandType = CommandType.CommandType["GAME"]
                            self.__partie = i
                            self.__ind = ind
                            break
                if join:
                    self.__clientConn.send(ConstantMessage.ConstantMessage["CLIENT_GAMEJOIN_SUCC"].encode(ENCODING))
                    self.__partie.sendMapToAll()
                    self.__partie.sendEquipe(self)
                    return 
                else:
                    self.__clientConn.send(ConstantMessage.ConstantMessage["CLIENT_GAMEJOIN_ERR"].encode(ENCODING))
                    return
        return

    # Méthode qui gère toutes les commandes liées à la partie
    def handleGameCommand(self,data):
        if ConstantMessage.ConstantMessage["CLIENT_PLAYERMOVE"] in data:
            data = data.split()
            combat = self.__partie.movePlayer(self,data[2])
            if combat != None:
                self.__commandType = CommandType.CommandType["BATTLE"]
                self.__combat = combat
            return
        elif ConstantMessage.ConstantMessage["CLIENT_PLAYERSENDMSG"] in data:
            tmp = data.split()
            if len(tmp) >= 3:
                msg = ""
                for i in range(2,len(tmp)):
                    msg += tmp[i] + " "
                self.__partie.sendMessage(msg.strip(),ENCODING,self.__address)
            return
        elif "poketudiant" in data:
            if "free" in data:
                data = data.split()
                self.__partie.liberePoke(self,data[1])
            elif "move" in data:
                data = data.split()
                self.__partie.movePoke(self,data[1],data[3])
        return

    # Méthode qui gère toutes les commandes liées au combats
    def handleBattleCommand(self,data):
        if ConstantMessage.ConstantMessage["CLIENT_RLEAVE"] in data:
            if self.__combat.commande("fuite",self):
                self.__commandType = CommandType.CommandType["GAME"]
                self.__combat = None
                self.__partie.sendMapToAll()
                self.__partie.sendEquipe(self)
                e
        elif ConstantMessage.ConstantMessage["CLIENT_RATTACK"] in data:
            if "attack1" in data:
                indice = 1
            else:
                indice = 2
            if self.__combat.commande("attaque",self,indice):
                self.__commandType = CommandType.CommandType["GAME"]
                self.__combat = None
                self.__partie.sendMapToAll()
                self.__partie.sendEquipe(self)
                
                return
        elif ConstantMessage.ConstantMessage["CLIENT_RCATCH"] in data:
            if self.__combat.commande("capture",self):
                self.__commandType = CommandType.CommandType["GAME"]
                self.__combat = None
                self.__partie.sendMapToAll()
                self.__partie.sendEquipe(self)
                
                return
        elif ConstantMessage.ConstantMessage["CLIENT_RSWITCH"] in data:
                self.__commandType = CommandType.CommandType["SWITCH"]
                self.__clientConn.send(ConstantMessage.ConstantMessage["SERVER_RINDEXSWITCH"].encode(ENCODING))
                self.__partie.sendEquipe(self)
                return
        return
        
    # Méthode qui gère toutes les commandes liées au changement dans un combat
    def handleSwitchCommand(self,data):
        if ConstantMessage.ConstantMessage["CLIENT_INDEX"] in data:
            data = data.split()
            if len(data) > 3:
                if self.__combat.changementPossible(self,int(data[3])):
                    self.__combat.commande("changement",self,int(data[3]))
                    self.__commandType = CommandType.CommandType["BATTLE"]
                    return
                else:
                    self.__clientConn.send(ConstantMessage.ConstantMessage["SERVER_ERRORINDEX"].encode(ENCODING))
                    self.__clientConn.send(ConstantMessage.ConstantMessage["SERVER_RINDEXSWITCH"].encode(ENCODING))
                    self.__partie.sendEquipe(self)
            else:
                self.__clientConn.send(ConstantMessage.ConstantMessage["SERVER_ERRORINDEX"].encode(ENCODING))
                self.__clientConn.send(ConstantMessage.ConstantMessage["SERVER_RINDEXSWITCH"].encode(ENCODING))
                self.__partie.sendEquipe(self)
    
    # Méthode qui gère toutes les commandes liées au changement dans un combat suite à la mort d'un poketudiant
    def handleSwitchMortCommand(self,data):
        if ConstantMessage.ConstantMessage["CLIENT_INDEX"] in data:
            data = data.split()
            if len(data) > 3:
                if self.__combat.changementPossible(self,int(data[3])):
                    self.__combat.commande("changementMort",self,int(data[3]))
                    self.__commandType = CommandType.CommandType["BATTLE"]
                    self.__combat.run()
                    return
                else:
                    self.__clientConn.send(ConstantMessage.ConstantMessage["SERVER_ERRORINDEX"].encode(ENCODING))
                    self.__clientConn.send(ConstantMessage.ConstantMessage["SERVER_RINDEXSWITCH"].encode(ENCODING))
                    self.__partie.sendEquipe(self)
            else:
                self.__clientConn.send(ConstantMessage.ConstantMessage["SERVER_ERRORINDEX"].encode(ENCODING))
                self.__clientConn.send(ConstantMessage.ConstantMessage["SERVER_RINDEXSWITCH"].encode(ENCODING))
                self.__partie.sendEquipe(self)

    # Méthode qui définit le type de commande que le client va recevoir ensuite
    def setCommandType(self,commandType):
        self.__commandType = CommandType.CommandType[commandType]
