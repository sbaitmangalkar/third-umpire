package com.voter.info.util;

/**
 * 
 * @author Shyam | catch.shyambaitmangalkar@gmail.com
 *
 */
public class MemoryManager {
	/**
	 * 
	 */
	private static void garbageCollect() {
		try {
			System.gc();
			Thread.sleep(200);
			System.runFinalization();
			Thread.sleep(200);
			System.gc();
			Thread.sleep(200);
			System.runFinalization();
			Thread.sleep(200);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static long getMemoryUse() {
        garbageCollect();
        garbageCollect();
        garbageCollect();
        garbageCollect();
        long totalMemory = Runtime.getRuntime().totalMemory();
        garbageCollect();
        garbageCollect();
        long freeMemory = Runtime.getRuntime().freeMemory();
        return (totalMemory - freeMemory);
    }
}
