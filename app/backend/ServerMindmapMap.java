package backend;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

//TODO backend team, what is the task of this class?
public class ServerMindmapMap {
	private final Map<URL, Set<String>> serverMapIdMap;
	private final Map<String, URL> mapIdServerMap;
	private final int maxMapsPerServer;
	private final int initialPort;

	public ServerMindmapMap(int maxMapsPerServer, int initialPort) {
		serverMapIdMap = Collections.synchronizedMap(new HashMap<URL, Set<String>>());
		mapIdServerMap = Collections.synchronizedMap(new HashMap<String, URL>());
		this.maxMapsPerServer = maxMapsPerServer;
		this.initialPort = initialPort;
	}

	public void put(URL serverAddress, String mapId) {
		//add to port map
		if(!serverMapIdMap.containsKey(serverAddress)) {
			serverMapIdMap.put(serverAddress, new HashSet<String>());
		}
		serverMapIdMap.get(serverAddress).add(mapId);

		//add to id map
		mapIdServerMap.put(mapId, serverAddress);
	}

	/**
	 * 
	 * @param mapId map to remove
	 * @return hosting server or null of not hosted
	 */
	public URL remove(String mapId) {
		if(mapIdServerMap.containsKey(mapId)) {
			//get hosting server
			URL hostingServerPort = mapIdServerMap.get(mapId);
			Set<String> mapsOnHostingServer = serverMapIdMap.get(hostingServerPort);
			//remove from maps
			mapsOnHostingServer.remove(mapId);
			mapIdServerMap.remove(mapId);
			return hostingServerPort;
		} else {
			return null;
		}
	}
	
	/**
	 * removes a server from the list
	 * @param port server to shutdown
	 * @return set with mapIds that have lost their server
	 */
	public Set<String> remove(URL server) {
		if(serverMapIdMap.containsKey(server)) {
			Set<String> mapIds = serverMapIdMap.remove(server);
			
			for(String id: mapIds) {
				mapIdServerMap.remove(id);
			}
			
			return mapIds;
		}
		
		return new HashSet<String>();
	}
	
	/**
	 * 
	 * @return server port or null if there is no free capacity
	 */
	public URL getServerWithFreeCapacity() {
		for(Entry<URL,Set<String>> entry : serverMapIdMap.entrySet()) {
			if(entry.getValue().size() < maxMapsPerServer) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	public boolean hasOpenMaps(URL serverUrl) {
		return serverMapIdMap.containsKey(serverUrl) ? serverMapIdMap.get(serverUrl).size() > 0 : false;
	}
	
	/**
	 * 
	 * @param mapId
	 * @return server port or null if not hosted
	 */
	public URL getServerURLForMap(String mapId) {
		if(containsServerURLForMap(mapId)) {
			return mapIdServerMap.get(mapId);
		} else {
			return null;
		}
	}

    /**
     *
     * @param mapId
     * @return boolean if contains mapId
     */
    public boolean containsServerURLForMap(String mapId) {
        return mapIdServerMap.containsKey(mapId);
    }
	
	public int getNextAvailablePort() {
		int lowestPort = initialPort;
		for(URL server : serverMapIdMap.keySet()) {
			if(server.getPort() >= lowestPort) {
				lowestPort = server.getPort()+1;
			}
		}
		return lowestPort;
	}
	
	public static class MapMetaData {
		public long lastTimeAccessed;
		public int serverPort;
		
		public MapMetaData(long lastTimeAccessed, int serverPort) {
			this.lastTimeAccessed = lastTimeAccessed;
			this.serverPort = serverPort;
		}	
	}
}