class TileMap:

    def __init__(self,typeTile,indiceJoueur = ""):
        self.__typeTile = typeTile
        self.__indiceJoueur = indiceJoueur
        self.__estConnecte = False

    def estConnecte(self):
        return self.__estConnecte

    def setConnecte(self,valeur):
        self.__estConnecte = valeur

    def contientJoueur(self):
        return (self.estConnecte() and not (self.__indiceJoueur == ""))

    def setJoueur(self,indiceJoueur):
        self.__indiceJoueur = indiceJoueur

    def removeJoueur(self):
        self.__indiceJoueur = ""

    def getIndiceJoueur(self):
        return self.__indiceJoueur

    def getTypeTile(self):
        return self.__typeTile