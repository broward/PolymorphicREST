package broward.ghost.io;

import static spark.Spark.*;

import java.util.LinkedList;

import spark.Spark;

public class PolyServer {
	private static PolyServer instance;
	public static int PORT = 8081;
	public static String BASE_URL = "http://localhost:" + PORT + "/";
	public static LinkedList<String> routes = new LinkedList<String>();
	public static int count = 0;


	// get our instance
	public static PolyServer getInstance() {
		if (instance == null) {
			instance = new PolyServer();
			port(PORT);
		}

		return instance;
	}
	
	public static String mockMFA() {
		return "hello" + count++;
	}


	/**
	 * Load server
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		PolyServer.getInstance();

		try {
			while (true) {
				
				// remove expired API after one exists
				if (routes.size() > 1) {
					String removed = routes.pop();
					boolean result = Spark.unmap(removed, "get");
					System.out.println("removed:   " + BASE_URL + removed);
				} 
				
				// generate new API using MFA key
				routes.add(mockMFA());
				System.out.println("added:  " + BASE_URL + routes.getLast() + "\n");
				
				// add new API
				get(routes.getLast(), (request, response) -> {
					return "hello " + routes.getLast();
				});
				
				count++;
				Thread.sleep(10 * 1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
