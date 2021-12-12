# Classe qui implémente une case de la carte
class TileMap:

    def __init__(self,typeTile,estDepart):
        self.__typeTile = typeTile
        self.__indiceJoueur = ""
        self.__estConnecte = False
        self.__estDepart = estDepart

    def estConnecte(self):
        return self.__estConnecte

    def estDepart(self):
        return self.__estDepart

    # Méthode qui permet changer l'état d'une case
    def setConnecte(self,valeur):
        self.__estConnecte = valeur

    # Méthode qui renvoie si un joueur connecté est sur cette case
    def contientJoueur(self):
        return (self.estConnecte() and not (self.__indiceJoueur == ""))

    # Méthode pour ajouter un joueur à une case
    def setJoueur(self,indiceJoueur):
        self.__indiceJoueur = indiceJoueur

    # Méthode pour retirer un joueur à une case#
    def removeJoueur(self):
        self.__indiceJoueur = ""

    def getIndiceJoueur(self):
        return self.__indiceJoueur

    def getTypeTile(self):
        return self.__typeTile