/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.notification.constant;

/**
 * 接收人类型常量 —— {@link kunlun.notification.model.Notification#getTargetType()} 的取值域，
 * 与 {@code targetIds}、{@code excludedUserIds} 配合圈定接收范围。
 *
 * @author Kahle
 * @see kunlun.notification.model.Notification
 */
public final class TargetType {

    /**
     * 指定用户：{@code targetIds} 为用户标识列表（默认值）。
     */
    public static final String USER = "USER";
    /**
     * 部门：{@code targetIds} 为部门标识，是否包含子部门由消费者实现决定。
     */
    public static final String DEPARTMENT = "DEPARTMENT";
    /**
     * 公司 / 租户：{@code targetIds} 为公司标识，通常配合 {@code excludedUserIds} 使用。
     */
    public static final String COMPANY = "COMPANY";
    /**
     * 用户组 / 角色：{@code targetIds} 为组或角色标识。
     */
    public static final String GROUP = "GROUP";
    /**
     * 全员：{@code targetIds} 可为空，配合 {@code excludedUserIds} 排除个别用户。
     */
    public static final String ALL = "ALL";

    private TargetType() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }
}
