package org.usfirst.frc.team5199.robot;

import java.util.ArrayList;

import interfaces.MainLoopObject;;

public class MainLoop {
	private ArrayList<MainLoopObject> objects;
	
	public MainLoop(){
	}
	
	public void update(){
		for(MainLoopObject o: objects){
			o.update();
		}
	}
	
	public void init(){
		for(MainLoopObject o: objects){
			o.init();
		}
	}
	
	public void add(MainLoopObject o){
		objects.add(o);
	}
	
	public void remove(MainLoopObject o){
		objects.remove(o);
	}
}
