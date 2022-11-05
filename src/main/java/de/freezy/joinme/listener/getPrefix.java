package de.freezy.joinme.listener;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.DisplayNameNode;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class getPrefix {



    //get luckperms prefix
    public static String getPrefix(ProxiedPlayer player) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());


        CachedMetaData metaData = luckPerms.getPlayerAdapter(ProxiedPlayer.class).getMetaData(player);
        //return node prefix
        return metaData.getPrefix().replace("&", "§");

    }


}
