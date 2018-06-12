package ASkyBlock.Prestige;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLoginListener implements Listener
{
    private static ASkyBlockPrestige plugin;

    public PlayerLoginListener(ASkyBlockPrestige plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        Player player = event.getPlayer();

        if (!ASkyBlockPrestige.playerPrestige.contains(player.getUniqueId().toString()))
        {
            ASkyBlockPrestige.playerPrestige.add(player.getUniqueId().toString(), 0);
            if (plugin.getConfig().getBoolean("prefix.show"))
            {
                player.setDisplayName(player.getName() + " " + "[" + 0 + "]");
                player.setPlayerListName(player.getName() + " " + "[" + 0 + "]");
            } else
            {
                player.setDisplayName(player.getName());
                player.setPlayerListName(player.getName());
            }
        }
        updateName(player);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event)
    {
        ASkyBlockPrestige.playerPrestige.save();
    }

    public static void updateName(Player player)
    {
        if (plugin.getConfig().getBoolean("prefix.show"))
        {
            if (plugin.getConfig().getBoolean("prestige.namecolor.show"))
            {
                String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prestige.namecolor." + ASkyBlockPrestige.playerPrestige.getPlayerPrestige(player.getUniqueId().toString())));

                player.setDisplayName(prefix + player.getName() + " " + "[" + ASkyBlockPrestige.playerPrestige.getPlayerPrestige(player.getUniqueId().toString()) + "]" + ChatColor.WHITE);
                player.setPlayerListName(prefix + player.getName() + " " + "[" + ASkyBlockPrestige.playerPrestige.getPlayerPrestige(player.getUniqueId().toString()) + "]" + ChatColor.WHITE);

            } else
            {
                player.setDisplayName(player.getName() + " " + "[" + ASkyBlockPrestige.playerPrestige.getPlayerPrestige(player.getUniqueId().toString()) + "]");
                player.setPlayerListName(player.getName() + " " + "[" + ASkyBlockPrestige.playerPrestige.getPlayerPrestige(player.getUniqueId().toString()) + "]");
            }
        } else
        {
            player.setDisplayName(player.getName());
            player.setPlayerListName(player.getName());
        }
    }
}
