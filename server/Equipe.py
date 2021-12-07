import Poketudiant

EQUIPE_TAILLE_MAX = 3

class Equipe:

    def __init__():
        self.__equipe = []

    def getTailleEquipe(self):
        return len(self.__equipe)

    def getEquipe(self):
        return self.__equipe

    def getEquipeInfo(self):
        infos = ""
        for poketudiant in self.__equipe:
            infos += poketudiant.getInfo()
        return infos

    def ajoutePoketudiant(self,poketudiant):
        if len(self.__equipe) < EQUIPE_TAILLE_MAX:
            self.__equipe.append(poketudiant)

    def supprimePoketudiant(self,indPoketudiant):
        self.__equipe.pop(indPoketudiant)

    def soigneTous(self):
        for poketudiant in equipe:
            poketudiant.soigne():

    def movePokemon(self,indice,direct):
        tmp = self.__equipe[indice]
        
        if direct == "up" and indice - 1 >= 0:
            self.__equipe[indice] = self.__equipe[indice-1]
            self.__equipe[indice-1] = tmp
        elif direct == "down" and indice + 1 < EQUIPE_TAILLE_MAX:
            self.__equipe[indice] = self.__equipe[indice+1]
            self.__equipe[indice+1] = tmp
