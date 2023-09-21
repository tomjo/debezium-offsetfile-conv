package net.tomjo.spring.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.*;

@RequiredArgsConstructor
public class KeycloakRealmRolesGrantedAuthoritiesMapper implements GrantedAuthoritiesMapper {

    public static final String REALM_ACCESS_CLAIM = "realm_access";
    public static final String ROLES_CLAIM = "roles";

    // prefix as defined by org.springframework.security.authorization.AuthorityAuthorizationManager
    public static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
        authorities.forEach(authority -> {
            if (authority instanceof OidcUserAuthority oidcUserAuthority) {
                mappedAuthorities.addAll(extractAuthorities(oidcUserAuthority.getIdToken().getClaims()));
                mappedAuthorities.addAll(extractAuthorities(oidcUserAuthority.getUserInfo().getClaims()));
            } else if (authority instanceof OAuth2UserAuthority oauth2UserAuthority) {
                mappedAuthorities.addAll(extractAuthorities(oauth2UserAuthority.getAttributes()));
            }
        });
        return mappedAuthorities;
    }

    private static Collection<GrantedAuthority> extractAuthorities(Map<String, Object> claims) {
        Object realmAccessClaim = claims.get(REALM_ACCESS_CLAIM);
        if (realmAccessClaim instanceof Map<?, ?> realmAccess) {
            Object rolesClaim = realmAccess.get(ROLES_CLAIM);
            if (rolesClaim instanceof List<?> roles) {
                return roles.stream()
                        .map(roleName -> ROLE_PREFIX + roleName)
                        .map(SimpleGrantedAuthority::new)
                        .map(GrantedAuthority.class::cast)
                        .toList();
            }
        }
        return List.of();
    }
}
