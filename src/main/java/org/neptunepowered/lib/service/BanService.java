/*
 * This file is part of NeptuneLib, licensed under the MIT License (MIT).
 *
 * Copyright (c) Jamie Mansfield <https://github.com/jamierocks>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.neptunepowered.lib.service;

import com.google.common.base.Optional;
import net.canarymod.Canary;
import net.canarymod.api.PlayerReference;
import org.neptunepowered.lib.ban.Ban;
import org.neptunepowered.lib.ban.BanType;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * Represents the service with which to ban users.
 */
public class BanService {

    /**
     * Gets all bans registered.
     *
     * @return All registered bans
     */
    public Collection<Ban> getBans() {
        Collection<Ban> bans = Collections.EMPTY_LIST;
        for (net.canarymod.bansystem.Ban ban : Canary.bans().getAllBans(net.canarymod.bansystem.BanType.UUID)) {
            bans.add(new CanaryPlayerBan(ban));
        }
        for (net.canarymod.bansystem.Ban ban : Canary.bans().getAllBans(net.canarymod.bansystem.BanType.IP)) {
            bans.add(new CanaryIpBan(ban));
        }
        return bans;
    }

    /**
     * Gets all player bans registered.
     *
     * @return All registered player bans
     */
    public Collection<Ban.Player> getPlayerBans() {
        Collection<Ban.Player> playerBans = Collections.EMPTY_LIST;
        for (net.canarymod.bansystem.Ban ban : Canary.bans().getAllBans(net.canarymod.bansystem.BanType.UUID)) {
            playerBans.add(new CanaryPlayerBan(ban));
        }
        return playerBans;
    }

    /**
     * Gets all IP bans registered.
     *
     * @return All registered IP bans
     */
    public Collection<Ban.Ip> getIpBans() {
        Collection<Ban.Ip> ipBans = Collections.EMPTY_LIST;
        for (net.canarymod.bansystem.Ban ban : Canary.bans().getAllBans(net.canarymod.bansystem.BanType.IP)) {
            ipBans.add(new CanaryIpBan(ban));
        }
        return ipBans;
    }

    /**
     * Checks if a player has any bans.
     *
     * @param player The player
     * @return True if the user has any bans, false otherwise
     */
    public boolean isBanned(PlayerReference player) {
        return Canary.bans().isBanned(player.getName());
    }

    /**
     * Checks if an IP has any bans.
     *
     * @param address The address
     * @return True if the address has any bans, false otherwise
     */
    public boolean isBanned(InetAddress address) {
        return Canary.bans().isIpBanned(address.getCanonicalHostName());
    }

    /**
     * Pardons a player, or removes all their bans.
     *
     * @param player The player
     */
    public void pardon(PlayerReference player) {
        Canary.bans().unban(player.getName());
    }

    /**
     * Pardons an IP address, or removes all the bans against that IP.
     *
     * @param address The IP address
     */
    public void pardon(InetAddress address) {
        Canary.bans().unban(address.getCanonicalHostName());
    }

    /**
     * Pardons a ban.
     *
     * @param ban The ban
     */
    public void pardon(Ban ban) {
        switch (ban.getBanType()) {
            case PLAYER:
                Canary.bans().unban(((Ban.Player) ban).getPlayer().getName());
                break;
            case IP:
                Canary.bans().unban(((Ban.Ip) ban).getAddress().getCanonicalHostName());
                break;
        }
    }

    /**
     * Adds a ban.
     *
     * @param ban The ban to put on the user
     */
    public void ban(Ban ban) {
        net.canarymod.bansystem.Ban cBan = new net.canarymod.bansystem.Ban();
        cBan.setIssuedDate(ban.getStartDate().getTime());
        cBan.setReason(ban.getReason());
        cBan.setBanType(ban.getBanType().getCanaryType());
        if (ban.getExpirationDate().isPresent()) {
            cBan.setExpiration(ban.getExpirationDate().get().getTime());
        }
        if (ban instanceof Ban.Ip) {
            cBan.setIp(((Ban.Ip) ban).getAddress().getCanonicalHostName());
        } else if (ban instanceof Ban.Player) {
            cBan.setSubject(((Ban.Player) ban).getPlayer().getName());
        }
        Canary.bans().issueBan(cBan);
    }

    private class CanaryBan implements Ban {

        private final net.canarymod.bansystem.Ban ban;

        private CanaryBan(net.canarymod.bansystem.Ban ban) {
            this.ban = ban;
        }

        @Override
        public BanType getBanType() {
            return BanType.fromCanaryType(ban.getBanType());
        }

        @Override
        public String getReason() {
            return ban.getReason();
        }

        @Override
        public Date getStartDate() {
            return new Date(ban.getIssuedDate());
        }

        @Override
        public Optional<Date> getExpirationDate() {
            if (ban.getExpiration() != -1) {
                return Optional.of(new Date(ban.getExpiration()));
            }
            return Optional.absent();
        }

        @Override
        public boolean isIndefinite() {
            return ban.getExpiration() == -1;
        }

        protected net.canarymod.bansystem.Ban getHandle() {
            return ban;
        }
    }

    private class CanaryIpBan extends CanaryBan implements Ban.Ip {

        private CanaryIpBan(net.canarymod.bansystem.Ban ban) {
            super(ban);
        }

        @Override
        public InetAddress getAddress() {
            try {
                return InetAddress.getByName(getHandle().getIp());
            } catch (UnknownHostException e) {
                return null;
            }
        }
    }

    private class CanaryPlayerBan extends CanaryBan implements Ban.Player {

        private CanaryPlayerBan(net.canarymod.bansystem.Ban ban) {
            super(ban);
        }

        @Override
        public PlayerReference getPlayer() {
            return Canary.getServer().getPlayer(getHandle().getBanningPlayer());
        }
    }
}
