package com.example.pass.repository.user;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class UserGroupMappingId implements Serializable {
    private String userGroupId;
    private String userId;
}
