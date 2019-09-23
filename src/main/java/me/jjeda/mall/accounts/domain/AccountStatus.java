package me.jjeda.mall.accounts.domain;

/**
 * NORMAL : 정상 사용자
 * BAN : 관리자에 의해 제재된 사용자
 * EXPIRED : 일정 기간동안 접속을 하지않아 휴면상태인 사용자
 * DELETED : 사용자에 의해 탈퇴처리 된 사용자
 */

public enum AccountStatus {

    NORMAL, BAN, EXPIRED, DELETED
}
