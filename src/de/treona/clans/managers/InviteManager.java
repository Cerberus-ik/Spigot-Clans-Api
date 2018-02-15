package de.treona.clans.managers;

import de.treona.clans.Clans;
import de.treona.clans.common.Clan;
import de.treona.clans.common.Invite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InviteManager {

    private List<Invite> invites;

    public InviteManager() {
        this.invites = new ArrayList<>();
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
        Clan clan = this.invites.stream().filter(invite -> invite.getTargetPlayer().equals(player) && invite.getClan().getClanName().equals(clanName)).findFirst().orElse(null).getClan();
        List<UUID> members = clan.getMembers();
        members.add(player.getUniqueId());
        Clans.updateClanMembers(clan, members);
        Player owner = Bukkit.getPlayer(clan.getOwner());
        if(owner != null){
            Bukkit.getScheduler().runTask(Clans.getPlugin(), () -> {
                owner.sendMessage(Clans.PREFIX_COLOR + " " + player.getName() + " accepted your invitation.");
            });
        }
        this.invites.removeIf(invite -> invite.getTargetPlayer().equals(player));
        return true;
    }
}
