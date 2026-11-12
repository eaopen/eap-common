package org.openea.eap.framework.common.util.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import org.junit.jupiter.api.Test;
import org.openea.eap.framework.common.pojo.CommonResult;

import static org.junit.jupiter.api.Assertions.*;

class FastjsonSecurityCompatibilityTest {

    @Test
    void preservesDictionaryAndArrayRoundTrip() {
        JSONObject value = JSON.parseObject("{\"label\":\"中文\",\"value\":7,\"children\":[{\"label\":\"子项\"}]}");
        JSONObject restored = JSON.parseObject(JSON.toJSONString(value));
        assertEquals("中文", restored.getString("label"));
        assertEquals(7, restored.getIntValue("value"));
        assertEquals("子项", restored.getJSONArray("children").getJSONObject(0).getString("label"));
    }

    @Test
    void preservesCommonResultIgnoredCheckedData() {
        // Serializing an error must not invoke getCheckedData(), which would throw.
        JSONObject value = JSON.parseObject(JSON.toJSONString(CommonResult.error(400, "invalid")));
        assertEquals(400, value.getIntValue("code"));
        assertEquals("invalid", value.getString("msg"));
        assertFalse(value.containsKey("checkedData"));
    }

    @Test
    void rejectsUrlSpecialTypeNamesBeforeResolution() {
        ParserConfig config = new ParserConfig();
        // Inert, non-resolvable names exercise both special-character guards without network access.
        for (String name : new String[] {"example.Invalid:Type", "example.Invalid!Type"}) {
            assertThrows(JSONException.class,
                    () -> config.checkAutoType(name, Object.class, Feature.SupportAutoType.mask), name);
        }
    }
}
