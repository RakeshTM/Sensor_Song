package com.example.sensorsong;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class SongsManager {

	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String,String>>();

	public ArrayList<HashMap<String, String>> getPlayList() {
		File file = new File("/storage/extSdCard/Bluetooth");
		if(file.listFiles(new FileExtensionFilter()).length>0)
		{
			for(File f: file.listFiles(new FileExtensionFilter()))
			{
				HashMap<String, String> song = new HashMap<String, String>();
				song.put("songName", f.getName());
				song.put("seongPath", f.getPath());
				songsList.add(song);
			}
		}
		return songsList;
	}
	
	class FileExtensionFilter implements FilenameFilter
	{

		@Override
		public boolean accept(File dir, String filename) {
			// TODO Auto-generated method stub
			return (filename.endsWith(".mp3")|| filename.endsWith(".MP3"));
		}
		
	}
}
