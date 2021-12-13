import random,Init,Attaque

DELIMITER = " "

#Modificateur de statistiques d'un poketudiant
modifiers = [0.9,1.0,1.1]

# Classe qui implémente un poketudiant
class Poketudiant:

    def __init__(self,variete,niveau = 1):
        self.__variete = variete
        self.__typeP = Init.listeInfoPoketudiants[variete]["type"]
        self.__coeffAtt = modifiers[random.randrange(0,3,1)]
        self.__coeffDef = modifiers[random.randrange(0,3,1)]
        self.__coeffPV = modifiers[random.randrange(0,3,1)]
        self.__maxPV = int(Init.listeInfoPoketudiants[variete]["PV"] * self.__coeffPV)
        self.__attaque = int(Init.listeInfoPoketudiants[variete]["Attaque"] * self.__coeffAtt)
        self.__defense = int(Init.listeInfoPoketudiants[variete]["Defense"] * self.__coeffDef)
        self.__attaque1 = Attaque.choixAttaqueType(self.__typeP.getTypeP())
        self.__attaque2 = Attaque.choixAttaqueAutreType(self.__typeP.getTypeP())
        self.__niveau = niveau
        if self.__niveau != 1:
            for i in range(1,self.__niveau):
                self.__attaque = int(self.__attaque * 1.1)
                self.__defense = int(self.__defense * 1.1)
                self.__maxPV = int(self.__maxPV * 1.1)
        self.__currentPV = self.__maxPV
        self.__exp = 0
        for i in range(1,self.__niveau):
            self.__exp += int(500 * ((1 + i) / 2))

    def getVariete(self):
        return self.__variete

    def getType(self):
        return self.__typeP

    def getMaxPV(self):
        return self.__maxPV

    def getCurrentPV(self):
        return self.__currentPV

    def getPercentPV(self):
        return int(self.__currentPV / self.__maxPV * 100)

    def getAttaque(self):
        return self.__attaque

    def getDefense(self):
        return self.__defense

    def getAttaque1(self):
        return self.__attaque1

    def getAttaque2(self):
        return self.__attaque2

    def getNiveau(self):
        return self.__niveau

    def getEXP(self):
        return self.__exp

    # Méthode qui fait subir des dégâts à un poketudiant
    def recoisDegat(self,degats):
        if self.__currentPV - degats < 0:
            self.__currentPV = 0
        else:
            self.__currentPV -= degats

    # Méthode qui calcule l'expérience nécessaire pour arriver au prochain niveau
    def getEXPToNextLevel(self):
        expToNextLevel = 0
        for i in range(1,self.__niveau+1):
            expToNextLevel += 500 * ((1 + i) / 2)
        return  (int(expToNextLevel))

    # Méthode qui formate les informations qu'un poketudiant pour l'envoyer au client
    def getInfo(self):
        return self.__variete + DELIMITER + self.__typeP.getTypeP() + DELIMITER + str(self.__niveau) + DELIMITER + str(self.__exp) + DELIMITER + str(self.getEXPToNextLevel()) + DELIMITER + str(self.__currentPV) + DELIMITER + str(self.__maxPV) + DELIMITER + str(self.__attaque) + DELIMITER + str(self.__defense) + DELIMITER + self.__attaque1.getInfo() + DELIMITER + self.__attaque2.getInfo() + "\n"

    # Méthode qui formate les informations qu'un poketudiant pour l'envoyer au client dans un combat
    def getCombatInfo(self):
        return self.__variete + DELIMITER + str(self.__niveau) + DELIMITER + str(self.getPercentPV()) + DELIMITER + self.__attaque1.getInfo() + DELIMITER + self.__attaque2.getInfo() + "\n"

    # Méthode qui formate les informations qu'un poketudiant pour l'envoyer au client advers lors d'un combat
    def getCombatInfoAdv(self):
        return self.__variete + DELIMITER + str(self.__niveau) + DELIMITER + str(self.getPercentPV()) + "\n"

    # Méthode qui soigne un poketudiant
    def soigne(self):
        self.__currentPV = self.__maxPV

    # Méthode qui calcule l'expérience qu'un poketudiant rapporte quand il est tué
    def calculXP(self):
        return int(self.__exp * 0.1)

    # Méthode qui ajoute de l'expérience à un poketudiant
    def giveXP(self,montant):
        self.__exp += montant

    # Méthode qui augment d'un niveau un poketudiant
    def prendNiveau(self):
        self.__niveau += 1
        self.__attaque = int(self.__attaque * 1.1)
        self.__defense = int(self.__defense * 1.1)
        self.__maxPV = int(self.__maxPV * 1.1)
        self.soigne()
        if self.__niveau >= 3 and Init.listeInfoPoketudiants[self.__variete]["Evolution"] != "":
            return self.evolution()
        return False

    # Méthode qui vérifie si un poketudiant évolue ou pas
    def evolution(self):
        probaEvo = random.randrange(0,100)
        if self.__niveau == 3 and probaEvo < 20:
            self.__variete = Init.listeInfoPoketudiants[self.__variete]["Evolution"]
            self.recalculeStat()
            return True
        elif self.__niveau == 4 and probaEvo < 38:
            self.__variete = Init.listeInfoPoketudiants[self.__variete]["Evolution"]
            self.recalculeStat()
            return True
        elif self.__niveau >= 5:
            self.__variete = Init.listeInfoPoketudiants[self.__variete]["Evolution"]
            self.recalculeStat()
            return True
        return False

    # Méthode qui affecte un montant d'expérience à un poketudiant
    def setExp(self,montant):
        self.__exp = montant

    # Méthode qui recalcule les statitstiques d'un poketudiant suite à une évolution
    def recalculeStat(self):
        self.__attaque = int(Init.listeInfoPoketudiants[variete]["Attaque"] * self.__coeffAtt)
        self.__defense = int(Init.listeInfoPoketudiants[variete]["Defense"] * self.__coeffDef)
        self.__maxPV = int(Init.listeInfoPoketudiants[variete]["MaxPV"] * self.__coeffPV)
        for i in range(1,self.__niveau):
            self.__attaque = int(self.__attaque * 1.1)
            self.__defense = int(self.__defense * 1.1)
            self.__maxPV = int(self.__maxPV * 1.1)

    # Méthode qui fait perdre de l'expérience ( voir un niveau ) à un poketudiant suite à une défaite
    def perdXP(self):
        if self.__exp > 0:
            self.__exp = int(self.__exp * 0.8)
            expToLastLevel = 0
            for i in range(1,self.__niveau):
                expToLastLevel += 500 * ((1 + i) / 2)
            if self.__exp < expToLastLevel:
                self.__niveau -= 1
                self.__attaque = int(self.__attaque * 0.9)
                self.__defense = int(self.__defense * 0.9)
                self.__maxPV = int(self.__maxPV * 0.9)
        
