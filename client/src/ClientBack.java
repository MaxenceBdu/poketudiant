import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/*
    Maybe the most important class of the client
    Builds front elements before sending them to the panels
    And sends udp/tcp messages
    It's a singleton
 */
public class ClientBack implements ConstantMessages {
    final int UDP_PORT = 9000;
    final int TCP_PORT = 9001;

    private DatagramSocket udpSocket;

    private Socket tcpSocket;
    private BufferedReader socketReader;
    private PrintStream socketPrinter;

    private static ClientBack clientBackInstance;
    private int spritesSize;

    private ClientDaemon clientDaemon;

    private ClientBack(){}

    public static ClientBack getInstance(){
        if(clientBackInstance == null){
            clientBackInstance = new ClientBack();
        }

        return clientBackInstance;
    }

    public void closeTCPCommunication(){
        try {
            tcpSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientDaemon.setCanRun(false);
    }

    public void startDaemon(){
        if(clientDaemon == null) {
            clientDaemon = new ClientDaemon(socketReader);
            clientDaemon.start();
        }else {
            clientDaemon.setCanRun(true);
            clientDaemon.setSocketReader(socketReader);
        }
    }

    public BufferedReader getSocketReader() {
        return socketReader;
    }

    /*
        Sends the 'looking for poketudiant servers' message
        and stores the responses
     */
    protected ArrayList<InetAddress> askForServers() {
        if(udpSocket == null){
            try {
                udpSocket = new DatagramSocket();
                udpSocket.setBroadcast(true);
                udpSocket.setSoTimeout(1000);
            }catch(SocketException e){
                e.printStackTrace();
                return null;
            }
        }

        byte[] buf1 = LOOKING_FOR_SERVERS.getBytes();
        byte[] buf2 = new byte[50];
        try {
            ArrayList<InetAddress> serverList = new ArrayList<>();
            InetAddress address = InetAddress.getByName("255.255.255.255");

            DatagramPacket send = new DatagramPacket(buf1, buf1.length, address, UDP_PORT);
            DatagramPacket receive = new DatagramPacket(buf2, buf2.length);

            try{
                udpSocket.send(send);
            }catch(IOException e){
                e.printStackTrace();
            }

            try{
                while (true) {
                    udpSocket.receive(receive);
                    String realResponse = new String(receive.getData(), 0, receive.getLength());
                    if (realResponse.equals(SERVER_FOUND)) {
                        serverList.add(receive.getAddress());
                    }
                }
            }catch(SocketTimeoutException e){
                return serverList;
            }
            catch(IOException e){
                e.printStackTrace();
                return null;
            }
        }catch(UnknownHostException e){
            e.printStackTrace();
            return null;
        }
    }

    /*
        Send the 'require game list' message
        and store the list of game
     */
    public ArrayList<GameListItem> askForGameList(InetAddress serverAddress){
        ArrayList<GameListItem> games = new ArrayList<>();
        try{
            if (tcpSocket != null) {
                tcpSocket.close();
            }

            tcpSocket = new Socket(serverAddress, TCP_PORT);
            tcpSocket.setSoTimeout(5000);

            socketReader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            socketPrinter = new PrintStream(tcpSocket.getOutputStream());

            socketPrinter.println(ASK_GAME_LIST);
            String response = socketReader.readLine();

            String[] tab = response.split(" ");
            int nbGames = Integer.parseInt(tab[tab.length-1]);
            if(nbGames > 0){
                for(int i = 0; i < nbGames; i++){
                    String[] game = socketReader.readLine().split(" ");
                    games.add(new GameListItem(game[1], Integer.parseInt(game[0])));
                }
            }

            return games;
        }catch(IOException e){
            e.printStackTrace();
            return games;
        }
    }

    /*
        Send the 'create game name_of_the_game' message
        with game's name as parameter
     */
    public boolean askForGameCreation(String gameName){

        try{
            socketReader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            socketPrinter = new PrintStream(tcpSocket.getOutputStream());

            socketPrinter.println(CREATE_GAME+gameName);
            String response = socketReader.readLine();
            return response.equals(GAME_CREATED);
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    /*
        Send the 'join game name_of_the_game' message
        with game's name as parameter
     */
    public void askForGameJoin(String game){
        try{
            socketReader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            socketPrinter = new PrintStream(tcpSocket.getOutputStream());

            socketPrinter.println(JOIN_GAME+game);
            String response = socketReader.readLine();

            if(response.equals(JOIN_SUCCESS)){
                DisplayWindow.getInstance().goToGame();
                new ClientDaemon(ClientBack.getInstance().getSocketReader()).start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /*
        Finds the position of the player in the map
        and then keeps only the cells around it to make a 15x15 square
     */
    public void generateMap(int lines, int columns, List<String> map){
        boolean found = false;
        int xplayer =0, yplayer = 0, xlimit1, xlimit2, ylimit1, ylimit2;

        // Find player's position
        for(int i = 0; i < lines; i++){
            String[] split = map.get(i).split("");
            for(int j = 0; j < columns; j++){
                if(split[j].equals("0")) {
                    xplayer = j;
                    yplayer = i;
                    found = true;
                    break;
                }
            }
            if(found){
                break;
            }
        }

        // Modify limits according the player's postion and map's borders
        if(xplayer < 7) {
            xlimit1 = 0;
            xlimit2 = 15;
        }else if(xplayer >= columns-8){
            xlimit1 = columns - 15;
            xlimit2 = columns;
        }else{
            xlimit1 = xplayer-7;
            xlimit2 = xplayer+8;
        }

        if(yplayer < 7) {
            ylimit1 = 0;
            ylimit2 = 15;
        }else if(yplayer >= lines-8){
            ylimit1 = lines - 15;
            ylimit2 = lines;
        }else{
            ylimit1 = yplayer-7;
            ylimit2 = yplayer+8;
        }

        List<JLabel> cells = new ArrayList<>(15*15);
        int size = DisplayWindow.getInstance().getGamePanel().getMainPanelSize()/15;
        for(int i = ylimit1; i < ylimit2; i++){
            String[] split = map.get(i).split("");
            for(int j = xlimit1; j < xlimit2; j++){
                switch (split[j]) {
                    case "0":
                        cells.add(CellGenerator.generateCell(CellGenerator.CellType.PLAYER0, size));
                        break;
                    case "1":
                        cells.add(CellGenerator.generateCell(CellGenerator.CellType.PLAYER1, size));
                        break;
                    case "2":
                        cells.add(CellGenerator.generateCell(CellGenerator.CellType.PLAYER2, size));
                        break;
                    case "3":
                        cells.add(CellGenerator.generateCell(CellGenerator.CellType.PLAYER3, size));
                        break;
                    case "+":
                        cells.add(CellGenerator.generateCell(CellGenerator.CellType.HEAL, size));
                        break;
                    case "*":
                        cells.add(CellGenerator.generateCell(CellGenerator.CellType.GRASS, size));
                        break;
                    default:
                        cells.add(CellGenerator.generateCell(CellGenerator.CellType.NEUTRAL, size));
                        break;
                }
            }
        }
        DisplayWindow.getInstance().displayGame(cells);
    } // generateMap

    /*
        Functions to send player's movement
     */
    public void playerMoveUp(){
        sendPlayerAction(MOVE_UP);
    }

    public void playerMoveDown(){
        sendPlayerAction(MOVE_DOWN);
    }

    public void playerMoveLeft(){
        sendPlayerAction(MOVE_LEFT);
    }

    public void playerMoveRight(){
        sendPlayerAction(MOVE_RIGHT);
    }

    public void sendPlayerAction(String move){
        socketPrinter.println(move);
    }

    /*
        To change the id of a poketudiant in the team
     */
    public void sendPoketudiantMove(int id, String direction){
        socketPrinter.println("poketudiant "+id+" move "+direction);
    }

    public void sendPoketudiantFree(int id){
        socketPrinter.println("poketudiant "+id+" free");
    }

    /*
        Function to send player's actions during the fight
     */
    public void sendPlayerActionFight(String action){
        socketPrinter.println(ConstantMessages.PLAYER_ACTION+action);
        if(!action.equals("switch"))
            DisplayWindow.getInstance().getGamePanel().getFightPanel().enableActionButtons(false);
    }

    /*
        Gets sprites for all poketudiants of the team
        and sends them to the front (only for the sidebar here)
     */
    public void generateTeamDisplay(List<String> team){
        List<TeamItem> pokeTeam = new ArrayList<>();
        spritesSize = (int) (DisplayWindow.getInstance().getGamePanel().getxOffset()*0.6);
        int cpt = 0;
        for(String poke : team){
            String[] infosPoke = poke.split(" ");
            String lvl = infosPoke[2];
            String currentXp = infosPoke[3];
            String xpNextLvl = infosPoke[4];
            String currentPv = infosPoke[5];
            String maxPv = infosPoke[6];
            switch (infosPoke[0].toUpperCase()){
                case "PARLFOR":
                    pokeTeam.add(new TeamItem(SpriteManager.generatePoketudiant(PoketudiantSpriteSource.PARLFOR, spritesSize), cpt, lvl, currentXp, xpNextLvl, currentPv, maxPv));
                    break;
                case "ISMAR":
                    pokeTeam.add(new TeamItem(SpriteManager.generatePoketudiant(PoketudiantSpriteSource.ISMAR, spritesSize), cpt, lvl, currentXp, xpNextLvl, currentPv, maxPv));
                    break;
                case "RIGOLAMOR":
                    pokeTeam.add(new TeamItem(SpriteManager.generatePoketudiant(PoketudiantSpriteSource.RIGOLAMOR, spritesSize), cpt, lvl, currentXp, xpNextLvl, currentPv, maxPv));
                    break;
                case "PROCRASTINO":
                    pokeTeam.add(new TeamItem(SpriteManager.generatePoketudiant(PoketudiantSpriteSource.PROCRASTINO, spritesSize),cpt, lvl, currentXp, xpNextLvl, currentPv, maxPv));
                    break;
                case "COUCHTAR":
                    pokeTeam.add(new TeamItem(SpriteManager.generatePoketudiant(PoketudiantSpriteSource.COUCHTAR, spritesSize), cpt, lvl, currentXp, xpNextLvl, currentPv, maxPv));
                    break;
                case "NUIDEBOU":
                    pokeTeam.add(new TeamItem(SpriteManager.generatePoketudiant(PoketudiantSpriteSource.NUIDEBOU, spritesSize),cpt, lvl, currentXp, xpNextLvl, currentPv, maxPv));
                    break;
                case "ALABOURRE":
                    pokeTeam.add(new TeamItem(SpriteManager.generatePoketudiant(PoketudiantSpriteSource.ALABOURRE, spritesSize),cpt, lvl, currentXp, xpNextLvl, currentPv, maxPv));
                    break;
                case "BUCHAFON":
                    pokeTeam.add(new TeamItem(SpriteManager.generatePoketudiant(PoketudiantSpriteSource.BUCHAFON, spritesSize),cpt, lvl, currentXp, xpNextLvl, currentPv, maxPv));
                    break;
                case "BELMENTION":
                    pokeTeam.add(new TeamItem(SpriteManager.generatePoketudiant(PoketudiantSpriteSource.BELMENTION, spritesSize),cpt, lvl, currentXp, xpNextLvl, currentPv, maxPv));
                    break;
                case "PROMOMAJOR":
                    pokeTeam.add(new TeamItem(SpriteManager.generatePoketudiant(PoketudiantSpriteSource.PROMOMAJOR, spritesSize),cpt, lvl, currentXp, xpNextLvl, currentPv, maxPv));
                    break;
                case "ENSEIGNANT-DRESSEUR":
                    pokeTeam.add(new TeamItem(SpriteManager.generatePoketudiant(PoketudiantSpriteSource.ENSEIGNANT, spritesSize),cpt, lvl, currentXp, xpNextLvl, currentPv, maxPv));
                    break;
                default:
                    break;
            }
            cpt++;
        }
        DisplayWindow.getInstance().displayTeam(pokeTeam);
    } // generateTeamDisplay


    /*
        Manages messages about fights excepted the 'encounter enter poketudiant index' message
     */
    public void interpretFightMessage(String message){
        if(!message.isBlank()){
            String[] split = message.split(" ");
            if(split[1].equals("new")){
                // create fight panel or set visible if already created
                DisplayWindow.getInstance().displayFight(split[2].equals("wild"));

            }else if(split[1].equals("poketudiant")){
                if(split[2].equals("player") || split[2].equals("opponent")){
                    ImageIcon sprite;
                    String pv=split[5], lvl=split[4], realVariety = split[3], myVariety = split[3].toUpperCase();
                    if(realVariety.equals("Enseignant-dresseur")){
                        myVariety = "ENSEIGNANT";
                    }

                    if(split[2].equals("player")){
                        String attack1 = split[6] + " / " + split[7];
                        String attack2 = split[8] + " / " + split[9];
                        //System.out.println(attack1+" "+attack2);
                        // send to front the sprite of poketudiant looking to opponent
                        sprite = SpriteManager.generatePoketudiant(PoketudiantSpriteSource.valueOf(myVariety+"_DOS"), spritesSize);
                        DisplayWindow.getInstance().getGamePanel().getFightPanel().displayPlayerPoketudiant(sprite, realVariety, pv, lvl, attack1, attack2);
                    }else{
                        // send to front the sprite of poketudiant looking to player
                        sprite = SpriteManager.generatePoketudiant(PoketudiantSpriteSource.valueOf(myVariety), spritesSize);
                        DisplayWindow.getInstance().getGamePanel().getFightPanel().displayOpponentPoketudiant(sprite, realVariety, pv, lvl);
                    }
                }else if(split[2].equals("xp")){
                    // Notify player the amount of xp earned
                    DisplayWindow.getInstance().getGamePanel().xpNotification(split[3], split[4]);
                }else if(split[2].equals("lvl")){
                    // Notify the up of level
                    DisplayWindow.getInstance().getGamePanel().lvlUpNotification(split[3], split[4]);
                }else{
                    // Notify evolution
                    DisplayWindow.getInstance().getGamePanel().evolutionNotification(split[3], split[4]);
                }
            } else if(split[1].equals("enter") && split[2].equals("action")) {
                DisplayWindow.getInstance().getGamePanel().getFightPanel().enableActionButtons(true);
            }else if(split[1].equals("catch") && split[2].equals("ok")){
                DisplayWindow.getInstance().getGamePanel().backToMap(0, false);
            }else if(split[1].equals("win") || split[1].equals("lose")){
                DisplayWindow.getInstance().getGamePanel().backToMap(2, split[1].equals("win"));
            }else if(split[1].equals("escape")) {
                if(split[2].equals("ok"))
                    DisplayWindow.getInstance().getGamePanel().backToMap(1, split[1].equals("win"));
                else
                    DisplayWindow.getInstance().getGamePanel().failEscapeNotification();
            }else if(split[1].equals("forbidden")){
                DisplayWindow.getInstance().getGamePanel().forbiddenNotification();
            }else if(split[1].equals("KO")){
                DisplayWindow.getInstance().getGamePanel().koNotification(split[2].equals("player"));
            }
        }
    } // interpretFightMessage

    /*
        Send the 'encounter poketudiant index X' message
        where X is the String parameter
     */
    public void sendSwitchPoketudiant(String s) {
        socketPrinter.println(SWITCH_POKE+s);
        DisplayWindow.getInstance().getGamePanel().getFightPanel().enableActionButtons(false);
    }
}