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
