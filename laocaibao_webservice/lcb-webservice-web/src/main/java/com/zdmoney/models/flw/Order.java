package com.zdmoney.models.flw;


import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;


/**
 * @ Author : Evan.
 * @ Description :
 * @ Date : Crreate in 2018/10/26 16:08
 * @Mail : xuyt@zendaimoney.com
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "order")
public class Order {
    private String s_id;
    private String order_id_parent;
    private String order_id;
    private String order_time;
    private String uid;
    private String uname;
    private String tc;
    private String pay_time;
    //status 状态 （0 流标,1已支付,2 起息）
    private int status;
    private int locked = 0;
    private String lastmod;
    private int is_newbuyer = 1;
    private int platform = 0;
    private String remark = "";
    private Extension extension;
    @XmlElementWrapper
    @XmlElements({@XmlElement(name = "product", type = Product.class)})
    private List<Product> products;


}
