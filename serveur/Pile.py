# Classe qui implémente une Pile
class Pile:

    def __init__(self):
        self.pile = [3,2,1,0]
        self.taille = 4
    
    def estVide(self):
        return self.taille == 0

    def pop(self):
        if not self.estVide():
            self.taille -= 1
            return self.pile[self.taille]
        return None

    def push(self,valeur):
        if self.taille + 1 < 4:
            self.pile[self.taille] = valeur
            self.taille += 1