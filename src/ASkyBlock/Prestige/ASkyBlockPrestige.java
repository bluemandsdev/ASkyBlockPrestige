package ASkyBlock.Prestige;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.*;

public class ASkyBlockPrestige extends JavaPlugin
{
    public static PlayerStore playerPrestige;

    @Override
    public void onEnable()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(this), this);

        getCommand("prestige").setExecutor(new Commands(this));

        String pluginFolder = getDataFolder().getAbsolutePath();
        new File(pluginFolder).mkdir();

        ASkyBlockPrestige.playerPrestige = new PlayerStore(new File(pluginFolder + File.separator + "player-prestige.json"));
        ASkyBlockPrestige.playerPrestige.load();

    }

    @Override
    public void onDisable()
    {
        this.playerPrestige.save();
    }

}
