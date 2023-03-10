package pluginstudies.pluginstudies.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pluginstudies.pluginstudies.PluginStudies;
import pluginstudies.pluginstudies.managers.UIManager;

import static pluginstudies.pluginstudies.utils.Utils.*;

public class ArmorStand implements CommandExecutor {

    private UIManager UIManager;
    private PluginStudies plugin;

    public ArmorStand(PluginStudies pluginStudies){
        plugin = pluginStudies;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)){
            log("Apenas jogadores podem usar o comando ArmorStandGUI");
            return true;
        }

        Player player = (Player) sender;
        UIManager = new UIManager(plugin, player);

        UIManager.openMainASInterface();
//        Inventory inventory = Bukkit.createInventory(player, 3 * 9, color("&8&lArmor Stand GUI"));
//
//        ItemStack armorStand = new ItemStack(Material.ARMOR_STAND);
//        ItemMeta armorStandMeta = armorStand.getItemMeta();
//        armorStandMeta.setDisplayName(color("&c&lCustom Test Dummy UI"));
//        armorStandMeta.setLore(Arrays.asList(color("&7"),color("&7Build your custom armor stand")));
//        armorStand.setItemMeta(armorStandMeta);
//        inventory.setItem(13, armorStand);
//
//        ItemStack exitIndicator = new ItemStack(Material.BARRIER);
//        ItemMeta exitMeta = exitIndicator.getItemMeta();
//        exitMeta.setDisplayName(color("&c&lClick to exit"));
//        exitIndicator.setItemMeta(exitMeta);
//        inventory.setItem(26, exitIndicator);
//
//        player.openInventory(inventory);

        return true;
    }
}
