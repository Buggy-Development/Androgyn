package de.androgyn;

import java.io.File;
import java.nio.file.Paths;

import org.apache.logging.log4j.Logger;

import de.androgyn.gui.BendingGUIOverlay;
import de.androgyn.listeners.ChatInputListener;
import de.androgyn.managers.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@net.minecraftforge.fml.common.Mod(modid = Androgyn.MODID, name = Androgyn.NAME, version = Androgyn.VERSION)
public class Androgyn {
    public static final String MODID = "androgyn";
    public static final String NAME = "Androgyn";
    public static final String VERSION = "1.0";

    public static final String moduleConfigLocation = Paths.get("").toAbsolutePath()+"/Androgyn/modules";
    
    
    private static Logger logger;
    private static Androgyn instance;
    private static Minecraft minecraft;
    

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        minecraft = Minecraft.getMinecraft();
    	instance = this;
        logger = event.getModLog();
    	
    	
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {


		File folder = new File(moduleConfigLocation);
		if (!folder.exists()) folder.mkdirs();

    	MinecraftForge.EVENT_BUS.register(new BendingGUIOverlay());
    	MinecraftForge.EVENT_BUS.register(new ChatInputListener());
        
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    	new ModuleManager();
    }
    
    public static Androgyn getInstance() {
    	return instance;
    }
    
    public static Logger getLogger() {
    	return logger;
    }
    
    public static Minecraft getMinecraft() {
		return minecraft;
	}
}
