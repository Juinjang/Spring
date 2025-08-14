package umc.th.juinjang.domain.withdraw.model;

public enum WithdrawReason {

    NOT_USE,            // 더 이상 쓸 일이 없어요
    CANNOT_USE,         // 앱 사용법을 모르겠어요
    NOT_HELP,           // 임장에 도움이 되지 않아요
    CANNOT_FUNCTION,    // 앱이 정상적으로 작동하지 않아요
    CONCERN_SECURITY,   // 보안이 걱정돼요
    OTHER_SERVICE       // 다른 서비스가 더 좋아요
}
