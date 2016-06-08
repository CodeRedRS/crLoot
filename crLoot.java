package codered.crLoot;

import codered.crLoot.LootTask.Bank;
import codered.crLoot.LootTask.ReturnToWild;
import codered.crLoot.LootTask.Take;
import codered.universal.Util.crTask;
import codered.universal.Util.crVariables;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

import java.util.Arrays;

/**
 * Created by Dakota on 5/30/2016.
 */
@Script.Manifest (
        name = "crLoot PRE_ALPHA",
        description = "Looting the wilderness",
        properties = "topic=0;client=4;hidden=true;"
)
public class crLoot extends PollingScript<ClientContext> {
    private Tile BankTile = null;

    @Override
    public void start() {

        crVariables.taskList.addAll(Arrays.asList(new Bank(ctx), new ReturnToWild(ctx), new Take(ctx)));
    }

    @Override
    public void poll() {
        try {
            for (crTask t : crVariables.taskList) {
                if (t.activate()) {
                    t.execute();
                }
            }
        } catch (Exception ex) {
            ctx.controller.script().log.info(ex.getMessage());
        }
    }
}
