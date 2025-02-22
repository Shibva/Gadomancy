package makeo.gadomancy.common.potions;

import java.util.UUID;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.entities.golems.EntityGolemBase;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 09.12.2015 15:59
 */
public class PotionBuffGolem extends PotionCustomTexture {

    public static final double SPEED_INCREASE = 0.4;
    public static final double HEALTH_INCREASE = 0.5;
    public static final double DAMAGE_INCREASE = 1;

    public static final AttributeModifier SPEED_INC_PERCENT = new AttributeModifier(
            UUID.fromString("9f76335f-2952-46e9-b91a-c555ca983ada"),
            "POTION_GOLEM_SPEED",
            PotionBuffGolem.SPEED_INCREASE,
            1);
    public static final AttributeModifier HEALTH_INC_PERCENT = new AttributeModifier(
            UUID.fromString("fafcbdc2-9c24-45a1-b832-4b6c4a5df9c7"),
            "POTION_GOLEM_HEALTH",
            PotionBuffGolem.HEALTH_INCREASE,
            1);
    public static final AttributeModifier DAMAGE_INC_PERCENT = new AttributeModifier(
            UUID.fromString("6e562261-dccf-4421-ac6f-c742989c7dfb"),
            "POTION_GOLEM_DAMAGE",
            PotionBuffGolem.DAMAGE_INCREASE,
            1);

    public PotionBuffGolem(int id) {
        super(id, false, Aspect.CRAFT.getColor(), Aspect.CRAFT.getImage());
        this.setPotionName("potion.buffGolem");
    }

    @Override
    public void applyAttributesModifiersToEntity(EntityLivingBase entity, BaseAttributeMap attrMap, int amplifier) {
        super.applyAttributesModifiersToEntity(entity, attrMap, amplifier);

        if (entity instanceof EntityGolemBase) {
            EntityGolemBase golem = (EntityGolemBase) entity;

            IAttributeInstance inst = golem.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            if (inst.getModifier(PotionBuffGolem.SPEED_INC_PERCENT.getID()) != null) {
                inst.applyModifier(PotionBuffGolem.SPEED_INC_PERCENT);
            }

            inst = golem.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            if (inst.getModifier(PotionBuffGolem.HEALTH_INC_PERCENT.getID()) != null) {
                inst.applyModifier(PotionBuffGolem.HEALTH_INC_PERCENT);
            }

            inst = golem.getEntityAttribute(SharedMonsterAttributes.attackDamage);
            if (inst.getModifier(PotionBuffGolem.DAMAGE_INC_PERCENT.getID()) != null) {
                inst.applyModifier(PotionBuffGolem.DAMAGE_INC_PERCENT);
            }
        }
    }

    @Override
    public void removeAttributesModifiersFromEntity(EntityLivingBase entity, BaseAttributeMap attrMap, int amplifier) {
        super.removeAttributesModifiersFromEntity(entity, attrMap, amplifier);

        if (entity instanceof EntityGolemBase) {
            EntityGolemBase golem = (EntityGolemBase) entity;

            IAttributeInstance inst = golem.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            if (inst.getModifier(PotionBuffGolem.SPEED_INC_PERCENT.getID()) != null) {
                inst.removeModifier(PotionBuffGolem.SPEED_INC_PERCENT);
            }

            inst = golem.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            if (inst.getModifier(PotionBuffGolem.HEALTH_INC_PERCENT.getID()) != null) {
                inst.removeModifier(PotionBuffGolem.HEALTH_INC_PERCENT);
            }

            inst = golem.getEntityAttribute(SharedMonsterAttributes.attackDamage);
            if (inst.getModifier(PotionBuffGolem.DAMAGE_INC_PERCENT.getID()) != null) {
                inst.removeModifier(PotionBuffGolem.DAMAGE_INC_PERCENT);
            }
        }
    }
}
