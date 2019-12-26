package com.github.gurpreetsachdeva;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;


public class TestClass {

	public static void main(String[] args) {

		String filePath = "/home/gurpreet/trades-data/new.json";
		
		System.out.println(getDirectory(filePath));
		System.out.println(getFileName(filePath));
		
		
		BufferedReader reader = null;
		try {
			 reader = new BufferedReader(new FileReader(filePath));
			 
			
			String line = reader.readLine();
			while (line != null) {
				System.out.println(line);
				line = reader.readLine();
			}
			
			final Path path = FileSystems.getDefault().getPath(getDirectory(filePath));
			//System.out.println(path);
			try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {
			    final WatchKey watchKey = path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
			    while (true) {
			        final WatchKey wk = watchService.take();
			        for (WatchEvent<?> event : wk.pollEvents()) {
			            //we only register "ENTRY_MODIFY" so the context is always a Path.
			            final Path changed = (Path) event.context();
			            System.out.println(changed);
			            if (changed.endsWith(getFileName(filePath))) {
			            	line=reader.readLine();
			                while(line!=null) {
			                	
			                	System.out.println(line);
			                	line=reader.readLine();
			                }
			            }
			        }
			        // reset the key
			        boolean valid = wk.reset();
			        if (!valid) {
			            System.out.println("Key has been unregisterede");
			        }
			    }
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		System.out.println("Exiting FileReaderWorker");
		
	}

	public static String getDirectory(String fullPath) {

		int index = fullPath.lastIndexOf("/");
		return fullPath.substring(0, index);

	}

	public static String getFileName(String fullPath) {

		int index = fullPath.lastIndexOf("/");

		return fullPath.substring(index+1);
	}

}
