package com.zdmoney.models.flw;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @ Author : Evan.
 * @ Description :
 * @ Date : Crreate in 2018/10/26 18:06
 * @Mail : xuyt@zendaimoney.com
 */

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "orders")
public class Orders {
    private List<Order> order;
}
