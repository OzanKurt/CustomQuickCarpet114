package quickcarpet.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import quickcarpet.interfaces.PlayerEntityMixinInterface;
import quickcarpet.settings.Settings;
import quickcarpet.utils.DonatorStatus;
import quickcarpet.utils.Messenger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static quickcarpet.utils.Messenger.m;

public class DonationCommand {
    // TODO: allow any order like execute
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literalargumentbuilder = literal("donation")
//            .requires(s -> s.hasPermissionLevel(Settings.commandPlayer))
            .then(
                argument("player", word())
                    .suggests((c, b) -> CommandSource.suggestMatching(getPlayers(c.getSource()), b))
                    .executes(DonationCommand::getDonationStatus)
                    .then(
                        argument("amount", integer()).executes(DonationCommand::addDonationAmount)
                    )
            );

        dispatcher.register(literalargumentbuilder);
    }

    /**
     * Get the player names to suggest.
     *
     * @param source
     * @return
     */
    private static Collection<String> getPlayers(ServerCommandSource source) {
        Set<String> players = new HashSet<>();

        players.addAll(source.getPlayerNames());

        return players;
    }

    private static int getDonationStatus(CommandContext<ServerCommandSource> context) {
        String playerName = getString(context, "player");

        MinecraftServer server = context.getSource().getMinecraftServer();
        ServerPlayerEntity player = server.getPlayerManager().getPlayer(playerName);
        PlayerEntityMixinInterface playerMixin = (PlayerEntityMixinInterface) server.getPlayerManager().getPlayer(playerName);

        String message = null;

        if (playerMixin.getDonatorStatus() == DonatorStatus.NON_DONATOR) {
            message = String.format(
                "The player %s has not donated yet.",
                player.getName().asString()
            );
        } else {
            message = String.format(
                "The player %s has donated %d$ so far and has the donator status %s.",
                player.getName().asString(),
                playerMixin.getDonationAmount(),
                playerMixin.getDonatorStatus().getName()
            );
        }

        Text text = new LiteralText(message);

        try {
            Messenger.m(context.getSource().getPlayer(), text);
        } catch (CommandSyntaxException e) {
            server.log(message);
        }

        return 1;
    }

    private static int addDonationAmount(CommandContext<ServerCommandSource> context) {
        String playerName = getString(context, "player");
        int amount = getInteger(context, "amount");

        MinecraftServer server = context.getSource().getMinecraftServer();

        PlayerEntityMixinInterface player = (PlayerEntityMixinInterface) server.getPlayerManager().getPlayer(playerName);

        player.addDonationAmount(amount);

        return 1;
    }
}
