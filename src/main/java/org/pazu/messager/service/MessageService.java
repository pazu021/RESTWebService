package org.pazu.messager.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.pazu.messager.database.DatabaseClass;
import org.pazu.messager.exception.DataNotFoundException;
import org.pazu.messager.model.Message;

public class MessageService {

	private Map<Long, Message> messages = DatabaseClass.getMessages();

	public MessageService() {
		messages.put(1L, new Message(1L, "message A", "author A"));
		messages.put(2L, new Message(2L, "message B", "author B"));
		messages.put(3L, new Message(3L, "message C", "author C"));
	}

	public List<Message> getAllMessagesForYear(int year) {

		List<Message> messageList = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		for (Message m : messages.values()) {
			cal.setTime(m.getCreated());
			if (cal.get(Calendar.YEAR) == year) {
				messageList.add(m);
			}
		}
		return messageList;
	}

	public List<Message> getAllMessagesPaginated(int start, int size) {

		ArrayList<Message> list = new ArrayList<Message>(messages.values());
		if (start > list.size()) {
			return new ArrayList<Message>();
		}
		return list.subList(start, start + size);

	}

	public List<Message> getAllMessages() {
		return new ArrayList<Message>(messages.values());
	}

	public Message getMessage(long id){

		Message message = messages.get(id);
		if (message == null) {
			throw new DataNotFoundException("Message with id " + id
					+ " not found");
		}
		return message;
	}

	public Message addMessage(Message message) {
		message.setId(messages.size() + 1);
		messages.put(message.getId(), message);
		return message;
	}

	public Message updateMessage(Message message) {
		if (message.getId() <= 0) {
			return null;
		}
		messages.put(message.getId(), message);
		return message;
	}

	public Message removeMessage(long id) {
		return messages.remove(id);
	}
	
	
	

}
