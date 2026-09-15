package org.openea.eap.framework.mq.rabbitmq.core;

import com.fasterxml.jackson.databind.JavaType;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.util.ClassUtils;

import java.util.List;

/**
 * 支持“根包覆盖子包”语义的 Jackson 类型映射器。
 *
 * <p><b>背景：</b>Spring AMQP 自 3.2.11 起把无参 {@code Jackson2JsonMessageConverter} 的默认可信包
 * 由 {@code *} 收紧为 {@code [java.util, java.lang]}（Spring Boot 3.5.x 对应 spring-amqp 3.2.12）。
 * 业务应用升级到 Spring Boot 3.5.16 后，消息头 {@code __TypeId__} 指向的业务类不在可信范围内，
 * 消费端反序列化会抛 {@code IllegalArgumentException}，且多为运行期才暴露，排查成本高。
 *
 * <p><b>修改逻辑：</b>在父类执行可信包校验之前，把消息头里出现的类型所属的<b>精确包名</b>动态登记进
 * 父类可信集合，从而让配置的根包（如 {@code org.openea.eap}）覆盖其下所有子包。
 *
 * <p><b>修改原因：</b>{@code DefaultJackson2JavaTypeMapper#isTrustedPackage} 只做“包名精确相等”判断
 * （{@code packageName.equals(trustedPackage)}），既不支持父包前缀，也不支持
 * {@code org.openea.eap.*} 这类通配写法。若逐个枚举精确包名，则“每新增一个消息包就要改一次配置、
 * 漏改就再次反序列化失败”，容易复发，故用本类把语义统一成“根包覆盖子包”。
 *
 * <p><b>安全边界：</b>只有落在 {@code trustedRoots} 之下（含根本身）的类型才会被登记，
 * 等价于信任该根包全量，仍明显窄于信任 {@code *}（信任全部）。
 * 父类自带的 {@code java.util} / {@code java.lang} 可信包保持不变。
 *
 * <p><b>用法：</b>
 * <pre>{@code
 * @Bean
 * @Primary
 * public Jackson2JsonMessageConverter rabbitMessageConverter() {
 *     Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
 *     converter.setJavaTypeMapper(new TrustedRootJackson2JavaTypeMapper("org.openea.eap"));
 *     return converter;
 * }
 * }</pre>
 * 注意：应用自定义 {@code MessageConverter} 后，starter 的默认转换器已在
 * {@code EapRabbitMQAutoConfiguration#createMessageConverter} 处加了
 * {@code @ConditionalOnMissingBean} 自动退让；若同应用内可能定义多个候选，仍建议显式标注
 * {@code @Primary}，避免 Spring Boot 用 {@code ObjectProvider#getIfUnique()} 挑选时返回 {@code null}。
 */
public class TrustedRootJackson2JavaTypeMapper extends DefaultJackson2JavaTypeMapper {

    /**
     * 受信的根包列表，命中的子包会被动态登记进父类可信集合
     */
    private final List<String> trustedRoots;

    public TrustedRootJackson2JavaTypeMapper(String... trustedRoots) {
        this.trustedRoots = List.of(trustedRoots);
    }

    /**
     * 重写类型解析入口：父类的 {@code toClass} 也委托到本方法，因此只需覆盖这一处。
     *
     * <p>根类型、内容类型、键类型三种头部都可能承载类名（容器类型会分别读写
     * {@code __TypeId__} / {@code __ContentTypeId__} / {@code __KeyTypeId__}），
     * 需要逐一登记，否则容器类型在解析内容/键类型时仍会因不可信而失败。
     */
    @Override
    public JavaType toJavaType(MessageProperties properties) {
        trustIfUnderRoot(retrieveHeaderAsString(properties, getClassIdFieldName()));
        trustIfUnderRoot(retrieveHeaderAsString(properties, getContentClassIdFieldName()));
        trustIfUnderRoot(retrieveHeaderAsString(properties, getKeyClassIdFieldName()));
        return super.toJavaType(properties);
    }

    /**
     * 若类名落在任一受信根包之下，则把它的精确包名登记进父类可信集合。
     *
     * <p>注意：这里只登记“当前这条消息实际用到的包”，不会放宽到根包以外的任何类型。
     */
    private void trustIfUnderRoot(String classId) {
        if (classId == null || classId.isEmpty()) {
            return;
        }
        String packageName = ClassUtils.getPackageName(classId);
        for (String root : trustedRoots) {
            if (isUnderRoot(packageName, root)) {
                addTrustedPackages(packageName);
                return;
            }
        }
    }

    /**
     * 判断包名是否落在受信根包之下（含根本身）。
     *
     * <p>单独抽成静态方法是为了让单测能直接锁定“前缀必须落在包分隔符上”这一边界：
     * 要求根包之后必须是 {@code .}，避免把 {@code org.openea.eapx} 误判成
     * {@code org.openea.eap} 的子包而意外放宽信任范围。
     */
    public static boolean isUnderRoot(String packageName, String root) {
        return packageName.equals(root) || packageName.startsWith(root + ".");
    }
}
