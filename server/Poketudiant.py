DELIMITER = " "

class Poketudiant:

    def __init__(self,variete,typeP,maxPV,attaque,defense,attaque1,attaque2):
        self.__variete = variete
        self.__typeP = typeP
        self.__maxPV = maxPV
        self.__currentPV = maxPV
        self.__attaque = attaque
        self.__defense = defense
        self.__attaque1 = attaque1
        self.__attaque2 = attaque2
        self.__niveau = 1
        self.__exp = 0

    def getVariete(self):
        return self.__variete

    def getType(self):
        return self.typeP

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

    def getEXPToNextLevel(self):
        expToNextLevel = 0
        for niveau in range(1,self.__niveau):
            expToNextLevel += 500 * ((1 + niveau) / 2)
        return  (expToNextLevel - self.__exp)

    def getInfo(self):
        return self.__variete + DELIMITER + self.__typeP.getNomType() + DELIMITER + self.__niveau + DELIMITER + str(self.getEXPToNextLevel()) + DELIMITER + str(self.__currentPV) + DELIMITER + str(self.__maxPV) + DELIMITER + str(self.__attaque) + DELIMITER + str(self.__defense) + DELIMITER + self.__attaque1.getInfo() + DELIMITER + self.__attaque2.getInfo() + "\n"

    def getCombatInfo(self):
        return self.__variete + DELIMITER + self.__niveau + DELIMITER + str(self.getCurrentPV()) + DELIMITER + self.__attaque1.getInfo() + DELIMITER + self.__attaque2.getInfo() + "\n"