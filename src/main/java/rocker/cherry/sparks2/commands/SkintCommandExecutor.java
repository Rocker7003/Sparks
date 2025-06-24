package rocker.cherry.sparks2.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rocker.cherry.sparks2.skint.SkintItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class SkintCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("Эту команду может использовать только игрок.").color(NamedTextColor.RED));
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(Component.text("Использование: /skint <тип_скинта> [игрок] [количество]").color(NamedTextColor.YELLOW));
            player.sendMessage(Component.text("Использование: /skint <тип_скинта> [количество]").color(NamedTextColor.YELLOW));
            player.sendMessage(Component.text("Доступные типы: crumb, shard, crystal, fragment, honed, skintonite").color(NamedTextColor.YELLOW));
            return true;
        }

        String skintType = args[0].toLowerCase();
        Player targetPlayer = player;
        int amount = 1;

        if (args.length >= 2) {
            try {
                amount = Integer.parseInt(args[1]);
                if (amount <= 0) {
                    player.sendMessage(Component.text("Количество должно быть положительным числом.").color(NamedTextColor.RED));
                    return true;
                }

                if (args.length >= 3) {
                    targetPlayer = Bukkit.getPlayer(args[2]);
                    if (targetPlayer == null) {
                        player.sendMessage(Component.text("Игрок '" + args[2] + "' не найден.").color(NamedTextColor.RED));
                        return true;
                    }
                }
            } catch (NumberFormatException e) {
                targetPlayer = Bukkit.getPlayer(args[1]);
                if (targetPlayer == null) {
                    player.sendMessage(Component.text("Игрок '" + args[1] + "' не найден.").color(NamedTextColor.RED));
                    return true;
                }
                if (args.length >= 3) {
                    try {
                        amount = Integer.parseInt(args[2]);
                        if (amount <= 0) {
                            player.sendMessage(Component.text("Количество должно быть положительным числом.").color(NamedTextColor.RED));
                            return true;
                        }
                    } catch (NumberFormatException ex) {
                        player.sendMessage(Component.text("Неверное количество: " + args[2]).color(NamedTextColor.RED));
                        return true;
                    }
                }
            }
        }

        ItemStack skintItemStack = null;

        switch (skintType) {
            case "crumb":
                skintItemStack = SkintItem.SKINT_CRUMB.createItemStack();
                break;
            case "shard":
                skintItemStack = SkintItem.SKINT_SHARD.createItemStack();
                break;
            case "crystal":
                skintItemStack = SkintItem.SKINT_CRYSTAL.createItemStack();
                break;
            case "fragment":
                skintItemStack = SkintItem.SKINT_FRAGMENT.createItemStack();
                break;
            case "honed":
                skintItemStack = SkintItem.SKINT_HONED.createItemStack();
                break;
            case "skintonite":
                skintItemStack = SkintItem.SKINTONITE.createItemStack();
                break;
            default:
                player.sendMessage(Component.text("Неизвестный тип скинта: " + skintType).color(NamedTextColor.RED));
                player.sendMessage(Component.text("Доступные типы: crumb, shard, crystal, fragment, honed, skintonite").color(NamedTextColor.YELLOW));
                return true;
        }

        if (skintItemStack != null) {
            skintItemStack.setAmount(amount);
            targetPlayer.getInventory().addItem(skintItemStack);
            player.sendMessage(Component.text("Выдано " + amount + " " + LegacyComponentSerializer.legacyAmpersand().serialize(skintItemStack.getItemMeta().displayName()) + " игроку " + targetPlayer.getName() + ".").color(NamedTextColor.GREEN));
            if (!player.equals(targetPlayer)) {
                targetPlayer.sendMessage(Component.text("Вы получили " + amount + " " + LegacyComponentSerializer.legacyAmpersand().serialize(skintItemStack.getItemMeta().displayName()) + " от " + player.getName() + ".").color(NamedTextColor.GREEN));
            }
        } else {
            player.sendMessage(Component.text("Произошла ошибка при создании предмета.").color(NamedTextColor.RED));
        }

        return true;
    }
}