package org.openea.eap.framework.datasource.dynamicds;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 自定义动态数据源类
 *
 */
@Slf4j
public class EapDynamicRoutingDataSource extends DynamicRoutingDataSource {

    public EapDynamicRoutingDataSource(){
        this(null);
    }

    public EapDynamicRoutingDataSource(List<DynamicDataSourceProvider> providers) {
        super(providers);
    }
}
