package org.openea.eap.extj.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Data
public class Pagination extends Page{
    @Schema(name = "每页条数",example = "20")
    private long pageSize=20;
    @Schema(name = "排序类型")
    private String sort="desc";
    @Schema(name = "排序列")
    private String sidx="";
    @Schema(name = "当前页数",example = "1")
    private long currentPage=1;



    @Schema(accessMode = READ_ONLY)
    @JsonIgnore
    private long total;
    @Schema(accessMode = READ_ONLY)
    @JsonIgnore
    private long records;

    public <T> List<T> setData(List<T> data, long records) {
        this.total = records;
        return data;
    }
}
