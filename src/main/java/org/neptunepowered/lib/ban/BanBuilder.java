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
package org.neptunepowered.lib.ban;

import net.canarymod.api.PlayerReference;

import java.net.InetAddress;
import java.util.Date;

public interface BanBuilder {

    /**
     * Sets the player to be banned.
     *
     * <p>This can only be done if the {@link BanType} has been set to {@link BanType#PLAYER}.</p>
     *
     * @param player The player
     * @return This builder
     */
    BanBuilder user(PlayerReference player);

    /**
     * Sets the IP address to be banned.
     *
     * <p>This can only be done if the {@link BanType} has been set to {@link BanType#IP}.</p>
     *
     * @param address The IP address
     * @return This builder
     */
    BanBuilder address(InetAddress address);

    /**
     * Sets the type of the ban.
     *
     * @param type The type to be set
     * @return This builder
     */
    BanBuilder type(BanType type);

    /**
     * Sets the reason for the ban.
     *
     * @param reason The reason
     * @return This builder
     */
    BanBuilder reason(String reason);

    /**
     * Sets the date that the ban starts.
     *
     * @param date The start date
     * @return This builder
     */
    BanBuilder startDate(Date date);

    /**
     * Sets the expiration date of the ban, or removes it.
     *
     * @param date The expiration date, or null in order to remove it
     * @return This builder
     */
    BanBuilder expirationDate(Date date);

    /**
     * Creates a new Ban from this builder.
     *
     * @return A new Ban
     */
    Ban build();
}
