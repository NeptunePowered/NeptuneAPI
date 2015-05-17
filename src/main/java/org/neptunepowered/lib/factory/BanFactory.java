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
package org.neptunepowered.lib.factory;

import com.google.common.base.Optional;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.canarymod.Canary;
import net.canarymod.api.PlayerReference;
import org.neptunepowered.lib.ban.Ban;
import org.neptunepowered.lib.ban.BanBuilder;
import org.neptunepowered.lib.ban.BanType;

import java.net.InetAddress;
import java.util.Date;

public class BanFactory {

    private static BiMap<BanType, net.canarymod.bansystem.BanType> map =
            ImmutableBiMap.<BanType, net.canarymod.bansystem.BanType>builder()
                    .put(BanType.PLAYER, net.canarymod.bansystem.BanType.UUID)
                    .put(BanType.IP, net.canarymod.bansystem.BanType.IP)
                    .build();

    protected BanFactory() { }

    public BanBuilder builder() {
        return new BanBuilder() {
            PlayerReference player;
            InetAddress address = null;
            BanType banType = BanType.PLAYER;
            String reason;
            Date startDate;
            Date expirationDate = null;

            @Override
            public BanBuilder user(PlayerReference player) {
                this.player = player;
                return this;
            }

            @Override
            public BanBuilder address(InetAddress address) {
                this.address = address;
                return this;
            }

            @Override
            public BanBuilder type(BanType type) {
                this.banType = type;
                return this;
            }

            @Override
            public BanBuilder reason(String reason) {
                this.reason = reason;
                return this;
            }

            @Override
            public BanBuilder startDate(Date date) {
                this.startDate = date;
                return this;
            }

            @Override
            public BanBuilder expirationDate(Date date) {
                this.expirationDate = date;
                return this;
            }

            @Override
            public Ban build() {
                switch (banType) {
                    case IP:
                        return new Ban.IP() {
                            @Override
                            public InetAddress getAddress() {
                                return address;
                            }

                            @Override
                            public BanType getBanType() {
                                return banType;
                            }

                            @Override
                            public String getReason() {
                                return reason;
                            }

                            @Override
                            public Date getStartDate() {
                                return startDate;
                            }

                            @Override
                            public Optional<Date> getExpirationDate() {
                                return Optional.of(expirationDate);
                            }

                            @Override
                            public boolean isIndefinite() {
                                return expirationDate == null;
                            }
                        };
                    default:
                        return new Ban.Player() {
                            @Override
                            public PlayerReference getPlayer() {
                                return player;
                            }

                            @Override
                            public BanType getBanType() {
                                return banType;
                            }

                            @Override
                            public String getReason() {
                                return reason;
                            }

                            @Override
                            public Date getStartDate() {
                                return startDate;
                            }

                            @Override
                            public Optional<Date> getExpirationDate() {
                                return Optional.of(expirationDate);
                            }

                            @Override
                            public boolean isIndefinite() {
                                return expirationDate == null;
                            }
                        };
                }
            }
        };
    }

    public void issueBan(Ban ban) {
        net.canarymod.bansystem.Ban cBan = new net.canarymod.bansystem.Ban();
        cBan.setIssuedDate(ban.getStartDate().getTime());
        cBan.setReason(ban.getReason());
        cBan.setBanType(map.get(ban.getBanType()));
        if (ban.getExpirationDate().isPresent()) {
            cBan.setExpiration(ban.getExpirationDate().get().getTime());
        }
        if (ban instanceof Ban.IP) {
            cBan.setIp(((Ban.IP) ban).getAddress().getCanonicalHostName());
        } else if (ban instanceof Ban.Player) {
            cBan.setSubject(((Ban.Player) ban).getPlayer().getName());
        }
        Canary.bans().issueBan(cBan);
    }
}
