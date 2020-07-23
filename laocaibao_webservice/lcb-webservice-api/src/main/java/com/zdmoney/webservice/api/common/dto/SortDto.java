package com.zdmoney.webservice.api.common.dto;


/**
 * Created by xinqigu on 2015/8/25.
 */
public class SortDto extends BaseDto {

    private static final long serialVersionUID = -5011009474137538871L;

    /** 排序属性 */
    private String property;

    /** 升/降 */
    private Direction direction;

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public SortDto() {

    }

    public SortDto(String property) {
        this.direction = Direction.ASC;
        this.property = property;
    }

    public SortDto(String property, Direction direction) {
        this.direction = direction;
        this.property = property;
    }

    public enum Direction {
        ASC, DESC;
        Direction() {}
    }
}
