package org.openea.eap.extj.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Page {
    @Schema(name = "关键字")
    protected String keyword="";
}
