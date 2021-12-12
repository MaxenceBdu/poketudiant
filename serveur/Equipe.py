import Poketudiant

EQUIPE_TAILLE_MAX = 3

# Classe qui implémente l'équipe de poketudiant
class Equipe:

    def __init__(self):
        self.__equipe = [None,None,None]
        self.__taille = 0

    def getTailleEquipe(self):
        return self.__taille

    def getEquipe(self):
        return self.__equipe

    # Méthode qui formate les informations des poketudiants de l'équipe pour l'envoi au client
    def getCombatInfo(self,indice):
        if indice >= 0 and indice < self.__taille:
            return self.__equipe[indice].getCombatInfo()

    # Méthode qui formate les informations du poketudiant courant pour l'envoi au client
    def getCombatInfoAdv(self,indice):
        if indice >= 0 and indice < self.__taille:
            return self.__equipe[indice].getCombatInfoAdv()

    # Méthode qui formate les informations du poketudiant pour l'envoi au client adverse
    def getEquipeInfo(self):
        infos = ""
        for poketudiant in self.__equipe:
            if poketudiant != None:
                infos += poketudiant.getInfo()
        return infos

    # Méthode qui ajoute un pokétudiant à l'équipe
    def ajoutePoketudiant(self,poketudiant):
        if self.__taille < EQUIPE_TAILLE_MAX:
            self.__equipe[self.__taille] = poketudiant
            self.__taille += 1

    # Méthode qui supprime un poketudiant de l'équipe
    def supprimePoketudiant(self,indPoketudiant):
        if self.getTailleEquipe() > 1 and self.__equipe[indPoketudiant] != None:
            for i in range(indPoketudiant,self.__taille-1):
                self.__equipe[i] = self.__equipe[i+1]
            self.__equipe[self.__taille-1] = None
            self.__taille -= 1

    # Méthode qui soigne tout les poketudiants de l'équipe
    def soigneTous(self):
        for poketudiant in self.__equipe:
            if poketudiant != None:
                poketudiant.soigne()

    # Méthode qui gère le changemement de position d'un poketudiant dans l'équipe
    def movePoketudiant(self,indice,direct):
        tmp = self.__equipe[indice]
        
        if direct == "up" and indice - 1 >= 0:
            self.__equipe[indice] = self.__equipe[indice-1]
            self.__equipe[indice-1] = tmp
        elif direct == "down" and indice + 1 < self.__taille:
            self.__equipe[indice] = self.__equipe[indice+1]
            self.__equipe[indice+1] = tmp

    # Méthode qui retourne le poketudiant à l'indice dans l'équipe
    def getPoketudiant(self,indice):
        if indice >= 0 and indice < self.__taille:
            return self.__equipe[indice]

    # Méthode qui renvoie le nombre de poketudiants encore vivant dans l'équipe
    def nbPokeVivant(self):
        nbPokeVivant = 0
        for i in self.__equipe:
            if i != None and i.getCurrentPV() >= 1:
                nbPokeVivant += 1
        return nbPokeVivant

    # Méthode qui renvoie l'indice du premier poketudiant vivant
    def getIndicePremierVivant(self):
        for i in range(0,self.__taille):
            if self.__equipe[i] != None and self.__equipe[i].getCurrentPV() >= 1:
                return i