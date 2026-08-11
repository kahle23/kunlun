package kunlun.extension.convert.exchangerate;

public interface ExRateFiller {

    /**
     * 给 业务数据 填充汇率信息
     * @param data 业务数据
     */
    void fill(Object data);

}
