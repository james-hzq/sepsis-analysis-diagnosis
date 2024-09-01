package com.hzq.system.pojo.vo;
import com.hzq.core.constant.Constants;
import org.apache.commons.lang3.StringUtils;

/**
 * @author hua
 * @className com.hzq.system.vo MetaVo
 * @date 2024/8/31 10:21
 * @description 定义路由显示信息
 */
public class MetaVo {
    /**
     * 设置该路由在侧边栏和面包屑中展示的名字
     */
    private final String title;

    /**
     * 设置该路由的图标，对应路径src/assets/icons/svg
     */
    private final String icon;

    /**
     * 设置为true，则不会被 <keep-alive>缓存
     */
    private final boolean noCache;

    /**
     * 内链地址（http(s)://开头）
     */
    private final String link;

    private MetaVo(Builder builder) {
        this.title = builder.title;
        this.icon = builder.icon;
        this.noCache = builder.noCache;
        this.link = builder.link;
    }

    public static class Builder {
        private String title;
        private String icon;
        private boolean noCache;
        private String link;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setIcon(String icon) {
            this.icon = icon;
            return this;
        }

        public Builder setNoCache(boolean noCache) {
            this.noCache = noCache;
            return this;
        }

        public Builder setLink(String link) {
            if (StringUtils.startsWithAny(link, Constants.HTTP_PREFIX, Constants.HTTPS_PREFIX)) {
                this.link = link;
            }
            return this;
        }
        public MetaVo build() {
            return new MetaVo(this);
        }
    }

    public boolean isNoCache() {
        return noCache;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }

    public String getLink() {
        return link;
    }
}
