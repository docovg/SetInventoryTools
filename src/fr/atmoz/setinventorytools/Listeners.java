package fr.atmoz.setinventorytools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Listeners implements Listener {

    private final FileConfiguration config;

    public Listeners(FileConfiguration config) {
        this.config = config;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        boolean clearInventory = config.getBoolean("clear_inventory", false);
        if (clearInventory) {
            player.getInventory().clear(); 
        }

        if (config.contains("items") && config.getConfigurationSection("items") != null) {
            for (String key : config.getConfigurationSection("items").getKeys(false)) {
                try {
                    int slot = Integer.parseInt(key);
                    String type = config.getString("items." + key + ".type");
                    String name = config.getString("items." + key + ".name");
                    List<String> lore = config.getStringList("items." + key + ".lore");
                    int amount = config.getInt("items." + key + ".amount", 1); 

                    if (type != null && Material.getMaterial(type) != null) {
                        Material material = Material.valueOf(type);
                        ItemStack item = new ItemStack(material, amount);
                        ItemMeta meta = item.getItemMeta();

                        if (meta != null) {
                            if (name != null) {
                                meta.setDisplayName(name.replace("&", "§")); 
                            }

                            if (lore != null) {
                                List<String> formattedLore = new ArrayList<>();
                                for (String line : lore) {
                                    formattedLore.add(line.replace("&", "§"));
                                }
                                meta.setLore(formattedLore);
                            }

                            item.setItemMeta(meta);

                            if (config.contains("items." + key + ".enchantments")) {
                                for (String enchant : config.getConfigurationSection("items." + key + ".enchantments").getKeys(false)) {
                                    Enchantment enchantment = Enchantment.getByName(enchant);
                                    if (enchantment != null) {
                                        int level = config.getInt("items." + key + ".enchantments." + enchant);
                                        item.addUnsafeEnchantment(enchantment, level);
                                    }
                                }
                            }

                            player.getInventory().setItem(slot, item);
                        }
                    }
                } catch (Exception e) {
                    player.sendMessage("Une erreur s'est produite lors de la configuration de l'inventaire.");
                    e.printStackTrace(); 
                }
            }
        } else {
            player.sendMessage("La configuration des items est incorrecte ou absente.");
        }
    }



}
