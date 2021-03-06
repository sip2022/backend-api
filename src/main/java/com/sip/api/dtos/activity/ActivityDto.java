package com.sip.api.dtos.activity;

import com.sip.api.dtos.user.UserSlimDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.index.qual.NonNegative;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDto {
    private String id;
    private String name;
    @NonNegative
    private Double basePrice;
    private UserSlimDto professor;
    @NonNegative
    private int attendeesLimit;
}
