package com.zdmoney.models;

public class InitVerAddr {
    private Long id;

    private String clientVer;
    
    public String getClientVer() {
		return clientVer;
	}

	public void setClientVer(String clientVer) {
		this.clientVer = clientVer;
	}

	public String getUrlAddr() {
		return urlAddr;
	}

	public void setUrlAddr(String urlAddr) {
		this.urlAddr = urlAddr;
	}

	private String urlAddr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}