package codered.crLoot.LootTask;

import codered.universal.Util.crTask;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

/**
 * Created by Dakota on 5/30/2016.
 */
public class ReturnToWild extends crTask<ClientContext> {
    public ReturnToWild(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() < 28
                && ctx.players.local().tile().y()  < 3522;
    }

    @Override
    public void execute() {
        GameObject WildyDitch = ctx.objects.select().name("Wilderness Ditch").nearest().poll();
        if (!WildyDitch.inViewport()) {
            ctx.movement.step(WildyDitch);
            ctx.camera.turnTo(WildyDitch);
        }

        if (WildyDitch.inViewport()) {
            WildyDitch.interact("Cross");

            if (ctx.widgets.component(382,27).visible()) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.widgets.component(382,27).click() && ctx.varpbits.varpbit(1045) == 3670016;
                    }
                }, 300, 10);
            }

            if (ctx.widgets.component(382, 24).visible()) {
                ctx.widgets.component(382,24).click();
            }

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().tile().y() > 3522;
                }
            });
        }
    }
}
