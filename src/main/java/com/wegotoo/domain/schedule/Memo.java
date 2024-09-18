package com.wegotoo.domain.schedule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Long id;

    @Column(name = "memo_content", nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detailed_plan_id")
    private DetailedPlan detailedPlan;

    @Builder
    private Memo(String content, DetailedPlan detailedPlan) {
        this.content = content;
        this.detailedPlan = detailedPlan;
    }

    public static Memo write(String content, DetailedPlan detailedPlan) {
        return Memo.builder()
                .content(content)
                .detailedPlan(detailedPlan)
                .build();
    }

    public void edit(String content) {
        this.content = content != null ? content : this.content;
    }
}
