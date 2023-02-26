package pluginstudies.pluginstudies;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pluginstudies.pluginstudies.commands.*;
import pluginstudies.pluginstudies.handlers.GUIHandler;
import pluginstudies.pluginstudies.handlers.PlayerHandler;
import pluginstudies.pluginstudies.handlers.SkillsUIHandler;
import pluginstudies.pluginstudies.handlers.TorchHandler;
import pluginstudies.pluginstudies.managers.ProfileManager;
import pluginstudies.pluginstudies.utils.ConfigUtil;
import pluginstudies.pluginstudies.utils.DelayedTask;
import pluginstudies.pluginstudies.utils.Utils;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.logging.Logger;

public final class PluginStudies extends JavaPlugin {
    private static Logger logger;
    private ProfileManager profileManager;
    private ConfigUtil profileConfig;


    private BukkitTask task; //vai receber o bukkit runnable que vai operar a logica do spawn
    private World world;
    private List<Entity> entities = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
//        Bukkit.getLogger().info("Hello, World!");
        logger = getLogger(); //pega o logger desse plugin, que dá acesso aos chats e mensagens (?)
        Utils.log("O novo hello world!");

        this.profileConfig = new ConfigUtil(this, "profiles.yml");

        this.profileManager = new ProfileManager(this);
        this.profileManager.loadProfilesFromConfig();

        world = Bukkit.getWorld("world");
        spawnMobs(8,12, 5 * 20);

//        saveDefaultConfig();

//        List<String> basicWeap = (List<String>) getConfig().getList("basic-weapons");
        //getConfig() lê o arquivo config.yml disponível no momento (em resources)
        //getList() procura, no arquivo, a key com o nome do path indicado. retorna uma List<> com os itens

        //Como um dos usos para o config file, podemos ler o conteúdo e usá-lo no código de forma rápida:
//        for (String weapon : basicWeap){
//            Bukkit.getLogger().info(weapon);//irá logar as armas basicas no console
//        }

//        ConfigUtil config = new ConfigUtil(this, "test.yml");
//        config.getConfig().set("hello", "world");//existem os gets e os sets para escrever ou recuperar info. do arquivo.
//        //ao setar, definimos a primeira string como key e a segunda como value
//        config.save();

        SkillsUI skillsUI = new SkillsUI(this);

        getCommand("skills").setExecutor(skillsUI);
        getCommand("skills").setTabCompleter(skillsUI);
        getCommand("fly").setExecutor(new Fly());
        getCommand("combatmenu").setExecutor(new Menu(this));
        getCommand("buildertoolkit").setExecutor(new BuilderToolkit());
        getCommand("trainingdummy").setExecutor(new ArmorStand(this));

        //---------   LISTENERS   ------------
        new TorchHandler(this);
        new PlayerHandler(this);
        new DelayedTask(this);
//        new SkillsUIHandler(this);
        new GUIHandler(this);
    }

    @Override
    public void onDisable() {
        profileManager.saveProfilesToConfig(); //passando as mudanças para o configFile
        // Plugin shutdown logic
        //salvar efetivamente as mudanças em no configFile profileConfig
        profileConfig.save();
        Bukkit.getLogger().info("Shutting Down...");
    }

    public ConfigUtil getProfileConfig(){
        return this.profileConfig;
    }

    public static Logger getPluginLogger(){
        return logger;
    }
    public ProfileManager getProfileManager(){
        return this.profileManager;
    }

    public void spawnMobs(int areaSize, int mobCap, int timer){

        //Ao chamar o método spawnMobs, queremos iniciar um BukkitRunnable Anonimo rodando o seu método run()
        //que irá ser responsável pela lógica de spawn e irá rodar no timer especificado em runTaskTimer()


        task = new BukkitRunnable() {
            @Override
            public void run(){
                //Checking if there are valid entities in the list
                //Criamos uma lista das entidades que devem ser removidas por invalidez
                try{
                    List<Entity> removal = new ArrayList<>();
                    for (Entity entity : entities){
                        if (entity.isDead() || !entity.isValid()){ //se não é valida/ está morta:
                            removal.add(entity);
                        }
                    }
                        //agora tiramos todas as entidades de removal da lista principal "entities"
                        // .removeAll() nos permite tirar todos os elementos na coleção "removal" da coleção "entities"
                        //agora "entities" possui espaços vagos e permitirá novos spawns
                        entities.removeAll(removal);
                }catch(ConcurrentModificationException exeption){
                    Utils.error("Multiple concurrent attempts to alterate lists/collections");
                }

                //Spawning Algorithm

                int availableMobCap = mobCap - entities.size();
                if (availableMobCap<= 0){return;}

                int hordeSize = (int) (Math.random() * (availableMobCap + 1));
                int count = 0;
                while (count <= hordeSize){
                    count++;
                    int randX = getRandomWithNeg(areaSize);
                    int randZ = getRandomWithNeg(areaSize);
                    Block block = world.getHighestBlockAt(randX, randZ); //na coordenada escolhida, pegue o bloco mais alto
                    double xOffset = getRandomOffset();
                    double zOffset = getRandomOffset();
                    Location loc = block.getLocation().clone().add(xOffset,1,zOffset); //queremos spawnar 1 bloco acima do escolhido

                    entities.add(world.spawnEntity(loc, EntityType.ZOMBIE)); //adicionamos à var. entities todos os zombies

                }
            }

        }.runTaskTimer(this, 0L, timer);

    }

    public int getRandomWithNeg(int range){
        int random = (int) (Math.random() * (range+1)); //gere um número no range
        if (Math.random() > 0.5){random *= -1;} //50% de ser negativo
        return random;
    }

    public double getRandomOffset(){ //nos dá um offset entre 0 e 0.999...
        double random = Math.random();
        if (Math.random() > 0.5){random *= -1;}
        return random;
    }

}