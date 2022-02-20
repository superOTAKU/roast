package org.summer.protocol.data;

import org.summer.protocol.RemoteObjectData;

import java.util.List;

public class ErrorData implements RemoteObjectData {
    private Integer errorCode;
    private List<ErrorField> errorFields;
    private Object extData;

    public ErrorData(Integer errorCode, List<ErrorField> errorFields, Object extData) {
        this.errorCode = errorCode;
        this.errorFields = errorFields;
        this.extData = extData;
    }

    public static class ErrorField {
        private String fieldName;
        private String errorMsg;
    }
}
