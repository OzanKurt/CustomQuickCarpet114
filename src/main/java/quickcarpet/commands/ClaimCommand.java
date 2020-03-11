package quickcarpet.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import quickcarpet.interfaces.PlayerEntityMixinInterface;
import quickcarpet.utils.HttpClient;
import quickcarpet.utils.Messenger;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.minecraft.server.command.CommandManager.literal;

public class ClaimCommand {
    // TODO: allow any order like execute
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = literal("claim")
            .executes(ClaimCommand::claim);

        dispatcher.register(builder);
    }

    private static int claim(CommandContext<ServerCommandSource> context) {
        String playerName = getString(context, "player");

        pla

        HttpClient client = new HttpClient();
//        client.
        MinecraftServer server = context.getSource().getMinecraftServer();
        ServerPlayerEntity player = server.getPlayerManager().getPlayer(playerName);
        PlayerEntityMixinInterface playerMixin = (PlayerEntityMixinInterface) server.getPlayerManager().getPlayer(playerName);

        int emptySlotCount = player.inventory.getEmptySlot();

        if (emptySlotCount == 0) {
            sendMessageToPlayer(context, "Your inventory is full, cannot buy the item.");

            return 1;
        }

        Entity item = new SwordItem(ToolMaterials.DIAMOND, 10, 1);

        player.sendPickup(item, 1);
        sendMessageToPlayer(context, "The items have been added to your inventory.");

//        String message = null;
//
//        if (playerMixin.getDonatorStatus() == DonatorStatus.NON_DONATOR) {
//            message = String.format(
//                "The player %s has not donated yet.",
//                player.getName().asString()
//            );
//        } else {
//            message = String.format(
//                "The player %s has donated %d$ so far and has the donator status %s.",
//                player.getName().asString(),
//                playerMixin.getDonationAmount(),
//                playerMixin.getDonatorStatus().getName()
//            );
//        }
//
//        Text text = new LiteralText(message);
//
//        try {
//            Messenger.m(context.getSource().getPlayer(), text);
//        } catch (CommandSyntaxException e) {
//            server.log(message);
//        }

        return 1;
    }

    private static void sendMessageToPlayer(CommandContext<ServerCommandSource> context, String message) {
        try {
            Messenger.m(context.getSource().getPlayer(), new LiteralText(message));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
    }
}
