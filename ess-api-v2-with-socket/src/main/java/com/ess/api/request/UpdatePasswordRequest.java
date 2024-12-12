package com.ess.api.request;

import lombok.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdatePasswordRequest {
    private String email;

    private String oldPassword;

    private String newPassword;
}
