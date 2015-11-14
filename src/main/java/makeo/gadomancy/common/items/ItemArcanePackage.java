package makeo.gadomancy.common.items;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import thaumcraft.common.items.ItemLootBag;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 14.11.2015 12:34
 */
public class ItemArcanePackage extends ItemLootBag {
    public ItemArcanePackage() {
        setCreativeTab(null);
        setUnlocalizedName("ItemLootBag");
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {

    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return super.getIconFromDamage(damage >> 1);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        if ((stack.getItemDamage() & 1) == 1) {
            super.addInformation(stack, player, list, par4);
        } else {
            list.add(StatCollector.translateToLocal(Gadomancy.MODID + ".info.ArcanePackage"));
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        switch (stack.getItemDamage() >> 1) {
            case 1:
                return EnumRarity.uncommon;
            case 2:
                return EnumRarity.rare;
        }
        return EnumRarity.common;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if ((stack.getItemDamage() & 1) == 0) {
            return "item.ItemArcanePackage";
        }
        return super.getUnlocalizedName(stack);
    }

    public void setContents(ItemStack stack, List<ItemStack> items) {
        NBTTagList stackList = new NBTTagList();

        for (ItemStack item : items) {
            if (stack != null) {
                NBTTagCompound nbtStack = new NBTTagCompound();
                item.writeToNBT(nbtStack);
                stackList.appendTag(nbtStack);
            }
        }
        NBTHelper.getData(stack).setTag("items", stackList);
    }

    public List<ItemStack> getContents(ItemStack stack) {
        List<ItemStack> contents = new ArrayList<ItemStack>();
        if (stack.hasTagCompound()) {
            NBTTagList stackList = (NBTTagList) stack.getTagCompound().getTag("items");
            if (stackList != null) {
                for (int i = 0; i < stackList.tagCount(); ++i) {
                    NBTTagCompound nbtStack = stackList.getCompoundTagAt(i);
                    contents.add(ItemStack.loadItemStackFromNBT(nbtStack));
                }
            }
        }
        return contents;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            List<ItemStack> contents = getContents(stack);
            for (ItemStack content : contents) {
                world.spawnEntityInWorld(new EntityItem(world, player.posX, player.posY, player.posZ, content));
            }
            world.playSoundAtEntity(player, "thaumcraft:coins", 0.75F, 1.0F);
        }
        stack.stackSize -= 1;
        return stack;
    }
}
