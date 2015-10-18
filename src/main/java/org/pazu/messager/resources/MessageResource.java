package org.pazu.messager.resources;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.pazu.messager.model.Message;
import org.pazu.messager.service.MessageService;

@Path("/messages")
public class MessageResource {

	MessageService messageService = new MessageService();

	@GET
	// @Produces(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> getMessages(@QueryParam("year") int year,
			@QueryParam("start") int start, @QueryParam("size") int size) {

		if (year > 0) {
			return messageService.getAllMessagesForYear(year);
		}

		if (start >= 0 && size > 0) {
			return messageService.getAllMessagesPaginated(start, size);
		}

		return messageService.getAllMessages();
	}

	// @POST
	// // @Produces(MediaType.APPLICATION_XML)
	// @Consumes(MediaType.APPLICATION_JSON)
	// @Produces(MediaType.APPLICATION_JSON)
	// public Message addMessage(Message message) {
	// return messageService.addMessage(message);
	// }

	// @POST
	// @Consumes(MediaType.APPLICATION_JSON)
	// @Produces(MediaType.APPLICATION_JSON)
	// public Response addMessage(Message message) {
	//
	// Message newMessage = messageService.addMessage(message);
	// return Response.status(Status.CREATED).entity(newMessage).build();
	//
	// }

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addMessage(Message message, @Context UriInfo uriInfo) {

		Message newMessage = messageService.addMessage(message);
		String newId = String.valueOf(newMessage.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();

		return Response.created(uri).entity(newMessage).build();

	}

	@GET
	@Path("/{messageId}")
	// @Produces(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public Message getMessage(@PathParam("messageId") long messageId,
			@Context UriInfo uriInfo) {

		Message message = messageService.getMessage(messageId);
		message.addLink(getUriForSelf(uriInfo, message), "self");
		message.addLink(getUriForProfile(uriInfo, message), "profile");
		message.addLink(getUriForComments(uriInfo, message), "comments");
		return message;
	}

	@PUT
	@Path("/{messageId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Message updateMessage(@PathParam("messageId") long messageId,
			Message message) {
		message.setId(messageId);
		return messageService.updateMessage(message);
	}

	@DELETE
	@Path("/{messageId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message removeMessage(@PathParam("messageId") long messageId) {
		return messageService.removeMessage(messageId);
	}

	@Path("/{messageId}/comments")
	public CommentResource getCommentResource() {
		return new CommentResource();
	}

	private String getUriForSelf(UriInfo uriInfo, Message message) {
		String uri = uriInfo.getBaseUriBuilder().path(MessageResource.class)
				.path(Long.toString(message.getId())).build().toString();

		return uri;
	}

	private String getUriForComments(UriInfo uriInfo, Message message) {
		URI uri = uriInfo.getBaseUriBuilder().path(MessageResource.class)
				.path(MessageResource.class, "getCommentResource")
				.path(CommentResource.class)
				.resolveTemplate("messageId", message.getId()).build();
		return uri.toString();
	}

	private String getUriForProfile(UriInfo uriInfo, Message message) {
		URI uri = uriInfo.getBaseUriBuilder().path(ProfileResource.class)
				.path(message.getAuthor()).build();
		return uri.toString();
	}

}
