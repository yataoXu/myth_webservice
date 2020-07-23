package com.zdmoney.webservice.api.dto.app;

import com.zdmoney.webservice.api.dto.busi.BusiBrandDto;
import lombok.Data;

import java.io.Serializable;

/**
 * @author gaol
 * @create 2019-03-20
 */
@Data
public class PublicizeDTO implements Serializable {

    private Advert advert;

    private BusiBrandDto brand;
}
