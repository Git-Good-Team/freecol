
package net.sf.freecol.metaserver;


import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import net.sf.freecol.common.networking.Connection;
import net.sf.freecol.common.networking.Message;
import net.sf.freecol.server.FreeColServer;

import org.w3c.dom.Element;



/**
* The <code>MetaRegister</code> stores information about running servers.
* Each server has it's own {@link MetaItem} object.
*/
public final class MetaRegister {
    private static Logger logger = Logger.getLogger(MetaRegister.class.getName());

    public static final String  COPYRIGHT = "Copyright (C) 2003-2005 The FreeCol Team";
    public static final String  LICENSE = "http://www.gnu.org/licenses/gpl.html";
    public static final String  REVISION = "$Revision$";
    
    private ArrayList<MetaItem> items = new ArrayList<MetaItem>();
    
    
    /**
    * Gets the server entry with the diven address and port.
    *
    * @param address The IP-address of the server.
    * @param port The port number of the server.
    * @return The server entry or <code>null</code> if the given 
    *         entry could not be found.
    */
    private MetaItem getItem(String address, int port) {
        int index = indexOf(address, port);
        if (index >= 0) {
            return items.get(index);
        } else {
            return null;
        }
    }
    

    /**
    * Gets the index of the server entry with the diven address and port.
    *
    * @param address The IP-address of the server.
    * @param port The port number of the server.
    * @return The index or <code>-1</code> if the given entry could not be found.
    */
    private int indexOf(String address, int port) {
        for (int i=0; i<items.size(); i++) {
            if (address.equals(items.get(i).getAddress()) && port == items.get(i).getPort()) {
                return i;
            }
        }

        return -1;
    }


    /**
    * Removes servers that have not sent an update for some time.
    */
    public synchronized void removeDeadServers() {
        logger.info("Removing dead servers.");

        long time = System.currentTimeMillis() - MetaServer.REMOVE_OLDER_THAN;
        for (int i=0; i<items.size(); i++) {
            if (items.get(i).getLastUpdated() < time) {
                logger.info("Removing: " + items.get(i));
                items.remove(i);
            }
        }
    }


    /**
    * Adds a new server with the given attributes.
    *
    * @param name The name of the server.
    * @param address The IP-address of the server.
    * @param port The port number in which clients may connect.
    * @param slotsAvailable Number of players that may conncet.
    * @param currentlyPlaying Number of players that are currently connected.
    * @param isGameStarted <i>true</i> if the game has started.
    * @param version The version of the server.
    * @param gameState The current state of the game.
    */
    public synchronized void addServer(String name, String address, int port, int slotsAvailable,
                int currentlyPlaying, boolean isGameStarted, String version, int gameState)
                throws IOException {
        MetaItem mi = getItem(address, port);
        if (mi == null) {
            // Check connection before adding the server:
            Connection mc = null;
            try {                
                mc = new Connection(address, port, null);
                Element element = Message.createNewRootElement("disconnect");
                mc.send(element);
            } catch (IOException e) {
                logger.info("Server rejected (no route to destination):" + address + ":" + port);
                throw e;
            } finally {
                try {
                    if (mc != null) {
                        mc.close();
                    }
                } catch (IOException e) {}
            }
            items.add(new MetaItem(name, address, port, slotsAvailable, currentlyPlaying, isGameStarted, version, gameState));
            logger.info("Server added:" + address + ":" + port);
        } else {
            updateServer(mi, name, address, port, slotsAvailable, currentlyPlaying, isGameStarted, version, gameState);
        }
    }


    /**
    * Updates a server with the given attributes.
    *
    * @param name The name of the server.
    * @param address The IP-address of the server.
    * @param port The port number in which clients may connect.
    * @param slotsAvailable Number of players that may conncet.
    * @param currentlyPlaying Number of players that are currently connected.
    * @param isGameStarted <i>true</i> if the game has started.
    * @param version The version of the server.
    * @param gameState The current state of the game.
    */
    public synchronized void updateServer(String name, String address, int port, int slotsAvailable,
            int currentlyPlaying, boolean isGameStarted, String version, int gameState)
            throws IOException {
        MetaItem mi = getItem(address, port);
        if (mi == null) {
            addServer(name, address, port, slotsAvailable, currentlyPlaying, isGameStarted, version, gameState);
        } else {
            updateServer(mi, name, address, port, slotsAvailable, currentlyPlaying, isGameStarted, version, gameState);
        }
    }


    /**
    * Removes a server from the register.
    * @param address The IP-address of the server to remove.
    * @param port The port number of the server to remove.
    */
    public synchronized void removeServer(String address, int port) {
        int index = indexOf(address, port);
        if (index >= 0) {
            items.remove(index);
            logger.info("Removing server:" + address + ":" + port);
        } else {
            logger.info("Trying to remove non-existing server:" + address + ":" + port);
        }
    }

    
    /**
    * Creates a server list.
    * @return The server list as an XML DOM Element.
    */
    public synchronized Element createServerList() {
        Element element = Message.createNewRootElement("serverList");
        for (int i=0; i<items.size(); i++) {
            element.appendChild(items.get(i).toXMLElement(element.getOwnerDocument()));
        }
        return element;
    }


    /**
    * Updates a given <code>MetaItem</code>.
    *
    * @param mi The <code>MetaItem</code> that should be updated.
    * @param name The name of the server.
    * @param address The IP-address of the server.
    * @param port The port number in which clients may connect.
    * @param slotsAvailable Number of players that may conncet.
    * @param currentlyPlaying Number of players that are currently connected.
    * @param isGameStarted <i>true</i> if the game has started.
    * @param version The version of the server.
    * @param gameState The current state of the game: {@link FreeColServer#STARTING_GAME},
    *       {@link FreeColServer#IN_GAME} or {@link FreeColServer#ENDING_GAME}.
    */
    private void updateServer(MetaItem mi, String name, String address, int port, int slotsAvailable,
            int currentlyPlaying, boolean isGameStarted, String version, int gameState) {
        mi.update(name, address, port, slotsAvailable, currentlyPlaying, isGameStarted, version, gameState);
        logger.info("Server updated:" + mi.toString());
    }
}

