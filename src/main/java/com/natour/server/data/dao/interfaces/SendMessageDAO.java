package com.natour.server.data.dao.interfaces;

import com.natour.server.application.dtos.response.ResultMessageDTO;

public interface SendMessageDAO {

	public ResultMessageDTO sendMessage(String idConnection, String message);
}
