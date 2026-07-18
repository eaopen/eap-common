package org.openea.eap.framework.mybatis.core.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import org.openea.eap.framework.common.pojo.PageParam;
import org.openea.eap.framework.common.pojo.SortingField;
import org.openea.eap.framework.mybatis.core.enums.DbTypeEnum;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

/**
 * MyBatis 工具类
 */
public class MyBatisUtils {

    private static final String MYSQL_ESCAPE_CHARACTER = "`";

    private static final Pattern SAFE_COLUMN_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*$");

    public static <T> Page<T> buildPage(PageParam pageParam) {
        return buildPage(pageParam, null);
    }

    public static <T> Page<T> buildPage(PageParam pageParam, Collection<SortingField> sortingFields) {
        // 页码 + 数量
        Page<T> page = new Page<>(pageParam.getPageNo(), pageParam.getPageSize());
        // 排序字段
        if (!CollectionUtil.isEmpty(sortingFields)) {
            page.addOrder(sortingFields.stream().map(sortingField -> SortingField.ORDER_ASC.equals(sortingField.getOrder())
                            ? OrderItem.asc(StrUtil.toUnderlineCase(sortingField.getField()))
                            : OrderItem.desc(StrUtil.toUnderlineCase(sortingField.getField())))
                    .collect(Collectors.toList()));
        }
        return page;
    }

    /**
     * 为非分页查询补充经过校验的排序字段，避免将请求参数直接拼接到 SQL 中。
     */
    @SuppressWarnings("PatternVariableCanBeUsed")
    public static <T> void addOrder(Wrapper<T> wrapper, Collection<SortingField> sortingFields) {
        if (CollectionUtil.isEmpty(sortingFields)) {
            return;
        }
        if (wrapper instanceof QueryWrapper<T> query) {
            for (SortingField sortingField : sortingFields) {
                String columnName = buildSafeOrderColumn(sortingField.getField());
                if (columnName != null) {
                    query.orderBy(true, isAscOrder(sortingField.getOrder()), columnName);
                }
            }
            return;
        }
        if (wrapper instanceof LambdaQueryWrapper<T> lambdaQuery) {
            StringBuilder orderBy = new StringBuilder();
            for (SortingField sortingField : sortingFields) {
                String columnName = buildSafeOrderColumn(sortingField.getField());
                if (columnName == null) {
                    continue;
                }
                if (!orderBy.isEmpty()) {
                    orderBy.append(", ");
                }
                orderBy.append(columnName).append(" ").append(getOrderDirection(sortingField.getOrder()));
            }
            if (!orderBy.isEmpty()) {
                lambdaQuery.last("ORDER BY " + orderBy);
            }
            return;
        }
        throw new IllegalArgumentException("Unsupported wrapper type: " + wrapper.getClass().getName());
    }

    public static boolean isAscOrder(String order) {
        return SortingField.ORDER_ASC.equals(order);
    }

    public static String getOrderDirection(String order) {
        return isAscOrder(order) ? "ASC" : "DESC";
    }

    private static String buildSafeOrderColumn(String field) {
        String columnName = StrUtil.toUnderlineCase(field);
        return StrUtil.isNotEmpty(columnName) && SAFE_COLUMN_NAME_PATTERN.matcher(columnName).matches() ? columnName : null;
    }

    /**
     * 将拦截器添加到链中
     * 由于 MybatisPlusInterceptor 不支持添加拦截器，所以只能全量设置
     *
     * @param interceptor 链
     * @param inner       拦截器
     * @param index       位置
     */
    public static void addInterceptor(MybatisPlusInterceptor interceptor, InnerInterceptor inner, int index) {
        List<InnerInterceptor> inners = new ArrayList<>(interceptor.getInterceptors());
        inners.add(index, inner);
        interceptor.setInterceptors(inners);
    }

    /**
     * 获得 Table 对应的表名
     * <p>
     * 兼容 MySQL 转义表名 `t_xxx`
     *
     * @param table 表
     * @return 去除转移字符后的表名
     */
    public static String getTableName(Table table) {
        String tableName = table.getName();
        if (tableName.startsWith(MYSQL_ESCAPE_CHARACTER) && tableName.endsWith(MYSQL_ESCAPE_CHARACTER)) {
            tableName = tableName.substring(1, tableName.length() - 1);
        }
        return tableName;
    }

    /**
     * 构建 Column 对象
     *
     * @param tableName  表名
     * @param tableAlias 别名
     * @param column     字段名
     * @return Column 对象
     */
    public static Column buildColumn(String tableName, Alias tableAlias, String column) {
        if (tableAlias != null) {
            tableName = tableAlias.getName();
        }
        return new Column(tableName + StringPool.DOT + column);
    }

    /**
     * 跨数据库的 find_in_set 实现
     *
     * @param column 字段名称
     * @param value  查询值(不带单引号)
     * @return sql
     */
    public static String findInSet(String column, Object value) {
        // 这里不用SqlConstants.DB_TYPE，因为它是使用 primary 数据源的 url 推断出来的类型
        DbType dbType = JdbcUtils.getDbType();
        return DbTypeEnum.getFindInSetTemplate(dbType)
                .replace("#{column}", column)
                .replace("#{value}", StrUtil.toString(value));
    }

}
