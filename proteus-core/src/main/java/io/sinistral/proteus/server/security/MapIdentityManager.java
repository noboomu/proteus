/**
 *
 */
package io.sinistral.proteus.server.security;

import io.undertow.security.idm.Account;
import io.undertow.security.idm.Credential;
import io.undertow.security.idm.IdentityManager;
import io.undertow.security.idm.PasswordCredential;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Identity manager backed by an in-memory username-to-password map.
 *
 * @author jbauer
 */
public class MapIdentityManager implements IdentityManager
{
    /** The identities. */
    private final Map<String, char[]> identities;

    /**
     * Creates a manager over the given identity map.
     *
     * @param identities map of username to expected password
     */
    public MapIdentityManager(final Map<String, char[]> identities)
    {
        this.identities = identities;
    }

    /**
     * Accepts an existing account as still valid.
     *
     * @param account the account to verify
     * @return the same account
     */
    @Override
    public Account verify(Account account)
    {
        // An existing account so for testing assume still valid.
        return account;
    }

    /**
     * Rejects credentials that carry no identity id.
     *
     * @param credential the credential to verify
     * @return null, raw credentials cannot be verified
     */
    @Override
    public Account verify(Credential credential)
    {
        // Raw credentials are not verifiable without an identity id.
        return null;
    }

    /**
     * Verifies a password credential for a named identity.
     *
     * @param id the identity id
     * @param credential the password credential
     * @return the account on success, null otherwise
     */
    @Override
    public Account verify(String id, Credential credential)
    {
        Account account = getAccount(id);

        if ((account != null) && verifyCredential(account, credential)) {
            return account;
        }

        return null;
    }

    private boolean verifyCredential(Account account, Credential credential)
    {
        if (credential instanceof PasswordCredential) {
            char[] password = ((PasswordCredential) credential).getPassword();
            char[] expectedPassword = identities.get(account.getPrincipal().getName());

            return Arrays.equals(password, expectedPassword);
        }

        return false;
    }

    private Account getAccount(final String id)
    {
        if (identities.containsKey(id)) {
            return new UserAccount(id);
        }

        return null;
    }

    private static class UserAccount implements Account
    {
        /** The serial version uid. */
        private static final long serialVersionUID = -8234851531206339721L;
        /** The principal. */
        private final Principal principal;

        public UserAccount(String id)
        {
            principal = new Principal()
            {
                /**
                 * Returns the name.
                 *
                 * @return the name, or null when unset
                 */
                @Override
                public String getName()
                {
                    return id;
                }
            };
        }

        /**
         * Returns the principal.
         *
         * @return the principal, or null when unset
         */
        @Override
        public Principal getPrincipal()
        {
            return principal;
        }

        /**
         * Returns the roles.
         *
         * @return the roles, never null
         */
        @Override
        public Set<String> getRoles()
        {
            return Collections.emptySet();
        }
    }
}



