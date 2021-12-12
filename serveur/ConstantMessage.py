# Enumération des différents messages du protocole entre client et serveur

ConstantMessage = {
    "CLIENT_SEARCH" : "looking for poketudiant servers",
    "SERVER_SEARCH_RESPONSE" : "i'm a poketudiant server\n",

    "CLIENT_RGAMELIST" : "require game list",
    "CLIENT_RGAMECREA" : "create game",
    "CLIENT_RGAMEJOIN" : "join game",


    "SERVER_GAMECREA_SUCC" : "game created\n",
    "SERVER_GAMECREA_ERR" : "cannot create game\n",

    "CLIENT_GAMEJOIN_SUCC" : "game joined\n",
    "CLIENT_GAMEJOIN_ERR" : "cannot join game\n",

    "CLIENT_PLAYERMOVE" : "map move",

    "CLIENT_PLAYERSENDMSG" : "send message",
    "SERVER_SENDMSG" : "rival message ",

    "SERVER_SENDTEAM" : "team contains ",


    "CLIENT_RLEAVE" : "encounter action leave",
    "CLIENT_RATTACK" : "encounter action attack",
    "CLIENT_RCATCH" : "encounter action catch",
    "CLIENT_RSWITCH" : "encounter action switch",
    "CLIENT_INDEX" : "encounter poketudiant index",

    "SERVER_NEWRIVAL" : "encounter new rival ",
    "SERVER_NEWWILD" : "encounter new wild 1\n",
    "SERVER_POKEPLAYERINFO" : "encounter poketudiant player ",
    "SERVER_POKEOPPOINFO" : "encounter poketudiant opponent ",
    "SERVER_POKEXPINFO" : "encounter poketudiant xp ",
    "SERVER_POKEEVOINFO" : "encounter poketudiant evolution ",
    "SERVER_POKELVLINFO" : "encounter poketudiant level ",
    "SERVER_RACTION" : "encounter enter action\n",
    "SERVER_WINMSG" : "encounter win\n",
    "SERVER_LOSEMSG" : "encounter lose\n",
    "SERVER_KOPLAYER" : "encounter KO player\n",
    "SERVER_KOOPPO" : "encounter KO opponent\n",
    "SERVER_LEAVEOK" : "encounter escape ok\n",
    "SERVER_LEAVEFAIL" : "encounter escape fail\n",
    "SERVER_CATCHOK" : "encounter catch ok\n",
    "SERVER_CATCHFAIL" : "encounter catch fail\n",
    "SERVER_RINDEXSWITCH" : "encounter enter poketudiant index\n",
    "SERVER_ERRORINDEX" : "encounter invalid poketudiant index\n"
    
}