package ASkyBlock.Prestige;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.scheduler.BukkitRunnable;

public class Commands implements CommandExecutor
{
    private ASkyBlockPrestige plugin;

    public Commands(ASkyBlockPrestige plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
    {
        if (commandSender instanceof Player)
        {
            Player player = (Player) commandSender;

            if (command.getName().equals("prestige"))
            {
                int prestige = ASkyBlockPrestige.playerPrestige.getPlayerPrestige(player.getUniqueId().toString());

                if (prestige < plugin.getConfig().getInt("prestige.max"))
                {
                    ASkyBlockAPI.getInstance().calculateIslandLevel(player.getUniqueId());

                    int islandlevel = (int) ASkyBlockAPI.getInstance().getLongIslandLevel(player.getUniqueId());

                    int duePrestige = islandlevel / plugin.getConfig().getInt("prestige.level");

                    if (args.length == 0)
                    {
                        if (duePrestige < plugin.getConfig().getInt("prestige.max"))
                        {
                            player.sendMessage(ChatColor.GREEN + "You are currently Island Level: " + ChatColor.YELLOW + islandlevel);
                            if (duePrestige == 1)
                            {
                                player.sendMessage(ChatColor.GREEN + "You are currently Prestige Level: " + ChatColor.YELLOW + prestige + ChatColor.GREEN + ", you can prestige " + ChatColor.WHITE + "once" + ChatColor.GREEN + ", type " + ChatColor.YELLOW + "/prestige confirm");
                            } else
                            {
                                if (duePrestige > 0)
                                {
                                    player.sendMessage(ChatColor.GREEN + "You are currently Prestige Level: " + ChatColor.WHITE + prestige + ChatColor.GREEN + ", you can prestige " + ChatColor.WHITE + duePrestige  + ChatColor.GREEN + " times, type " + ChatColor.YELLOW + "/prestige confirm");
                                } else
                                {
                                    player.sendMessage(ChatColor.GREEN + "You are currently Prestige Level: " + ChatColor.WHITE + prestige + ChatColor.GREEN + ", you are not due a prestige yet!");
                                }
                            }

                        } else
                        {
                            player.sendMessage(ChatColor.GREEN + "You are currently Island Level: " + ChatColor.WHITE + islandlevel);
                            if (plugin.getConfig().getInt("prestige.max") - prestige >= 0)
                            {
                                player.sendMessage(ChatColor.GREEN + "You are currently Prestige Level: " + ChatColor.WHITE + prestige + ChatColor.GREEN + ", you can prestige " + ChatColor.WHITE + (plugin.getConfig().getInt("prestige.max") - prestige) + ChatColor.GREEN + " times, type " + ChatColor.YELLOW + "/prestige confirm");
                            } else
                            {
                                player.sendMessage(ChatColor.GREEN + "You are currently Prestige Level: " + ChatColor.WHITE + prestige + ChatColor.GREEN + ", you are not due a prestige yet!");
                            }
                        }


                        // /prestige
                        return true;
                    } else if (args.length == 1)
                    {
                        // /prestige with arguments
                        if (args[0].equals("confirm"))
                        {
                            if (duePrestige > 0)
                            {

                                plugin.getServer().dispatchCommand(player, "island reset");

                                plugin.getServer().dispatchCommand(player, "island confirm");

                                new BukkitRunnable()
                                {
                                    int stopper = 0;
                                    int tester = 0;

                                    @Override
                                    public void run()
                                    {
                                        if ((ASkyBlockAPI.getInstance().getLongIslandLevel(player.getUniqueId()) == 0))
                                        {
                                            String reward = plugin.getConfig().getString("prestige.reward." + (prestige + 1));

                                            String[] list = reward.split(",");

                                            for (String operate : list)
                                            {
                                                operate = operate.replace("%player%", player.getName());

                                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), operate);
                                            }

                                            ASkyBlockPrestige.playerPrestige.add(player.getUniqueId().toString(), prestige + 1);

                                            player.sendMessage(ChatColor.GREEN + "Congrats! You just reached Prestige Level: " + ChatColor.YELLOW + (prestige + 1));

                                            cancel();

                                        } else if (stopper == 30 || player.getOpenInventory().getType() != InventoryType.CHEST && !(ASkyBlockAPI.getInstance().getLongIslandLevel(player.getUniqueId()) == 0))
                                        {
                                            if(tester == stopper -1)
                                            {
                                                player.sendMessage("You must reset your island to prestige!");
                                                cancel();
                                            }
                                            else
                                            {
                                                ASkyBlockAPI.getInstance().calculateIslandLevel(player.getUniqueId());
                                                tester = stopper;
                                            }

                                        } else if(stopper % 2 == 0)
                                        {
                                            ASkyBlockAPI.getInstance().calculateIslandLevel(player.getUniqueId());
                                            stopper++;
//                                            System.out.println(stopper);
//                                            System.out.println(player.getOpenInventory().getType().toString());
//                                            System.out.println(player.getOpenInventory().getTopInventory().getType().toString());
                                        }

                                    }
                                }.runTaskTimer(plugin, 0, 20);

                            } else
                            {
                                player.sendMessage(ChatColor.RED + "You are not due a prestige!");
                            }
                            return true;
                        } else if (args[0].equals("help"))
                        {
                            player.sendMessage(ChatColor.YELLOW + "Prestige Help:");
                            player.sendMessage(ChatColor.WHITE + "You may prestige once every 20 island levels up to prestige level " + plugin.getConfig().getInt("prestige.max") + "each prestige will reward you!");
                            player.sendMessage(ChatColor.YELLOW + "---Commands---");
                            player.sendMessage(ChatColor.YELLOW + "/prestige:" + ChatColor.WHITE + " Checks your prestige level");
                            player.sendMessage(ChatColor.YELLOW + "/prestige confirm" + ChatColor.WHITE + " Increase your prestige level if eligible");
                            return true;
                        }
                    }

                    commandSender.sendMessage("Incorrect syntax");
                    return true;
                } else if (args.length > 0)
                {
                    if (args[0].equals("help"))
                    {
                        player.sendMessage(ChatColor.YELLOW + "Prestige Help:");
                        player.sendMessage(ChatColor.WHITE + "You may prestige once every 20 island levels up to prestige level " + plugin.getConfig().getInt("prestige.max") + ", each prestige will reward you!");
                        player.sendMessage(ChatColor.YELLOW + "---Commands---");
                        player.sendMessage(ChatColor.YELLOW + "/prestige:" + ChatColor.WHITE + " Checks your prestige level");
                        player.sendMessage(ChatColor.YELLOW + "/prestige confirm" + ChatColor.WHITE + " Increase your prestige level if eligible");
                        return true;
                    } else
                    {
                        player.sendMessage(ChatColor.GREEN + "You are max prestige Level: " + ChatColor.AQUA + plugin.getConfig().getInt("prestige.max") + ChatColor.GREEN + " Congrats! ");
                        return true;
                    }
                } else
                {
                    player.sendMessage(ChatColor.GREEN + "You are max Prestige Level: " + ChatColor.AQUA + plugin.getConfig().getInt("prestige.max") + ChatColor.GREEN + " Congrats! ");
                    return true;
                }
            }
        }
        return false;
    }
}
