package com.natour.server.data.dao.interfaces;

import com.natour.server.application.dtos.response.ResultMessageDTO;

public interface MessageDAO {

	public ResultMessageDTO sendMessage(String idConnection, String message);
}
