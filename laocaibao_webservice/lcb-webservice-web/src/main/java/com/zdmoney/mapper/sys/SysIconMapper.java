package com.zdmoney.mapper.sys;

import com.zdmoney.webservice.api.dto.sys.SysIconDto;

import java.util.List;

public interface SysIconMapper {

	List<SysIconDto> querySysIcon();

	int updateSysIcon(SysIconDto sysIcon);

	int deleteIcon();
}
