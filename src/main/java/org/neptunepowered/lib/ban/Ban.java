package org.neptunepowered.lib.ban;

import com.google.common.base.Optional;
import net.canarymod.api.PlayerReference;

import java.net.InetAddress;
import java.util.Date;

/**
 * Represents a ban made on an object.
 */
public interface Ban {

    /**
     * Gets the type of this ban.
     *
     * @return the ban type
     */
    BanType getBanType();

    /**
     * Get the reason for the ban.
     *
     * @return the reason specified for the ban.
     */
    String getReason();

    /**
     * Gets the start date of the ban.
     *
     * @return creation date of the ban
     */
    Date getStartDate();

    /**
     * Gets the expiration date of this ban, if available.
     *
     * @return expiration date of the ban or {@link Optional#absent()}
     */
    Optional<Date> getExpirationDate();

    /**
     * Gets whether this ban is indefinitely long, e.g. has no expiration date.
     *
     * @return {@code true} if this ban has no expiration date, otherwise {@code false}
     */
    boolean isIndefinite();

    /**
     * Represents a ban made on a player.
     */
    interface Player extends Ban {

        /**
         * Gets the player this ban applies to.
         *
         * @return the player
         */
        PlayerReference getPlayer();
    }

    /**
     * Represents a ban made on an IP.
     */
    interface IP extends Ban {

        /**
         * Gets the address this ban applies to.
         *
         * @return the address
         */
        InetAddress getAddress();
    }
}
