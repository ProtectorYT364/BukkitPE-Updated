package net.BukkitPE.event.entity;

import net.BukkitPE.entity.Entity;
import net.BukkitPE.event.Cancellable;
import net.BukkitPE.event.HandlerList;

public class EntityPortalEnterEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    public static final int TYPE_NETHER = 0;
    public static final int TYPE_END = 1;

    private final int type;

    public static HandlerList getHandlers() {
        return handlers;
    }

    public EntityPortalEnterEvent(Entity entity, int type) {
        this.entity = entity;
        this.type = type;
    }

    public int getPortalType() {
        return type;
    }
}
