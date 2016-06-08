package codered.crLoot.LootTask;

import codered.universal.Util.crTask;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GeItem;
import org.powerbot.script.rt4.GroundItem;

import java.util.concurrent.Callable;

/**
 * Created by Dakota on 5/30/2016.
 */
public class Take extends crTask<ClientContext> {
    int profit = 0;
    public Take(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() < 28
                && ctx.players.local().tile().y() > 3522
                && !ctx.players.local().inCombat();
    }

    @Override
    public void execute() {
        if (!ctx.movement.running() && ctx.movement.energyLevel() > 25) {
            ctx.movement.running(true);
        }

        final GroundItem Item = ctx.groundItems.select().action("Take").select(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundItem) {
                return groundItem.tile().y() > 3522;
            }
        }).nearest().poll();

        System.out.println("Item: " + Item.name() + " (" + Item.stackSize() + ")");
        if (!Item.inViewport()) {
            ctx.camera.turnTo(Item);
            if (!Item.inViewport()) {
                ctx.movement.findPath(Item).traverse();
            }
        } else {
            Item.interact("Take", Item.name());
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !Item.valid();
                }
            });

            profit += new GeItem(Item.id()).price * Item.stackSize();
            System.out.println("Profit: " + profit);
        }
    }
}
