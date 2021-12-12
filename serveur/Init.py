import json,TypePoketudiant,Attaque,random

#
# Module qui gère l'initialisation des ressources nécessaires au jeu (Poketudiant,Attaques,Types)
#

listeType = []
listeVariete = []
listeInfoPoketudiants = {}
listeCapturable = []
listeAttaque = {}

with open("type.json", "r") as f:
    data = json.load(f)
    for types in data["type"]:
        listeType.append(TypePoketudiant.TypePoketudiant(types))
        listeAttaque[listeType[len(listeType) - 1].getTypeP()] = []

with open("poketudiant.json","r") as f:
    data = json.load(f)
    for poketudiant in data:
        listeVariete.append(poketudiant)
        typeP = None
        for i in listeType:
            if data[poketudiant]["type"] == i.getTypeP():
                typeP = i
        listeInfoPoketudiants[poketudiant] = {"type" : typeP, "Attaque" : data[poketudiant]["Attaque"], "Defense" : data[poketudiant]["Defense"], "PV" : data[poketudiant]["PV"],"Capturable" : data[poketudiant]["Capturable"],"Evolution" : data[poketudiant]["Evolution"]}
        if data[poketudiant]["Capturable"] == 1:
            listeCapturable.append(poketudiant)

with open("attaque.json", "r") as f:
    data = json.load(f)
    for attaque in data:
        typeA = None
        for i in listeType:
            if data[attaque]["type"] == i.getTypeP():
                typeA = i
                break
        listeAttaque[data[attaque]["type"]].append(Attaque.Attaque(attaque,typeA,data[attaque]["Puissance"])) 

