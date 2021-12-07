import CommandType,ConstantMessage,Partie

ENCODING = "utf-8"

class Client:

    def __init__(self,clientConn,serverConn,address):
        self.__address = address
        self.__clientConn = clientConn
        self.__serverConn = serverConn
        self.__commandType = CommandType.CommandType["CONNECT"]
        self.__partie = None
        self.__ind = -1

    def deconnexion(self):
        if self.__partie != None and self.__ind != -1:
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

    def handleConnectCommand(self,data,listeParties):
        if data == ConstantMessage.ConstantMessage["CLIENT_RGAMELIST"]:
            response = "number of games " + str(len(listeParties)) + "\n"
            self.__clientConn.send(response.encode(ENCODING))
            response = ""
            for i in listeParties:
                response += str(i.getNbJoueurs())  + " " +  i.getNomPartie()+ "\n"
            self.__clientConn.send(response.encode(ENCODING)) 
            return
            
        elif ConstantMessage.ConstantMessage["CLIENT_RGAMECREA"] in data:
            tmp = data.split()
            if len(tmp) >= 3:   
                gameName = ""
                for i in range(2,len(tmp)):
                    gameName += tmp[i] + " "
                listeParties.append(Partie.Partie(gameName))
                self.__clientConn.send(ConstantMessage.ConstantMessage["SERVER_GAMECREA_SUCC"].encode(ENCODING))
                return
            else:
                self.__clientConn.send(ConstantMessage.ConstantMessage["SERVER_GAMECREA_ERR"].encode(ENCODING))
                return
        elif ConstantMessage.ConstantMessage["CLIENT_RGAMEJOIN"] in data:
            
            print("Joining")
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
                    return 
                else:
                    self.__clientConn.send(ConstantMessage.ConstantMessage["CLIENT_GAMEJOIN_ERR"].encode(ENCODING))
                    return

        self.__clientConn.send("No answer to that command".encode(ENCODING))
        return

    def handleGameCommand(self,data):
        if ConstantMessage.ConstantMessage["CLIENT_PLAYERMOVE"] in data:
            data = data.split()
            self.__partie.movePlayer(self.__ind,data[2])
            self.__partie.sendMapToAll()
        elif ConstantMessage.ConstantMessage["CLIENT_PLAYERSENDMSG"] in data:
            tmp = data.split()
            if len(tmp) >= 3:
                msg = ""
                for i in range(2,len(tmp)):
                    msg += tmp[i] + " "
                self.__partie.sendMessage(msg.strip(),ENCODING,self.__address)
        return

    def handleBattleCommand(self,data):
        self.__clientConn.send("No answer to that command".encode(ENCODING))
        return
