package com.zdmoney.models.payChannel;

import javax.persistence.*;

@Table(name = "BUSI_PAY_CHANNEL")
public class BusiPayChannel {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name="",sequenceName="Oracle")
    private Long id;

    @Column(name = "CHANNEL_NAME")
    private String channelName;

    @Column(name = "IS_ENABLE")
    private Short isEnable;

    @Column(name = "PAY_PLATFORM_CODE")
    private String payPlatformCode;

    @Column(name = "PAY_URL")
    private String payUrl;

    @Column(name = "PAY_PRIVATE_KEY")
    private String payPrivateKey;

    @Column(name = "PAY_MERCHANT_NO")
    private String payMerchantNo;

    @Column(name = "PAY_VESION")
    private String payVesion;

    @Column(name = "USE_CITY")
    private String useCity;

    /**
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return CHANNEL_NAME
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * @param channelName
     */
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    /**
     * @return IS_ENABLE
     */
    public Short getIsEnable() {
        return isEnable;
    }

    /**
     * @param isEnable
     */
    public void setIsEnable(Short isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * @return PAY_PLATFORM_CODE
     */
    public String getPayPlatformCode() {
        return payPlatformCode;
    }

    /**
     * @param payPlatformCode
     */
    public void setPayPlatformCode(String payPlatformCode) {
        this.payPlatformCode = payPlatformCode;
    }

    /**
     * @return PAY_URL
     */
    public String getPayUrl() {
        return payUrl;
    }

    /**
     * @param payUrl
     */
    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    /**
     * @return PAY_PRIVATE_KEY
     */
    public String getPayPrivateKey() {
        return payPrivateKey;
    }

    /**
     * @param payPrivateKey
     */
    public void setPayPrivateKey(String payPrivateKey) {
        this.payPrivateKey = payPrivateKey;
    }

    /**
     * @return PAY_MERCHANT_NO
     */
    public String getPayMerchantNo() {
        return payMerchantNo;
    }

    /**
     * @param payMerchantNo
     */
    public void setPayMerchantNo(String payMerchantNo) {
        this.payMerchantNo = payMerchantNo;
    }

    /**
     * @return PAY_VESION
     */
    public String getPayVesion() {
        return payVesion;
    }

    /**
     * @param payVesion
     */
    public void setPayVesion(String payVesion) {
        this.payVesion = payVesion;
    }

    /**
     * @return USE_CITY
     */
    public String getUseCity() {
        return useCity;
    }

    /**
     * @param useCity
     */
    public void setUseCity(String useCity) {
        this.useCity = useCity;
    }
}