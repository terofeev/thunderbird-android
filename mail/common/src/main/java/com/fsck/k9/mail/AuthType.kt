package com.fsck.k9.mail

enum class AuthType {
    /*
     * The names of these authentication types are saved as strings when
     * settings are exported and are also saved as part of the Server URI stored
     * in the account settings.
     *
     * PLAIN and CRAM_MD5 originally referred to specific SASL authentication
     * mechanisms. Their meaning has since been broadened to mean authentication
     * with unencrypted and encrypted passwords, respectively. Nonetheless,
     * their original names have been retained for backward compatibility with
     * user settings.
     */
    PLAIN,
    CRAM_MD5,
    EXTERNAL,

    /**
     * XOAUTH2 is an OAuth2.0 protocol designed/used by GMail.
     *
     * https://developers.google.com/gmail/xoauth2_protocol#the_sasl_xoauth2_mechanism
     */
    XOAUTH2,
    NONE,
}
