package codered.crLoot.LootTask;

import codered.universal.Util.crTask;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

/**
 * Created by Dakota on 5/30/2016.
 */
public class Bank extends crTask<ClientContext> {
    public Bank(ClientContext ctx) {
        super(ctx);
    }

    public boolean activate() {
        return ctx.inventory.select().count() == 28
                || ctx.players.local().inCombat();
    }

    @Override
    public void execute() {
        GameObject WildyDitch = ctx.objects.select().name("Wilderness Ditch").nearest().poll();
        if (ctx.players.local().tile().y() < 3522) {
            if (!ctx.bank.opened()) {
                ctx.movement.step(ctx.bank.nearest());
            } else {
                ctx.bank.open();
            }

            if (ctx.bank.open()) {
                ctx.bank.depositInventory();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().count() == 0;
                    }
                }, 75, 10);
                ctx.bank.close();
            }
        } else {
            if (!WildyDitch.inViewport()) {
                ctx.camera.turnTo(WildyDitch);
                if (!WildyDitch.inViewport()) {
                    ctx.movement.step(new Tile(ctx.players.local().tile().x() + Random.nextInt(-5, 5), ctx.players.local().tile().y() - Random.nextInt(15, 25)));
                }
            }

            if (WildyDitch.inViewport()) {
                WildyDitch.interact("Cross");

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().tile().y() < 3522;
                    }
                });
            }
        }
    }
}
