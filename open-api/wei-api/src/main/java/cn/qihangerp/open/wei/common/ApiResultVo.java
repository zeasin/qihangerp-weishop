package cn.qihangerp.open.wei.common;

import java.util.List;

/**
 * 描述：
 * Service返回结果VO
 *
 * @author qlp
 * @date 2018-12-26 5:20 PM
 */
public class ApiResultVo<T> {
    private int code;
    private String msg;
    private T data;
    private List<T> list;
    private Integer totalRecords;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public ApiResultVo() {
    }
//
//    public ResultVo(ResultVoEnum result) {
//        this.code = result.getIndex();
//        this.msg = result.getName();
//    }
//
//    public ResultVo(ResultVoEnum result, String msg, T data) {
//        this.code = result.getIndex();
//        this.msg = msg;
//        this.data = data;
//    }
//
//    public ResultVo(ResultVoEnum result, String msg) {
//        this.code = result.getIndex();
//        this.msg = msg;
//    }
//
//    public ResultVo(ResultVoEnum result, T data) {
//        this.code = result.getIndex();
//        this.msg = result.getName();
//        this.data = data;
//    }


    public static <T> ApiResultVo<T> success(int totalRecords , List<T> list){
        ApiResultVo<T> result = new ApiResultVo<>();
        result.setCode(ApiResultVoEnum.SUCCESS.getIndex());
        result.setMsg("SUCCESS");
        result.totalRecords = totalRecords;
        result.setList(list);
        return result;
    }

    public static <T> ApiResultVo<T> success(T data){
        ApiResultVo<T> result = new ApiResultVo<>();
        result.setCode(ApiResultVoEnum.SUCCESS.getIndex());
        result.setMsg("SUCCESS");
        result.setData(data);
        return result;
    }
    public static <T> ApiResultVo<T> success(int code, T data){
        ApiResultVo<T> result = new ApiResultVo<>();
        result.setCode(code);
        result.setMsg("SUCCESS");
        result.setData(data);
        return result;
    }

    public static <T> ApiResultVo<T> success(){
        ApiResultVo<T> result = new ApiResultVo<>();
        result.setCode(ApiResultVoEnum.SUCCESS.getIndex());
        result.setMsg("SUCCESS");
        return result;
    }

    public static <T> ApiResultVo<T> error(ApiResultVoEnum apiResultVoEnum)
    {
        ApiResultVo<T> result = new ApiResultVo<>();
        result.setCode(apiResultVoEnum.getIndex());
        result.setMsg(apiResultVoEnum.getName());
        return result;
    }

    public static <T> ApiResultVo<T> error(ApiResultVoEnum apiResultVoEnum, String msg)
    {
        ApiResultVo<T> result = new ApiResultVo<>();
        result.setCode(apiResultVoEnum.getIndex());
        result.setMsg(msg);
        return result;
    }

    public static <T> ApiResultVo<T> error(int code, String msg)
    {
        ApiResultVo<T> result = new ApiResultVo<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
    public static <T> ApiResultVo<T> error(int code, String msg, T data)
    {
        ApiResultVo<T> result = new ApiResultVo<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

}
