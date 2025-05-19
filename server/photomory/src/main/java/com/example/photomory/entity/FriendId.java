package com.example.photomory.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FriendId implements Serializable {
    private Long fromUserId;
    private Long toUserId;

    //hw-친구추가할때 추가
    // 두 객체가 같은지 비교 (equals는 반드시 오버라이드 해야 함)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendId)) return false;
        FriendId that = (FriendId) o;
        return Objects.equals(fromUserId, that.fromUserId) &&
                Objects.equals(toUserId, that.toUserId);
    }

    // hashCode도 꼭 오버라이드 해야 함 (JPA에서 key 식별용으로 사용)
    @Override
    public int hashCode() {
        return Objects.hash(fromUserId, toUserId);
    }
}
