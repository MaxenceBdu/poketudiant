class Attaque:

    def __init__(self,nomAttaque,typeAttaque,puissance):
        self.__nomAttaque = nomAttaque
        self.__typeAttaque = typeAttaque
        self.__puissance = puissance

    def getNomAttaque(self):
        return self.__nomAttaque

    def getTypeAttaque(self):
        return self.__typeAttaque

    def getPuissance(self):
        return self.__puissance

    def getInfo(self):
        return self.__nomAttaque + " " + self.__typeAttaque.getNomType()