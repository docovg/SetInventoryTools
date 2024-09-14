package fr.atmoz.setinventorytools;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
	    saveDefaultConfig(); 
	    getServer().getPluginManager().registerEvents(new Listeners(getConfig()), this);
	}


}
