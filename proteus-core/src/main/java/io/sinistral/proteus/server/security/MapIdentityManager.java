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

    @Override
    public Account verify(Account account)
    {
        // An existing account so for testing assume still valid.
        return account;
    }

    @Override
    public Account verify(Credential credential)
    {
        // Raw credentials are not verifiable without an identity id.
        return null;
    }

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
        private static final long serialVersionUID = -8234851531206339721L;
        private final Principal principal;

        public UserAccount(String id)
        {
            principal = new Principal()
            {
                @Override
                public String getName()
                {
                    return id;
                }
            };
        }

        @Override
        public Principal getPrincipal()
        {
            return principal;
        }

        @Override
        public Set<String> getRoles()
        {
            return Collections.emptySet();
        }
    }
}



