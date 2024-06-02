package me.messdaniel.configui.menu;

import me.messdaniel.configui.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class MenuItem {

    private String name = "dummy";
    private Material material = Material.DIRT;
    private Integer amount = 1;
    private List<String> lore = new ArrayList<>();
    private ArrayList<Integer> slots = new ArrayList<>();
    private String skullTexture;
    private ArrayList<ItemFlag> itemFlags = new ArrayList<>();
    private HashMap<Enchantment,Integer> enchantments = new HashMap<>();
    private PotionType effect;
    private boolean extended;
    private boolean upgraded;
    private Integer customModelData;
    private List<ClickEvent> clickEvents = new ArrayList<>();

    public MenuItem() {
    }
    public MenuItem(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) name = "";
        this.name = name;
    }

    public Material getMaterial() {
        if (material == null) material = Material.DIRT;
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getAmount() {
        if (amount == null || amount < 1) amount = 1;
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public ArrayList<Integer> getSlots() {
        return slots;
    }

    public void setSlots(ArrayList<Integer> slots) {
        this.slots = slots;
    }

    public ArrayList<ItemFlag> getItemFlags() {
        return itemFlags;
    }

    public void setItemFlags(ArrayList<ItemFlag> itemFlags) {
        this.itemFlags = itemFlags;
    }
    public void addItemFlag(ItemFlag itemFlag) {
        this.itemFlags.add(itemFlag);
    }

    public String getSkullTexture() {
        return skullTexture;
    }

    public void setSkullTexture(String skullTexture) {
        this.skullTexture = skullTexture;
    }

    public HashMap<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public void setEnchantments(HashMap<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
    }

    public void addEnchantments(Enchantment enchantment,Integer integer) {
        this.enchantments.put(enchantment,integer);
    }

    public PotionType getEffect() {
        return effect;
    }

    public void setEffect(PotionType effect) {
        this.effect = effect;
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public boolean isUpgraded() {
        return upgraded;
    }

    public void setUpgraded(boolean upgraded) {
        this.upgraded = upgraded;
    }

    public Integer getCustomModelData() {
        return customModelData;
    }

    public void setCustomModelData(Integer customModelData) {
        this.customModelData = customModelData;
    }

    public List<ClickEvent> getClickActions() {
        return clickEvents;
    }

    public void setClickActions(List<ClickEvent> clickEvents) {
        this.clickEvents = clickEvents;
    }
    public void addClickAction(ClickEvent clickEvent) {
        this.clickEvents.add(clickEvent);
    }

    public ItemStack createItem(Player player) {
        ItemStack item = new ItemStack(getMaterial(),getAmount());
        ItemMeta meta = item.getItemMeta();
        if (!getName().equalsIgnoreCase("")) meta.setDisplayName(ColorUtils.translate(getName()));
        ArrayList<String> lore = new ArrayList<>();
        for (String string : getLore()) {
            lore.add(ColorUtils.translate(string));
        }
        meta.setLore(lore);
        for (ItemFlag itemFlag : getItemFlags()) {
            meta.addItemFlags(itemFlag);
        }
        for (Enchantment enchantment : getEnchantments().keySet()) {
            meta.addEnchant(enchantment,getEnchantments().get(enchantment),true);
        }
        item.setItemMeta(meta);
        if (getSkullTexture() != null && getMaterial().equals(Material.PLAYER_HEAD)) {
            SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
            if (getSkullTexture().equalsIgnoreCase("{player}")) {
                skullMeta.setOwningPlayer(player);
            } else if (getSkullTexture().toCharArray().length <= 16) {
                skullMeta.setOwner(getSkullTexture());
            } else {
                PlayerProfile profile = getProfile(getSkullTexture());
                skullMeta.setOwnerProfile(profile);
            }
            item.setItemMeta(skullMeta);
        } else if (getEffect() != null) {
            PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
            if (potionMeta != null) {
                PotionData potionData = new PotionData(getEffect(), isExtended(), isUpgraded());
                potionMeta.setBasePotionData(potionData);
                item.setItemMeta(potionMeta);
            }
        } else if (getCustomModelData() != null) {
            meta.setCustomModelData(getCustomModelData());
            item.setItemMeta(meta);
        }
        return item;
    }

    public static PlayerProfile getProfile(String texture) {
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            if (isBase64(texture)) {
                urlObject = getSkinUrlFromBase64(texture);
            } else {
                urlObject = new URL("https://textures.minecraft.net/texture/" + textures);
            }
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject);
        profile.setTextures(textures);
        return profile;
    }
    public static URL getSkinUrlFromBase64(String texture) throws MalformedURLException {
        String decoded = new String(Base64.getDecoder().decode(texture));
        return new URL(decoded.substring("{\"textures\":{\"SKIN\":{\"url\":\"".length(), decoded.length() - "\"}}}".length()));
    }
    public static boolean isBase64(String str) {
        if (str == null || str.trim().isEmpty()) return false;
        if (!str.matches("^[a-zA-Z0-9/+]*={0,2}$"))  return false;
        if (str.length() % 4 != 0)  return false;

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(str);
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }
}
