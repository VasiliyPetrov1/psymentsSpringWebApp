package org.kosiuk.webApp.dto;

import org.kosiuk.webApp.entity.Role;
import org.kosiuk.webApp.exceptions.NotCompatibleRolesException;

import java.util.Set;

public interface UserRoleProvider{
    Set<Role> getCheckedRoles () throws NotCompatibleRolesException;
}
