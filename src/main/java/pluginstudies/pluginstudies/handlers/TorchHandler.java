package pluginstudies.pluginstudies.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import pluginstudies.pluginstudies.PluginStudies;

public class TorchHandler implements Listener {
    public TorchHandler(PluginStudies plugin){
        Bukkit.getPluginManager().registerEvents(this, plugin); //2 parametros: Listener-> Quem vai escutar/registrar
                                                             //                        Plugin -> Quem vai usar evento
   //todos os eventos captados por essa classe e suas funcionali. estarão disponíveis ao inicializá-la no plugin principal
    }

    /**
     * Tipos de prioridade de eventos:
     *
     * Lowest
     * Low
     * Normal
     * High
     * Highest
     * ---
     * Monitor
     *
     * O que vai importar é o parametro na Annotation, que determina a prioridade dos eventos, a ordem no código ajuda
     * na legibilidade apenas. Os nomes das funções tem que ser diferentes. Se não especificarmos nada na @, o evento
     * terá prioridade normal
     */

//    @EventHandler(priority = EventPriority.LOW)
//    public void onTorchPlace_low(BlockPlaceEvent event){
//        if (event.getBlock().getType() == Material.TORCH){
////            event.getBlock().setType(Material.DIAMOND_BLOCK);
//
////            event.setCancelled(true);
//            // isso permite simplesmente negar a ação. o evento chega no servidor e o cancela
//            //é possível pedir para um evento posterior ignorar ações, se elas foram canceladas em uma prio. anterior
//            //(se colocarmos uma tocha, podemos cancelar o evento e colocar um param. na anot. para as próximas ações
//            //serem ignoradas, como mostrado abaixo) [o evento deixa de existir a partir desse ponto]
//        }
//
//    }

    @EventHandler//(ignoreCancelled = true)
    public void onTorchPlace_normal(BlockPlaceEvent event){ //aqui o que importa é o argumento (BlockPlaceEvent e)
        // ele permite detectar especificamente o evento de colocar blocos e fazer algo com isso (o nome da func n importa)

        Block block = event.getBlock();
        Player player = event.getPlayer();

        if (block.getType() != Material.TORCH){ //Material é um ENUM com todos os blocos do jogo (ref estática)
            return;
        }

        //Permission node
        //pluginStudies.torch.diamond

        if (!event.getPlayer().hasPermission("pluginStudies.torch.diamond")){ //se o player não tiver essa perm, faça
            Bukkit.getLogger().info("Uma tocha foi colocada!");
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Uma tocha foi colocada!");
            return;
        }

        block.setType(Material.DIAMOND_BLOCK);
    }
}
