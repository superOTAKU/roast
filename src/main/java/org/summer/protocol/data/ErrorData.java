package org.summer.protocol.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorData {
    private Integer errorCode;
    private List<ErrorField> errorFields;
    private Object extData;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class ErrorField {
        private String fieldName;
        private String errorMsg;
    }

    public Map<String, Object> toDataMap() {
        return null;
    }

}
