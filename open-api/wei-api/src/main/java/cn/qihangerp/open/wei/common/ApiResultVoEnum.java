package cn.qihangerp.open.wei.common;
/**
 * 描述：
 * 结果枚举
 *
 * @author qlp
 * @date 2019-04-10 10:31
 */
public enum ApiResultVoEnum {
    SUCCESS("成功", 0),
    StateError("StateError", 1402),//状态错误
    SIGN_ERROR("签名错误", 1403),//

    ParamsError("参数错误", 1405),//参数错误
    SystemException("系统异常", 1505),//系统异常
    ApiException("Api异常", 1506),//系统异常
    TokenFail("token过期", 1401),//ali token过期
    Fail("失败", 1000);

    private String name;
    private int index;

    // 构造方法
    private ApiResultVoEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (ApiResultVoEnum c : ApiResultVoEnum.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
