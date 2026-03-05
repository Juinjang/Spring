package umc.th.juinjang.api.limjang.service.response;

public record NotePostResponse(Long noteId) {
	public static NotePostResponse of(Long noteId) {
		return new NotePostResponse(noteId);
	}
}
