package com.hzq.system.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @author hua
 * @className com.hzq.system.vo RouterVo
 * @date 2024/8/31 10:09
 * @description 传递给前端的路由对象
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo {
    /**
     * 路由名字
     */
    private final String name;

    /**
     * 路由地址
     */
    private final String path;

    /**
     * 是否隐藏路由，当设置 true 的时候该路由不会再侧边栏出现
     */
    private final boolean hidden;

    /**
     * 重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
     */
    private final String redirect;

    /**
     * 组件地址
     */
    private final String component;

    /**
     * 路由参数：如 {"id": 1, "name": "ry"}
     */
    private final String query;

    /**
     * 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
     */
    private final Boolean alwaysShow;

    /**
     * 其他元素
     */
    private final MetaVo meta;

    /**
     * 子路由
     */
    private final List<RouterVo> children;

    private RouterVo(Builder builder) {
        this.name = builder.name;
        this.path = builder.path;
        this.hidden = builder.hidden;
        this.redirect = builder.redirect;
        this.component = builder.component;
        this.query = builder.query;
        this.alwaysShow = builder.alwaysShow;
        this.meta = builder.meta;
        this.children = builder.children;
    }

    public static class Builder {
        private String name;
        private String path;
        private boolean hidden;
        private String redirect;
        private String component;
        private String query;
        private Boolean alwaysShow;
        private MetaVo meta;
        private List<RouterVo> children;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setHidden(boolean hidden) {
            this.hidden = hidden;
            return this;
        }

        public Builder setRedirect(String redirect) {
            this.redirect = redirect;
            return this;
        }

        public Builder setComponent(String component) {
            this.component = component;
            return this;
        }

        public Builder setQuery(String query) {
            this.query = query;
            return this;
        }

        public Builder setAlwaysShow(boolean alwaysShow) {
            this.alwaysShow = alwaysShow;
            return this;
        }

        public Builder setMetaVo(MetaVo metaVo) {
            this.meta = metaVo;
            return this;
        }

        public Builder setChildren(List<RouterVo> children) {
            this.children = children;
            return this;
        }

        public RouterVo build() {
            return new RouterVo(this);
        }
    }

    public String getName() {return name;}
    public String getPath() {
        return path;
    }
    public boolean getHidden() {
        return hidden;
    }
    public String getRedirect() {
        return redirect;
    }

    public String getComponent() {
        return component;
    }
    public String getQuery() {return query;}
    public Boolean getAlwaysShow() {return alwaysShow;}
    public MetaVo getMeta() {return meta;}
    public List<RouterVo> getChildren() {return children;}
}
