import Poketudiant, _thread,random,time,ConstantMessage

ENCODING = "utf-8"

# Classe qui implémente la gestion des combats
class Combat:

    def __init__(self,partie,joueur1,indPJ1,poketudiantWild = None,joueur2 = None,indPJ2 = None):
        self.__partie = partie
        self.__joueur1 = joueur1
        self.__indPJ1 = indPJ1
        self.__participantJ1 = [0]
        # Définit le type de combat pour définir la gestion du combat
        if poketudiantWild == None:
            self.__typeCombat = "PVP"
        else:
            self.__typeCombat = "PVE"
        self.__poketudiantWild = poketudiantWild
        self.__joueur2 = joueur2
        self.__indPJ2 = indPJ2
        self.__participantJ2 = [0]
        self.__actions = []

    def getTypeCombat(self):
        return self.__typeCombat

    # Méthode qui gère la deconnexion d'un joueur en plein milieu d'un combat
    def deconnexion(self,joueur):
        if self.__typeCombat == "PVP":
            if joueur == self.__joueur1:
                self.__joueur2.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_WINMSG"].encode(ENCODING))
                self.__joueur2.setCommandType("GAME")
                self.__partie.sendMapToAll()
                self.__partie.sendEquipe(self.__joueur2)
                self.__joueur2.removeCombat()
            else:
                self.__joueur1.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_WINMSG"].encode(ENCODING))
                self.__joueur1.setCommandType("GAME")
                self.__partie.sendMapToAll()
                self.__partie.sendEquipe(self.__joueur1)
                self.__joueur1.removeCombat()

    # Méthode qui envoie une demande d'action au joueurs
    def run(self):
        self.__joueur1.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_RACTION"].encode(ENCODING))
        if self.__typeCombat == "PVP":
            self.__joueur2.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_RACTION"].encode(ENCODING))

    # Méthode qui envoie les informations sur le poketudiant actuellement en combat aux joueurs
    def sendPokeActuelInfo(self,joueur1,joueur2,poketudiant):
        reponse = ConstantMessage.ConstantMessage["SERVER_POKEOPPOINFO"] + poketudiant.getCombatInfoAdv()
        joueur1.getClientConn().send(reponse.encode(ENCODING))
        reponse = ConstantMessage.ConstantMessage["SERVER_POKEPLAYERINFO"] + poketudiant.getCombatInfo()
        joueur2.getClientConn().send(reponse.encode(ENCODING))

    # Méthode qui s'occupe de la gestion du combat
    def commande(self,commande,joueur,indice = -1):
        #
        #   Partie Combat contre Rival
        #
        if self.__typeCombat == "PVP":
            
            # Si un client a besoin de changer de poketudiant à cause de sa mort

            if commande == "changementMort":
                self.__actions.clear()
                self.changerPoketudiant(joueur,indice)
                if joueur == self.__joueur1:
                    reponse = ConstantMessage.ConstantMessage["SERVER_POKEPLAYERINFO"] + self.__partie.getEquipeJoueur(joueur.getIndiceJoueur()).getPoketudiant(self.__indPJ1).getCombatInfo()
                    reponseR = ConstantMessage.ConstantMessage["SERVER_POKEOPPOINFO"] + self.__partie.getEquipeJoueur(joueur.getIndiceJoueur()).getPoketudiant(self.__indPJ1).getCombatInfoAdv()
                    rival = self.__joueur2
                else:
                    reponse = ConstantMessage.ConstantMessage["SERVER_POKEPLAYERINFO"] + self.__partie.getEquipeJoueur(joueur.getIndiceJoueur()).getPoketudiant(self.__indPJ2).getCombatInfo()
                    reponseR = ConstantMessage.ConstantMessage["SERVER_POKEOPPOINFO"] + self.__partie.getEquipeJoueur(joueur.getIndiceJoueur()).getPoketudiant(self.__indPJ2).getCombatInfoAdv()
                    rival = self.__joueur1
                joueur.getClientConn().send(reponse.encode(ENCODING))
                rival.getClientConn().send(reponseR.encode(ENCODING))
                self.__partie.sendEquipe(joueur)
                return False
            else:

                # Ajout de la commande du joueur dans la liste des actions
                if joueur == self.__joueur1:
                    self.__actions.append((commande,self.__joueur1,indice))
                else:
                    self.__actions.append((commande,self.__joueur2,indice))

                # Signifie que les deux joueurs ont effectués une action
                if len(self.__actions) > 1:

                    # Si les deux joueurs attaquent
                    if self.__actions[0][0] == "attaque" and self.__actions[1][0] == "attaque":
                        firstToAttack = random.randrange(0,2)
                        j1 = self.__actions[firstToAttack][1]
                        actionJ1 = self.__actions[firstToAttack][2]

                        #Sélectionne l'indice de l'attaque 
                        if firstToAttack == 0:
                            j2 = self.__actions[1][1]
                            actionJ2 = self.__actions[1][2]
                        else:
                            j2 = self.__actions[0][1]
                            actionJ2 = self.__actions[0][2]
                        
                        if j1 == self.__joueur1:
                            indJ1 = self.__indPJ1
                            indJ2 = self.__indPJ2
                        else:
                            indJ1 = self.__indPJ2
                            indJ2 = self.__indPJ1
                        pokeJ1 = self.__partie.getEquipeJoueur(j1.getIndiceJoueur()).getPoketudiant(indJ1)
                        pokeJ2 = self.__partie.getEquipeJoueur(j2.getIndiceJoueur()).getPoketudiant(indJ2)
                        
                        self.attaque(pokeJ1,pokeJ2,actionJ1)
                        self.sendPokeActuelInfo(j1,j2,pokeJ2)
                        self.__partie.sendEquipe(j2)

                        # Vérifie que le poketudiant n'est pas mort suite à l'attaque
                        if pokeJ2.getCurrentPV() < 1:
                            j1.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_KOOPPO"].encode(ENCODING))
                            j2.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_KOPLAYER"].encode(ENCODING))

                            # Vérifie que le client à encore un poketudiant
                            if self.__partie.getEquipeJoueur(j2.getIndiceJoueur()).nbPokeVivant() == 0:
                                j1.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_WINMSG"].encode(ENCODING))
                                j2.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_LOSEMSG"].encode(ENCODING))

                                # Soigne et fait perdre l'XP aux poketudiant perdants
                                self.__partie.getEquipeJoueur(j2.getIndiceJoueur()).soigneTous()
                                equipe = self.__partie.getEquipeJoueur(j2.getIndiceJoueur())
                                for i in range(0,equipe.getTailleEquipe()):
                                    equipe.getPoketudiant(i).perdXP()

                                # Gère la fin de combat pour le joueurs ayant mis sa commande en premier
                                if joueur == self.__joueur1:
                                    self.__joueur2.setCommandType("GAME")
                                    self.__joueur2.removeCombat()
                                    self.__partie.sendMapToAll()
                                    self.__partie.sendEquipe(self.__joueur2)
                                    return True
                                else:
                                    self.__joueur1.setCommandType("GAME")
                                    self.__joueur1.removeCombat()
                                    self.__partie.sendMapToAll()
                                    self.__partie.sendEquipe(self.__joueur1)
                                    self.__joueur1.removeCombat()
                                    return True
                            else:
                                # Demande au joueur de sélectionner un nouveau poketudiant
                                j2.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_RINDEXSWITCH"].encode(ENCODING))
                                self.__partie.sendEquipe(j2)
                                j2.setCommandType("SWITCH_MORT")
                                return False
                        else:
                            self.attaque(pokeJ2,pokeJ1,actionJ2)
                            self.sendPokeActuelInfo(j2,j1,pokeJ1)
                            self.__partie.sendEquipe(j1)

                            # Vérifie que le poketudiant n'est pas mort suite à l'attaque
                            if pokeJ1.getCurrentPV() < 1:
                                j2.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_KOOPPO"].encode(ENCODING))
                                j1.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_KOPLAYER"].encode(ENCODING))

                                # Vérifie que le client à encore un poketudiant
                                if self.__partie.getEquipeJoueur(j1.getIndiceJoueur()).nbPokeVivant() == 0:
                                    j2.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_WINMSG"].encode(ENCODING))
                                    j1.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_LOSEMSG"].encode(ENCODING))

                                    # Soigne et fait perdre l'XP aux poketudiant perdants
                                    self.__partie.getEquipeJoueur(j1.getIndiceJoueur()).soigneTous()
                                    equipe = self.__partie.getEquipeJoueur(j1.getIndiceJoueur())
                                    for i in range(0,equipe.getTailleEquipe()):
                                        equipe.getPoketudiant(i).perdXP()

                                    # Gère la fin de combat pour le joueurs ayant mis sa commande en premier
                                    if joueur == self.__joueur1:
                                        self.__joueur2.setCommandType("GAME")
                                        self.__partie.sendMapToAll()
                                        self.__partie.sendEquipe(self.__joueur2)
                                        self.__joueur2.removeCombat()
                                        return True
                                    else:
                                        self.__joueur1.setCommandType("GAME")
                                        self.__partie.sendMapToAll()
                                        self.__partie.sendEquipe(self.__joueur1)
                                        self.__joueur1.removeCombat()
                                        return True
                                else:
                                    # Demande au joueur de sélectionner un nouveau poketudiant
                                    j1.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_RINDEXSWITCH"].encode(ENCODING))
                                    self.__partie.sendEquipe(j1)
                                    j1.setCommandType("SWITCH_MORT")
                                    return False
                            # Efface la liste des actions et continue le combat
                            self.__actions.clear()
                            self.run()
                            return False

                    # Si les deux joueurs ont demandés un changement de poketudiant
                    elif self.__actions[0][0] == "changement" and self.__actions[1][0] == "changement":

                        if self.__actions[0][1] == self.__joueur1:
                            indJ1 = 0
                            indJ2 = 1
                        else:
                            indJ1 = 1
                            indJ2 = 0

                        j1 = self.__actions[indJ1][1]
                        j2 = self.__actions[indJ2][1]

                        #Sélectionne l'indice du poketudiant

                        indPJ1 = self.__actions[indJ1][2]
                        indPJ2 = self.__actions[indJ2][2]

                        self.changerPoketudiant(j1,indPJ1)
                        self.changerPoketudiant(j2,indPJ2)

                        pokeJ1 = self.__partie.getEquipeJoueur(j1.getIndiceJoueur()).getPoketudiant(self.__indPJ1)
                        pokeJ2 = self.__partie.getEquipeJoueur(j2.getIndiceJoueur()).getPoketudiant(self.__indPJ2)

                        self.sendPokeActuelInfo(j1,j2,pokeJ2)
                        self.__partie.sendEquipe(j2)

                        self.sendPokeActuelInfo(j2,j1,pokeJ1)
                        self.__partie.sendEquipe(j1)

                        # Efface la liste des actions et relance le combat
                        self.__actions.clear()
                        self.run()
                        return False
                    # Si l'un des deux joueurs a demandé un changement et l'autre une attaque
                    elif (self.__actions[0][0] == "changement" and self.__actions[1][0] == "attaque") or (self.__actions[0][0] == "attaque" and self.__actions[1][0] == "changement"):
                        if self.__actions[0][1] == self.__joueur1 and self.__actions[0][0] == "changement":
                            indJ1 = 0
                            indJ2 = 1
                            j1 = self.__actions[indJ1][1]
                            j2 = self.__actions[indJ2][1]
                            
                            # Sélectionne l'indice de l'attaque et du poketudiant
                            indPJ1 = self.__actions[indJ1][2]
                            indPJ2 = self.__actions[indJ2][2]
                            self.changerPoketudiant(j1,indPJ1)


                            pokeJ1 = self.__partie.getEquipeJoueur(j1.getIndiceJoueur()).getPoketudiant(self.__indPJ1)
                            pokeJ2 = self.__partie.getEquipeJoueur(j2.getIndiceJoueur()).getPoketudiant(self.__indPJ2)

                            self.sendPokeActuelInfo(j2,j1,pokeJ1)
                            self.__partie.sendEquipe(j1)

                            self.attaque(pokeJ2,pokeJ1,indPJ2)

                            self.sendPokeActuelInfo(j2,j1,pokeJ1)
                            self.__partie.sendEquipe(j1)
                            
                            # Efface la liste des actions et relance le combat
                            self.__actions.clear()
                            self.run()
                        else:
                            indJ1 = 1
                            indJ2 = 0
                            j1 = self.__actions[indJ1][1]
                            j2 = self.__actions[indJ2][1]
                            # Sélectionne l'indice de l'attaque et du poketudiant
                            indPJ1 = self.__actions[indJ1][2]
                            indPJ2 = self.__actions[indJ2][2]

                            self.changerPoketudiant(j2,indPJ2)

                            pokeJ1 = self.__partie.getEquipeJoueur(j1.getIndiceJoueur()).getPoketudiant(self.__indPJ1)
                            pokeJ2 = self.__partie.getEquipeJoueur(j2.getIndiceJoueur()).getPoketudiant(self.__indPJ2)
                            self.sendPokeActuelInfo(j1,j2,pokeJ2)
                            self.__partie.sendEquipe(j2)

                            self.attaque(pokeJ1,pokeJ2,indPJ1)

                            self.sendPokeActuelInfo(j1,j2,pokeJ2)
                            self.__partie.sendEquipe(j2)

                            # Efface la liste des actions et relance le combat
                            self.__actions.clear()
                            self.run()
                else:       
                    return False
        #
        #   Partie Combat contre Poketudiant Sauvage
        #
        elif self.__typeCombat == "PVE":

            # Si le joueur veut s'enfuire
            if commande == "fuite":
                fuite = self.fuite(joueur.getIndiceJoueur())
                if fuite:
                    joueur.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_LEAVEOK"].encode(ENCODING))
                    return True
                else:
                    joueur.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_LEAVEFAIL"].encode(ENCODING))

            #Si le joueur veut attaquer
            elif commande == "attaque":
                if self.attaque(self.__partie.getEquipeJoueur(joueur.getIndiceJoueur()).getPoketudiant(self.__indPJ1),self.__poketudiantWild,indice):
                    # Si le poketudiant sauvage est KO, calcule l'xp que va recevoir chaque poketudiant et géré les montées en niveaux et/ou évolutions

                    reponse = ConstantMessage.ConstantMessage["SERVER_POKEOPPOINFO"] + self.__poketudiantWild.getCombatInfoAdv()
                    joueur.getClientConn().send(reponse.encode(ENCODING))
                    joueur.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_KOOPPO"].encode(ENCODING))

                    expDu = self.__poketudiantWild.calculXP()
                    nbPoke = len(self.__participantJ1)
                    expParPoke = expDu // nbPoke

                    # Distribue de l'exp à chaque poketudiant participant au combat
                    equipe = self.__partie.getEquipeJoueur(joueur.getIndiceJoueur())
                    for i in self.__participantJ1:
                        expNext = equipe.getPoketudiant(i).getEXPToNextLevel()
                        if equipe.getPoketudiant(i).getNiveau() < 10:
                            # Si le poketudiant monte en niveau suite au combat
                            if expNext <= expParPoke + equipe.getPoketudiant(i).getEXP():
                                varieteP = equipe.getPoketudiant(i)
                                reponse = ConstantMessage.ConstantMessage["SERVER_POKELVLINFO"] + str(i) + " 1 \n"
                                joueur.getClientConn().send(reponse.encode(ENCODING))

                                # Si le poketudiant évolue suite au combat
                                if equipe.getPoketudiant(i).prendNiveau():
                                    reponse = ConstantMessage.ConstantMessage["SERVER_POKEEVOINFO"] + str(i) + " " + equipe.getPoketudiant(i).getVariete() + "\n"
                                    joueur.getClientConn().send(reponse.encode(ENCODING))
                            equipe.getPoketudiant(i).giveXP(expParPoke)

                            reponse = ConstantMessage.ConstantMessage["SERVER_POKEXPINFO"] + str(i) + " " + str(expParPoke) + "\n"
                            joueur.getClientConn().send(reponse.encode(ENCODING))

                    joueur.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_WINMSG"].encode(ENCODING))
                    return True
                else:
                    reponse = ConstantMessage.ConstantMessage["SERVER_POKEOPPOINFO"] + self.__poketudiantWild.getCombatInfoAdv()
                    joueur.getClientConn().send(reponse.encode(ENCODING))
            
            # Si le joueur veut capture le poketudiant
            elif commande == "capture":
                capture = self.capture(joueur.getIndiceJoueur(),self.__poketudiantWild)
                if capture:
                    joueur.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_CATCHOK"].encode(ENCODING))
                    return True
                else:
                    joueur.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_CATCHFAIL"].encode(ENCODING))

            # Si le joueur veut changer de poketudiant
            elif commande == "changement":
                if self.changerPoketudiant(joueur,indice):
                    if indice not in self.__participantJ1:
                        self.__participantJ1.append(indice)
                    reponse = ConstantMessage.ConstantMessage["SERVER_POKEPLAYERINFO"] + self.__partie.getEquipeJoueur(joueur.getIndiceJoueur()).getPoketudiant(self.__indPJ1).getCombatInfo()
                    joueur.getClientConn().send(reponse.encode(ENCODING))
                    self.__partie.sendEquipe(joueur)
                else:
                    return

            # Si le joueur doit changer de poketudiant suite à la mort d'un poketudiant
            elif commande == "changementMort":
                if self.changerPoketudiant(joueur,indice):
                    if indice not in self.__participantJ1:
                        self.__participantJ1.append(indice)
                    reponse = ConstantMessage.ConstantMessage["SERVER_POKEPLAYERINFO"] + self.__partie.getEquipeJoueur(joueur.getIndiceJoueur()).getPoketudiant(self.__indPJ1).getCombatInfo()
                    joueur.getClientConn().send(reponse.encode(ENCODING))
                    self.__partie.sendEquipe(joueur)
                    return
                else:
                    return

            # Gestion de l'attaque du poketudiant sauvage sur le poketudiant du joueur
            self.attaque(self.__poketudiantWild,self.__partie.getEquipeJoueur(joueur.getIndiceJoueur()).getPoketudiant(self.__indPJ1),random.randrange(1,3))
            reponse = ConstantMessage.ConstantMessage["SERVER_POKEPLAYERINFO"] + self.__partie.getEquipeJoueur(joueur.getIndiceJoueur()).getPoketudiant(self.__indPJ1).getCombatInfo()
            joueur.getClientConn().send(reponse.encode(ENCODING))
            self.__partie.sendEquipe(joueur)

            # Vérifie que le poketudiant du joueur est encore en vie
            if self.__partie.getEquipeJoueur(joueur.getIndiceJoueur()).getPoketudiant(self.__indPJ1).getCurrentPV() < 1:
                joueur.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_KOPLAYER"].encode(ENCODING))

                # Vérifie si il reste un poketudiant au joueur
                if self.__partie.getEquipeJoueur(joueur.getIndiceJoueur()).nbPokeVivant() == 0:
                    joueur.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_LOSEMSG"].encode(ENCODING))
                    self.__partie.getEquipeJoueur(joueur.getIndiceJoueur()).soigneTous()
                    equipe = self.__partie.getEquipeJoueur(joueur.getIndiceJoueur())

                    # Enlève de l'exp à tout les poketudiants en cas de défaite
                    for i in self.__participantJ1:
                        equipe.getPoketudiant(i).perdXP()
                    return True
                else:

                    # Sinon on demande l'indice du prochain poketudiant
                    joueur.getClientConn().send(ConstantMessage.ConstantMessage["SERVER_RINDEXSWITCH"].encode(ENCODING))
                    self.__partie.sendEquipe(joueur)
                    joueur.setCommandType("SWITCH_MORT")
                    return False
            else:
                self.run()
            return False

    # Méthode qui effectue l'attaque demandée par un joueur
    def attaque(self,poketudiantA,poketudiantD,indice):
        modifiers = [0.9,1.0,1.1]
        coeffAtt = modifiers[random.randrange(0,3,1)]
        if indice == 1:
            attaqueChoisie = poketudiantA.getAttaque1()
        else:
            attaqueChoisie = poketudiantA.getAttaque2()
        puissance = attaqueChoisie.getPuissance()

        if attaqueChoisie.estEfficace(poketudiantD.getType().getTypeP()):
            puissance *= 2

        degats = int(coeffAtt * (poketudiantA.getAttaque() / poketudiantD.getDefense()) * puissance)
        poketudiantD.recoisDegat(degats)
        if poketudiantD.getCurrentPV() < 1:
            return True
        return False

    # Méthode qui effectue ou non la fuite demandée par un joueur
    def fuite(self,indiceJoueur):
        if self.__typeCombat == "PVE":
            probaFuite = random.randrange(0,100)
            if self.__poketudiantWild.getNiveau() - self.__partie.getEquipeJoueur(indiceJoueur).getPoketudiant(self.__indPJ1).getNiveau() == 0:
                if probaFuite < 50:
                    return True
                else:
                    return False
            elif self.__poketudiantWild.getNiveau() - self.__partie.getEquipeJoueur(indiceJoueur).getPoketudiant(self.__indPJ1).getNiveau() == 1:
                if probaFuite < 75:
                    return True
                else:
                    return False
            elif self.__poketudiantWild.getNiveau() - self.__partie.getEquipeJoueur(indiceJoueur).getPoketudiant(self.__indPJ1).getNiveau() == -1:
                if probaFuite < 40:
                    return True
                else:
                    return False
            elif self.__poketudiantWild.getNiveau() - self.__partie.getEquipeJoueur(indiceJoueur).getPoketudiant(self.__indPJ1).getNiveau() == 2:
                if probaFuite < 90:
                    return True
                else:
                    return False
            elif self.__poketudiantWild.getNiveau() - self.__partie.getEquipeJoueur(indiceJoueur).getPoketudiant(self.__indPJ1).getNiveau() == -2:
                if probaFuite < 25:
                    return True
                else:
                    return False
            elif self.__poketudiantWild.getNiveau() - self.__partie.getEquipeJoueur(indiceJoueur).getPoketudiant(self.__indPJ1).getNiveau() >= 3:
                return False
            elif self.__poketudiantWild.getNiveau() - self.__partie.getEquipeJoueur(indiceJoueur).getPoketudiant(self.__indPJ1).getNiveau() <= -3:
                return True

    # Méthode qui effectue ou non la cpature demandée par un joueur
    def capture(self,indiceJoueur,poketudiantWild):
        probaCapture = 2*max(1/2 - (poketudiantWild.getCurrentPV()/poketudiantWild.getMaxPV()),0) * 100
        if probaCapture <= random.randrange(0,99):
            self.__partie.getEquipeJoueur(indiceJoueur).ajoutePoketudiant(poketudiantWild)
            return True
        return False

    # Méthode qui effectue le changement de poketudiant demandé par un joueur
    def changerPoketudiant(self,joueur,indice):
        equipe = self.__partie.getEquipeJoueur(joueur.getIndiceJoueur())
        if indice >= 0 and indice < equipe.getTailleEquipe():
            if equipe.getPoketudiant(indice).getCurrentPV() == 0:
                return False
            else:
                if joueur == self.__joueur1:
                    self.__indPJ1 = indice
                else:
                    self.__indPJ2 = indice
                return True
        else:
            return False 

    # Méthode qui vérifie si un changement de poketudiant est possible
    def changementPossible(self,joueur,indice):
        equipe = self.__partie.getEquipeJoueur(joueur.getIndiceJoueur())
        if joueur == self.__joueur1:
            ind = self.__indPJ1
        else:
            ind = self.__indPJ2
        if ind != indice and indice >= 0 and indice < equipe.getTailleEquipe():
            if equipe.getPoketudiant(indice).getCurrentPV() == 0:
                return False
            else:
                return True
        else:
            return False 
