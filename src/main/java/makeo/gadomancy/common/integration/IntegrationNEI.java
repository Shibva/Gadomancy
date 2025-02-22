package makeo.gadomancy.common.integration;

import java.util.ArrayList;
import java.util.Iterator;
import makeo.gadomancy.common.items.ItemFakeGolemPlacer;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.utils.Injector;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 02.12.2015 13:46
 */
public class IntegrationNEI extends IntegrationMod {
    @Override
    public String getModId() {
        return "NotEnoughItems";
    }

    private Injector nei;

    @Override
    protected void doInit() {
        this.nei = new Injector("codechicken.nei.api.API");

        this.hideItem(new ItemStack(RegisteredItems.itemFakeModIcon));
        this.hideItem(new ItemStack(RegisteredBlocks.blockStickyJar, 1, Short.MAX_VALUE));
        this.hideItem(new ItemStack(RegisteredItems.itemPackage, 1, Short.MAX_VALUE));
        this.hideItem(new ItemStack(RegisteredItems.itemFakeLootbag, 1, Short.MAX_VALUE));
        this.hideItem(new ItemStack(RegisteredBlocks.blockExtendedNodeJar, 1, Short.MAX_VALUE));
        this.hideItem(new ItemStack(RegisteredItems.itemExtendedNodeJar, 1, Short.MAX_VALUE));
        this.hideItem(
                new ItemStack(RegisteredItems.itemFakeGolemPlacer, 1, Short.MAX_VALUE)); // This does... nothing? ffs.
        this.hideItem(new ItemStack(RegisteredItems.itemTransformationFocus, 1, Short.MAX_VALUE));
        this.hideItem(new ItemStack(RegisteredItems.itemFamiliar_old, 1, Short.MAX_VALUE));
    }

    public static void checkItems(ArrayList items) {
        Iterator iterator = items.iterator();
        while (iterator.hasNext()) {
            Object item = iterator.next();
            if (item != null
                    && item instanceof ItemStack
                    && ((ItemStack) item).getItem() != null
                    && ((ItemStack) item).getItem() instanceof ItemFakeGolemPlacer) {
                iterator.remove();
            }
        }
    }

    private void hideItem(ItemStack item) {
        this.nei.invokeMethod("hideItem", ItemStack.class, item);
    }

    public void setItemListEntries(Block block, Iterable<ItemStack> items) {
        this.setItemListEntries(Item.getItemFromBlock(block), items);
    }

    public void setItemListEntries(Item item, Iterable<ItemStack> items) {
        this.nei.invokeMethod("setItemListEntries", new Class[] {Item.class, Iterable.class}, item, items);
    }
}
