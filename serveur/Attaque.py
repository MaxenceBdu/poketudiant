import Init,random

# Choix aléatoire d'une attaque d'un typeP
def choixAttaqueType(typeP):
    return Init.listeAttaque[typeP][random.randrange(0,len(Init.listeAttaque[typeP]))]

# Choix d'une attaque aléatoire dans les types restants
def choixAttaqueAutreType(typeP):
    listeAttaques = []
    for i in Init.listeAttaque:
        if i != typeP and i != "Teacher":
            listeAttaques.append(Init.listeAttaque[i])
    typeA = random.randrange(len(listeAttaques))
    return listeAttaques[typeA][random.randrange(0,len(listeAttaques[typeA]))]

# Classe qui implémente une attaque
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

    # Méthode qui formate les informations de l'attaque pour pouvoir l'envoyer au client
    def getInfo(self):
        return self.__nomAttaque + " " + self.__typeAttaque.getTypeP()

    # Méthode qui permet de savoir si une attaque est efficace sur un certains type
    def estEfficace(self,typeP):
        if self.__typeAttaque.getTypeP() == "Teacher" and typeP != "Teacher":
            return True
        elif self.__typeAttaque.getTypeP() == "Noisy" and typeP == "Lazy":
            return True
        elif self.__typeAttaque.getTypeP() == "Lazys" and typeP == "Motivated":
            return True
        elif self.__typeAttaque.getTypeP() == "Motivated" and typeP == "Noisy":
            return True
        return False
