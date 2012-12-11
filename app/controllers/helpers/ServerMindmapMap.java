package controllers.helpers;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class ServerMindmapMap {
	private final Map<Integer, Set<String>> portIdMap;
	private final Map<String, Integer> idPortMap;
	private final int maxMapsPerServer;
	private final int initialPort;

	public ServerMindmapMap(int maxMapsPerServer, int initialPort) {
		portIdMap = Collections.synchronizedMap(new HashMap<Integer, Set<String>>());
		idPortMap = Collections.synchronizedMap(new HashMap<String, Integer>());
		this.maxMapsPerServer = maxMapsPerServer;
		this.initialPort = initialPort;
	}

	public void put(int port, String mapId) {
		//add to port map
		if(!portIdMap.containsKey(port)) {
			portIdMap.put(port, new HashSet<String>());
		}
		portIdMap.get(port).add(mapId);

		//add to id map
		idPortMap.put(mapId, port);
	}

	/**
	 * 
	 * @param mapId map to remove
	 * @return hosting server or -1 of not hosted
	 */
	public int remove(String mapId) {
		if(idPortMap.containsKey(mapId)) {
			//get hosting server
			int hostingServerPort = idPortMap.get(mapId);
			Set<String> mapsOnHostingServer = portIdMap.get(hostingServerPort);
			//remove from maps
			mapsOnHostingServer.remove(mapId);
			idPortMap.remove(mapId);
			return hostingServerPort;
		} else {
			return -1;
		}
	}
	
	/**
	 * removes a server from the list
	 * @param port server to shutdown
	 * @return set with mapIds that have lost their server
	 */
	public Set<String> remove(int port) {
		if(portIdMap.containsKey(port)) {
			Set<String> mapIds = portIdMap.remove(port);
			
			for(String id: mapIds) {
				idPortMap.remove(id);
			}
			
			return mapIds;
		}
		
		return new HashSet<String>();
	}
	
	/**
	 * 
	 * @return server port or -1 if there is no free capacity
	 */
	public int getServerWithFreeCapacity() {
		for(Entry<Integer,Set<String>> entry : portIdMap.entrySet()) {
			if(entry.getValue().size() < maxMapsPerServer) {
				return entry.getKey();
			}
		}
		return -1;
	}
	
	public boolean hasOpenMaps(int port) {
		return portIdMap.containsKey(port) ? portIdMap.get(port).size() > 0 : false;
	}
	
	/**
	 * 
	 * @param mapId
	 * @return server port or -1 if not hosted
	 */
	public int getServerPortForMap(String mapId) {
		if(idPortMap.containsKey(mapId)) {
			return idPortMap.get(mapId);
		} else {
			return -1;
		}
	}
	
	public int getNextAvailablePort() {
		int lowestPort = initialPort;
		for(int port : portIdMap.keySet()) {
			if(port >= lowestPort) {
				lowestPort = port+1;
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