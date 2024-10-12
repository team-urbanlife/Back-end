package com.wegotoo.application.schedule.response;

import com.wegotoo.domain.schedule.repository.response.DetailedPlanQueryEntity;
import com.wegotoo.domain.schedule.repository.response.ScheduleDetailsQueryEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TravelPlanResponse {

    private Long scheduleDetailsId;
    private LocalDate travelDate;
    private List<DetailedPlanQueryEntity> detailedPlans = new ArrayList<>();

    @Builder
    private TravelPlanResponse(Long scheduleDetailsId, LocalDate travelDate, List<DetailedPlanQueryEntity> detailedPlans) {
        this.scheduleDetailsId = scheduleDetailsId;
        this.travelDate = travelDate;
        this.detailedPlans = detailedPlans;
    }

    public static TravelPlanResponse of(ScheduleDetailsQueryEntity scheduleDetails, List<DetailedPlanQueryEntity> detailedPlan) {
        return TravelPlanResponse.builder()
                .scheduleDetailsId(scheduleDetails.getId())
                .travelDate(scheduleDetails.getTravelDay())
                .detailedPlans(detailedPlan)
                .build();
    }

    public static List<TravelPlanResponse> toList(List<ScheduleDetailsQueryEntity> scheduleDetails,
                                                  List<DetailedPlanQueryEntity> detailedPlans) {
        Map<Long, List<DetailedPlanQueryEntity>> plansMap = toMap(detailedPlans);
        return scheduleDetails.stream()
                .map(scheduleDetail -> TravelPlanResponse.of(scheduleDetail, plansMap.get(scheduleDetail.getId()))).toList();
    }

    private static Map<Long, List<DetailedPlanQueryEntity>> toMap(List<DetailedPlanQueryEntity> detailedPlans) {
        if (detailedPlans == null) {
            return Collections.emptyMap();
        }
        return detailedPlans.stream()
                .collect(Collectors.groupingBy(DetailedPlanQueryEntity::getScheduleDetailsId));
    }
}
