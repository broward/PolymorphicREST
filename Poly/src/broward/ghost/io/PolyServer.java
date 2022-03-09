package broward.ghost.io;

import static spark.Spark.*;

import java.util.LinkedList;

import spark.Spark;

public class PolyServer {
	private static PolyServer instance;
	public static int PORT = 4567;
	public static LinkedList<String> routes =new LinkedList<String>();


	// get our instance
	public static PolyServer getInstance() {
		if (instance == null) {
			instance = new PolyServer();
			routes.add("/initial");
		}

		return instance;
	}


	/**
	 * Load server
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		PolyServer instance = PolyServer.getInstance();
		int count = 0;

		try {
			while (true) {
				
				// remove expired API
				if (count > 0) {
					String removed = routes.pop();
					boolean result = Spark.unmap(removed, "get");
					System.out.println("removed " + removed);
				} 
				
				// generate new API
				routes.add("hello" + count);
				System.out.println("new URL is " + routes.getLast());
				
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
