package de.treona.clans.managers;

import de.treona.clans.Clans;
import de.treona.clans.common.Clan;
import de.treona.clans.common.Invite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InviteManager {

    private final List<Invite> invites;
    private final JavaPlugin javaPlugin;

    public InviteManager(JavaPlugin javaPlugin) {
        this.invites = new ArrayList<>();
        this.javaPlugin = javaPlugin;
    }

    public void sendInvite(Invite invite){
        if(!this.invites.contains(invite)){
            invite.getTargetPlayer().sendMessage(Clans.PREFIX_COLOR + " You got invited to: " + ChatColor.DARK_PURPLE  + invite.getClan().getClanName() + ChatColor.RESET + " by: " + Bukkit.getOfflinePlayer(invite.getClan().getOwner()).getName());
            invite.getTargetPlayer().sendMessage(Clans.PREFIX_COLOR + " You can accept the invite with: " + ChatColor.GRAY + "/clans join " + invite.getClan().getClanName());
            this.invites.add(invite);
        }
    }

    public boolean hasInvite(Player player){
        return this.invites.stream().anyMatch(invite -> invite.getTargetPlayer().equals(player));
    }

    public boolean accept(Player player, String clanName){
        if(!this.hasInvite(player)){
            return false;
        }
        if(this.invites.stream().noneMatch(invite -> invite.getClan().getClanName().equals(clanName))){
            return false;
        }
        Invite invite = this.invites.stream()
                .filter(streamInvite -> streamInvite.getTargetPlayer().equals(player)
                        && streamInvite.getClan().getClanName().equals(clanName))
                .findFirst()
                .orElse(null);
        if(invite == null){
            return false;
        }
        Clan clan = invite.getClan();
        List<UUID> members = clan.getMembers();
        members.add(player.getUniqueId());
        Clans.updateClanMembers(clan, members);
        Player owner = Bukkit.getPlayer(clan.getOwner());
        if(owner != null){
            Bukkit.getScheduler().runTask(this.javaPlugin, () -> owner.sendMessage(Clans.PREFIX_COLOR + " " + player.getName() + " accepted your invitation."));
        }
        this.invites.removeIf(streamInvite -> streamInvite.getTargetPlayer().equals(player));
        return true;
    }
}
