/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.common.model;

import java.io.Serializable;

/**
 * 通用链接项 ——「名称 + 地址 + 备注」的组合，
 * 如通知上的跳转（查看详情 → /order/1）、审批动作（同意 → /approval/1/accept）。
 *
 * <p>地址是统一资源标识：Web 页面路由 / URL、APP deeplink、小程序 page 路径等，
 * 由使用方按目标端翻译为各端的物理跳转参数。</p>
 *
 * @author Kahle
 */
public class Link implements Serializable {

    /**
     * 以名称与地址创建链接项。
     *
     * @param name 名称
     * @param addr 地址
     * @return 链接项
     */
    public static Link of(String name, String addr) {

        return new Link().setName(name).setAddr(addr);
    }

    /**
     * 名称：按钮文案 / 显示名，如 "查看详情"、"同意"、"忽略"；
     * 作为主跳转时可空，展示端回退为「查看」。
     */
    private String name;
    /**
     * 地址（统一资源标识）：Web 页面路由 / URL、APP deeplink、小程序 page 路径。
     */
    private String addr;
    /**
     * 备注：补充说明（如按钮的悬浮提示），可空。
     */
    private String remark;

    public String getName() {

        return name;
    }

    public Link setName(String name) {

        this.name = name;
        return this;
    }

    public String getAddr() {

        return addr;
    }

    public Link setAddr(String addr) {

        this.addr = addr;
        return this;
    }

    public String getRemark() {

        return remark;
    }

    public Link setRemark(String remark) {

        this.remark = remark;
        return this;
    }

}
