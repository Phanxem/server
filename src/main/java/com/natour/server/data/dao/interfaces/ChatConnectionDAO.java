package com.natour.server.data.dao.interfaces;

import com.natour.server.data.entities.dynamoDB.ChatConnection;

public interface ChatConnectionDAO {

	public ChatConnection findById(String id);
	
	public ChatConnection add(String id);
	
	public ChatConnection updateWithIdUser(String id, String idUser);
	
	public ChatConnection findByIdUser(String idUser);
	
	public void delete(String id);
	
}
