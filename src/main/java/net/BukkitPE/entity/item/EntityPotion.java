package net.BukkitPE.entity.item;

import net.BukkitPE.Player;
import net.BukkitPE.entity.Entity;
import net.BukkitPE.entity.projectile.EntityProjectile;
import net.BukkitPE.event.potion.PotionCollideEvent;
import net.BukkitPE.level.format.FullChunk;
import net.BukkitPE.level.particle.InstantSpellParticle;
import net.BukkitPE.level.particle.Particle;
import net.BukkitPE.level.particle.SpellParticle;
import net.BukkitPE.nbt.tag.CompoundTag;
import net.BukkitPE.network.protocol.AddEntityPacket;
import net.BukkitPE.potion.Effect;
import net.BukkitPE.potion.Potion;

/**
 * @author xtypr
 */
public class EntityPotion extends EntityProjectile {

    public static final int NETWORK_ID = 86;

    public static final int DATA_POTION_ID = 16;

    public int potionId;

    public EntityPotion(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    public EntityPotion(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        potionId = this.namedTag.getShort("PotionId");

        this.dataProperties.putShort(DATA_POTION_ID, this.potionId);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.25f;
    }

    @Override
    public float getLength() {
        return 0.25f;
    }

    @Override
    public float getHeight() {
        return 0.25f;
    }

    @Override
    protected float getGravity() {
        return 0.1f;
    }

    @Override
    protected float getDrag() {
        return 0.01f;
    }


    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        this.timing.startTiming();

        int tickDiff = currentTick - this.lastUpdate;
        boolean hasUpdate = super.onUpdate(currentTick);

        if (this.age > 1200) {
            this.kill();
            hasUpdate = true;
        }

        if (this.isCollided) {
            this.kill();

            Potion potion = Potion.getPotion(this.potionId);

            PotionCollideEvent event = new PotionCollideEvent(potion, this);
            this.server.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return false;
            }

            potion = event.getPotion();
            if (potion == null) {
                return false;
            }

            potion.setSplash(true);

            Particle particle;
            int r;
            int g;
            int b;

            Effect effect = Potion.getEffect(potion.getId(), true);

            if (effect == null) {
                r = 40;
                g = 40;
                b = 255;
            } else {
                int[] colors = effect.getColor();
                r = colors[0];
                g = colors[1];
                b = colors[2];
            }

            if (Potion.isInstant(potion.getId())) {
                particle = new InstantSpellParticle(this, r, g, b);
            } else {
                particle = new SpellParticle(this, r, g, b);
            }

            this.getLevel().addParticle(particle);

            hasUpdate = true;
            Entity[] entities = this.getLevel().getNearbyEntities(this.getBoundingBox().grow(8.25, 4.24, 8.25));
            for (Entity anEntity : entities) {
                potion.applyPotion(anEntity);
            }
        }
        this.timing.stopTiming();
        return hasUpdate;
    }

    @Override
    public void spawnTo(Player player) {
        AddEntityPacket pk = new AddEntityPacket();
        pk.type = EntityPotion.NETWORK_ID;
        pk.eid = this.getId();
        pk.x = (float) this.x;
        pk.y = (float) this.y;
        pk.z = (float) this.z;
        pk.speedX = (float) this.motionX;
        pk.speedY = (float) this.motionY;
        pk.speedZ = (float) this.motionZ;
        pk.metadata = this.dataProperties;
        player.dataPacket(pk);

        super.spawnTo(player);
    }
}
