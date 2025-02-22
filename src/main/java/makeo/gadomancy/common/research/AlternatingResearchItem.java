package makeo.gadomancy.common.research;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import makeo.gadomancy.common.utils.Injector;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 25.07.2015 19:28
 */
public class AlternatingResearchItem extends SimpleResearchItem {
    private static final Field ICON_FIELD = Injector.getField("icon_item", ResearchItem.class);

    private final Injector injector = new Injector(this, ResearchItem.class);
    protected List<ItemStack> itemIcons;

    public AlternatingResearchItem(String key, int col, int row, int complex, AspectList tags, List<ItemStack> icons) {
        super(key, col, row, complex, icons.get(0), tags);
        this.itemIcons = icons;
    }

    public AlternatingResearchItem(String key, int col, int row, int complex, AspectList tags, ItemStack... icons) {
        this(key, col, row, complex, tags, Arrays.asList(icons));
    }

    private void updateIcon() {
        this.injector.setField(AlternatingResearchItem.ICON_FIELD, this.getIcon());
    }

    protected ItemStack getIcon() {
        return this.itemIcons.get(this.calcIndex(this.itemIcons.size()));
    }

    private int calcIndex(int max) {
        return (int) (System.currentTimeMillis() / 1000L % max);
    }

    @Override
    public boolean isSpecial() {
        this.updateIcon();
        return super.isSpecial();
    }
}
