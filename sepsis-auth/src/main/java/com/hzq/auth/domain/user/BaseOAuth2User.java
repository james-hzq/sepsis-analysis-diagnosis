package com.hzq.auth.domain.user;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

/**
 * @author gc
 * @class com.hzq.auth.domain.user BaseOAuth2User
 * @date 2024/11/4 17:38
 * @description 基础OAuth2用户类
 */
@ToString
public abstract class BaseOAuth2User implements OAuth2User {
    private final Set<GrantedAuthority> authorities;
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;

    protected BaseOAuth2User(Collection<GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey) {
        this.authorities = (authorities != null)
                ? Collections.unmodifiableSet(new LinkedHashSet<>(sortAuthorities(authorities)))
                : Collections.unmodifiableSet(new LinkedHashSet<>(Collections.emptyList()));
        this.attributes = Collections.unmodifiableMap(new LinkedHashMap<>(attributes));
        this.nameAttributeKey = nameAttributeKey;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return this.getAttributes().get(this.nameAttributeKey).toString();
    }

    private Set<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(Comparator.comparing(GrantedAuthority::getAuthority));
        sortedAuthorities.addAll(authorities);
        return sortedAuthorities;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        BaseOAuth2User that = (BaseOAuth2User) obj;
        if (!this.getName().equals(that.getName())) {
            return false;
        }
        if (!this.getAuthorities().equals(that.getAuthorities())) {
            return false;
        }
        return this.getAttributes().equals(that.getAttributes());
    }

    @Override
    public int hashCode() {
        int result = this.getName().hashCode();
        result = 31 * result + this.getAuthorities().hashCode();
        result = 31 * result + this.getAttributes().hashCode();
        return result;
    }
}
