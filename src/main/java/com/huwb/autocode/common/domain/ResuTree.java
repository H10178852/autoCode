package com.huwb.autocode.common.domain;

public class ResuTree {

    /**
     * 状态信息
     * */
    private Status status = new Status();

    /**
     * 返回数据
     * */
    private Object data;

    /**
     * 所需内部类
     * */

    public class Status{

        private Integer code = 200;

        private String message = "默认";

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Status(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public Status() {
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResuTree() {
    }

    public ResuTree(Status status, Object data) {
        this.status = status;
        this.data = data;
    }
}
