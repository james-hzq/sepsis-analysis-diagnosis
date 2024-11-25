package com.hzq.auth.login.user;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

/**
 * @author gc
 * @class com.hzq.auth.domain.user BaseOAuth2User
 * @date 2024/11/4 17:38
 * @description 基础OAuth2用户类
 */
@Getter
@ToString
public class BaseOAuth2User implements OAuth2User {
    // 授权服务器可访问第三方客户端的权限
    private final Set<GrantedAuthority> authorities;
    // 用户详细信息
    private final Map<String, Object> attributes;
    // 用户名
    private final String nameAttributeKey;
    // token
    private final OAuth2AccessToken accessToken;

    public BaseOAuth2User(Collection<GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, OAuth2AccessToken accessToken) {
        this.authorities = authorities != null
                ? Collections.unmodifiableSet(new LinkedHashSet<>(sortAuthorities(authorities)))
                : Collections.unmodifiableSet(new LinkedHashSet<>(Collections.emptyList()));
        this.attributes = Collections.unmodifiableMap(new LinkedHashMap<>(attributes));
        this.nameAttributeKey = nameAttributeKey;
        this.accessToken = accessToken;
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
