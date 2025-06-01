package com.example.account.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_REQUEST("잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR("서버 오류가 발생했습니다."),

    USER_NOT_FOUND("존재하지 않는 사용자입니다."),
    MAX_ACCOUNT_PER_USER_OVER("보유 가능한 계좌의 최대 갯수를 초과하였습니다."),

    ACCOUNT_NOT_FOUND("존재하지 않는 계좌입니다."),
    ACCOUNT_LOCKED("해당 계좌는 사용중입니다."),
    ACCOUNT_USER_NOT_MATCH("사용자 아이디와 계좌 소유주가 맞지 않습니다."),
    ACCOUNT_ALREADY_UNREGISTERED("계좌가 이미 해지된 상태입니다."),
    ACCOUNT_BALANCE_REMAINS("잔액이 남아 있습니다."),

    AMOUNT_OVER_BALANCE("거래 금액이 잔액보다 큽니다."),

    TRANSACTION_NOT_FOUND("거래 ID에 해당하는 거래가 없습니다."),
    TRANSACTION_ACCOUNT_NOT_MATCH("거래가 발생한 계좌가 아닙니다."),
    TRANSACTION_PARTIAL_CANCEL_NOT_ALLOWED("거래 금액과 취소 금액이 다릅니다."),
    TRANSACTION_TOO_OLD_TO_CANCEL("1년이 지난 거래는 취소가 불가능합니다.")
    ;

    private final String description;
}
