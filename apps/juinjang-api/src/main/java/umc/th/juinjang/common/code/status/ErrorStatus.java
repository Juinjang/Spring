package umc.th.juinjang.common.code.status;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import umc.th.juinjang.common.code.BaseErrorCode;
import umc.th.juinjang.common.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
	// 가장 일반적인 응답
	_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
	_BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
	_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
	_FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
	// For test
	TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트"),

	// Member Error
	MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "해당하는 사용자를 찾을 수 없습니다."),
	MEMBER_EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4002", "전달받은 사용자의 이메일이 없습니다."),
	MEMBER_NOT_FOUND_IN_KAKAO(HttpStatus.BAD_REQUEST, "MEMBER4003", "APPLE로 회원가입한 회원입니다."),
	ALREADY_LOGOUT(HttpStatus.BAD_REQUEST, "MEMBER4004", "이미 로그아웃 되었습니다."),
	UNCORRECTED_INFO(HttpStatus.BAD_REQUEST, "MEMBER4005", "올바르지 않은 정보입니다."),
	MEMBER_NOT_FOUND_IN_APPLE(HttpStatus.BAD_REQUEST, "MEMBER4006", "KAKAO로 회원가입한 회원입니다."),
	ALREADY_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER4007", "이미 가입된 회원입니다."),
	// 카카오 target id 에러
	EMPTY_TARGET_ID(HttpStatus.BAD_REQUEST, "MEMBER4008", "카카오 target id가 비어있습니다."),
	UNCORRECTED_TARGET_ID(HttpStatus.BAD_REQUEST, "MEMBER4009",
		"target id와 회원 정보가 일치하지 않습니다. 올바르지 않은 카카오 target id 입니다."),
	NOT_UNLINK_KAKAO(HttpStatus.BAD_REQUEST, "MEMBER4010", "카카오 연결 끊기에 실패하였습니다."),
	FAILED_TO_LOGIN(HttpStatus.INTERNAL_SERVER_ERROR, "MEMBER4011", "잘못된 정보입니다. 서버 관리자에게 문의 바랍니다."),
	FAILED_TO_SIGNUP(HttpStatus.INTERNAL_SERVER_ERROR, "MEMBER4012", "회원가입에 실패했습니다. 서버 관리자에게 문의 바랍니다."),

	//로그아웃 에러
	FAILED_TO_LOAD_PRIVATE_KEY(HttpStatus.BAD_REQUEST, "REVOKE4002", "private key 실패"),
	LOGOUT_FAILED(HttpStatus.BAD_REQUEST, "REVOKE4002", "private key 실패"),
	// nickname 에러
	NICKNAME_EMPTY(HttpStatus.BAD_REQUEST, "NICKNAME4001", "닉네임이 존재하지 않습니다. 닉네임을 입력해주세요."),
	ALREADY_NICKNAME(HttpStatus.BAD_REQUEST, "NICKNAME4002", "이미 존재하는 닉네임입니다."),

	// Limjang Error
	LIMJANG_POST_REQUEST_NULL(HttpStatus.BAD_REQUEST, "LIMJANG4001", "입력 값이 모두 넘어오지 않았습니다. 누락된 값이 있는지 다시 확인해주세요."),
	LIMJANG_POST_TYPE_ERROR(HttpStatus.BAD_REQUEST, "LIMJANG4002",
		"거래목적, 매물유형, 가격유형 입력값 중 하나가 정해지지 않은 값입니다. 다시 확인해주세요."),
	LIMJANG_POST_PRICE_ERROR(HttpStatus.BAD_REQUEST, "LIMJANG4003", "전달된 가격이 잘못되었습니다. 입력값을 확인해주세요."),
	LIMJANG_NOTFOUND_ERROR(HttpStatus.BAD_REQUEST, "LIMJANG404", "해당 임장이 존재하지 않습니다."),

	LIMJANG_DELETE_NOT_FOUND(HttpStatus.BAD_REQUEST, "LIMJANG4005", "전달된 ID의 값이 DB에 존재하지 않습니다. 전달 값을 다시 확인해주세요."),
	LIMJANG_DELETE_NOT_COMPLETE(HttpStatus.BAD_REQUEST, "LIMJANG4006",
		"요청한 임장 게시글이 모두 삭제되지 않아 삭제가 취소되었습니다. 다시 시도하거나 백엔드 팀에 문의바랍니다."),
	LIMJANG_UPDATE_PRICETYPE_ERROR(HttpStatus.BAD_REQUEST, "LIMJANG4007", "가격유형 입력값이 잘못되었습니다. 다시 확인해주세요."),
	LIMJANG_REQUEST_SORT_ERROR(HttpStatus.BAD_REQUEST, "LIMJANG4008", "요청한 정렬 방식이 지정되지 않은 값입니다. 다시 확인해주세요."),
	LIMJANG_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "LIMJANG4009", "요청한 임장 업데이트가 정상적으로 진행되지 않았습니다. 백엔드 팀에 문의 바랍니다."),

	// LimjangPrice Error
	LIMJANGPRICE_NOTFOUND_ERROR(HttpStatus.BAD_REQUEST, "LIMJANGPRICE4000", "해당 임장가격 레코드가 존재하지 않습니다."),
	LIMJANGPRICE_NULL_ERROR(HttpStatus.BAD_REQUEST, "LIMJANGPRICE4000", "임장 가격 저장에 실패했습니다. 임장 가격이 null입니다."),

	// scrap
	_SCRAP_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "SCRAP4000", "해당 게시글이 DB에 존재하지 않습니다. 관리자에게 문의 바랍니다."),
	_SCRAP_SCRAP_FAILD(HttpStatus.INTERNAL_SERVER_ERROR, "SCRAP4001", "스크랩 등록 실패. 재시도하거나 관리자에게 문의 바랍니다."),
	_SCRAP_UNSCRAP_FAILD(HttpStatus.INTERNAL_SERVER_ERROR, "SCRAP4002", "스크랩 취소 실패. 재시도하거나 관리자에게 문의 바랍니다."),
	_SCRAP_ALREADY_SCRAPED(HttpStatus.INTERNAL_SERVER_ERROR, "SCRAP4003", "이미 스크랩된 게시글입니다."),
	_SCRAP_ALREADY_UNSCRAPED(HttpStatus.INTERNAL_SERVER_ERROR, "SCRAP4004", "이미 스크랩 취소된 게시글입니다."),

	CHECKLIST_TYPE_ERROR(HttpStatus.BAD_REQUEST, "CHECKLIST400", "정해지지 않은 요청값입니다. 다시 확인해주세요."),
	CHECKLIST_NOTFOUND_ERROR(HttpStatus.BAD_REQUEST, "CHECKLIST404", "해당 체크리스트 질문 또는 답변이 존재하지 않습니다."),

	REPORT_NOTFOUND_ERROR(HttpStatus.BAD_REQUEST, "REPORT404", "해당 리포트가 존재하지 않습니다."),

	//JWT 토큰 에러
	TOKEN_UNAUTHORIZED(HttpStatus.INSUFFICIENT_SPACE_ON_RESOURCE, "TOKEN400", "유효하지 않거나 만료된 토큰입니다."),
	TOKEN_EMPTY(HttpStatus.BAD_REQUEST, "TOKEN401", "토큰값이 존재하지 않습니다."),
	REFRESH_TOKEN_UNAUTHORIZED(HttpStatus.I_AM_A_TEAPOT, "TOKEN402", "유효하지 않은 Refresh Token입니다. 다시 로그인하세요."),
	ACCESS_TOKEN_AUTHORIZED(HttpStatus.INSUFFICIENT_SPACE_ON_RESOURCE, "TOKEN403", "유효하지 않은 Access Token입니다."),
	REFRESH_TOKEN_AUTHORIZED(HttpStatus.UNAUTHORIZED, "TOKEN404", "유효한 Refresh Token입니다."),
	APPLE_ID_TOKEN_EMPTY(HttpStatus.BAD_REQUEST, "TOKEN405", "ID TOKEN값이 존재하지 않습니다."),
	INVALID_APPLE_ID_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN406", "Apple OAuth Identity Token 값이 올바르지 않습니다."),
	PUBLICKEY_ERROR_IN_APPLE_LOGIN(HttpStatus.UNAUTHORIZED, "TOKEN407", "Apple OAuth 로그인 중 public key 생성에 문제가 발생했습니다."),
	INVALID_APPLE_ID_TOKEN_INFO(HttpStatus.UNAUTHORIZED, "TOKEN407", "Apple id_token 값의 alg, kid 정보가 올바르지 않습니다."),

	// Image 에러
	IMAGE_DELETE_NOT_FOUND(HttpStatus.BAD_REQUEST, "IMAGE4000", "전달된 ID의 값이 DB에 존재하지 않습니다. 전달 값을 다시 확인해주세요."),
	IMAGE_DELETE_NOT_COMPLETE(HttpStatus.BAD_REQUEST, "IMAGE4001", "요청한 임장 게시글이 모두 삭제되지 않아 삭제가 취소되었습니다."),
	IMAGE_EMPTY(HttpStatus.BAD_REQUEST, "IMAGE4002", "이미지가 첨부되지 않았습니다."),
	IMAGE_NOT_SAVE(HttpStatus.BAD_REQUEST, "IMAGE4003", "이미지 저장에 실패했습니다."),

	//S3 에러
	//FILE_BAD_REQUEST(HttpStatus.BAD_REQUEST, "FILE400", ""),
	S3_NOT_FOUND(HttpStatus.NOT_FOUND, "S34000", "해당 file이 s3에 존재하지 않습니다. 백엔드 팀에 문의바랍니다."),
	S3_DELTE_FAILED(HttpStatus.NOT_FOUND, "S34000", "해당 file이 s3에 존재하지 않습니다. 백엔드 팀에 문의바랍니다."),

	//record 에러
	RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "RECORD400", "record가 존재하지 않습니다"),

	//withdraw 에러
	WITHDRAW_REASON_NOT_FOUND(HttpStatus.NOT_FOUND, "WITHDRAW400", "해당 탈퇴 사유 enum 값이 존재하지 않습니다"),

	// discord alert
	DISCORD_ALERT_ERROR(HttpStatus.NOT_FOUND, "DISCORD400", "discord 알림 수신 중 오류가 발생했습니다."),

	// PencilAccount alert
	PENCIL_ACCOUNT_NOT_FOUND(HttpStatus.BAD_REQUEST, "ACCOUNT4000", "멤버에 해당하는 계좌가 존재하지 않습니다."),

	// Apple  ALERT
	APPLE_VERIFICATION_ERROR(HttpStatus.BAD_REQUEST, "APPLE4000", "애플 인증 관련 에러가 발생했습니다."),

	SHAREDNOTE_NOT_FOUND(HttpStatus.NOT_FOUND, "SHAREDNOTE4000", "해당하는 공유노트가 존재하지 않습니다."),
	SHAREDNOTE_NOT_ENOUGH_PENCIL(HttpStatus.BAD_REQUEST, "SHAREDNOTE4001", "보유한 연필 수가 부족합니다."),
	SHAREDNOTE_CONFLICT(HttpStatus.CONFLICT, "SHAREDNOTE4002", "이미 구매한 노트입니다."),
	SHAREDNOTE_DEADLOCK(HttpStatus.LOCKED, "SHAREDNOTE4003", "잠시 후 다시 시도해주세요. 현재 다른 요청이 처리 중입니다."),
	SHAREDNOTE_TYPE_ERROR(HttpStatus.BAD_REQUEST, "SHAREDNOTE4004", "유효하지 않은 마이 노트 요청 타입입니다."),
	SHARED_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "SAHREDNOTE4005", "공유가 금지된 노트입니다."),
	SHAREDNOTE_DELETED_RECENTLY(HttpStatus.BAD_REQUEST, "SHAREDNOTE4006", "삭제한지 6개월이 지나지 않아 다시 공유할 수 없습니다."),
	SHAREDNOTE_ALREADY_EXISTS(HttpStatus.CONFLICT, "SHAREDNOTE4007", "이미 공유된 노트입니다."),

	// LikedNote
	LIKEDNOTE_CONFLICT(HttpStatus.CONFLICT, "LIKEDNOTE4000", "이미 좋아요한 노트입니다"),
	LIKEDNOTE_NOT_FOUND(HttpStatus.NOT_FOUND, "LIKEDNOTE4001", "이미 취소했거나 좋아요한 적이 없습니다."),

	// Terms_Agreement
	TERMS_AGREEMENT_DUPLICATED(HttpStatus.BAD_REQUEST, "TERMS_AGREEMENT_4000", "이미 약관에 동의하였습니다.");
	
	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

	@Override
	public ErrorReasonDTO getReason() {
		return ErrorReasonDTO.builder()
			.message(message)
			.code(code)
			.isSuccess(false)
			.build();
	}

	@Override
	public ErrorReasonDTO getReasonHttpStatus() {
		return ErrorReasonDTO.builder()
			.message(message)
			.code(code)
			.isSuccess(false)
			.httpStatus(httpStatus)
			.build()
			;
	}
}
