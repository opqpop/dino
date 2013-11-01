package com.lolRiver.river.models;

import org.apache.commons.lang3.StringUtils;
/**
 * @author mxia (mxia@lolRiver.com)
 *         10/2/13
 */

public class Role {
    public enum Name {
        NONE,   // for ARAM
        TOP,
        MID,
        JUNG,
        SUPP,
        ADC
    }

    private Name name;

    public Role(Name name) {
        this.name = name;
    }

    public static Role fromString(String roleName) {
        if (StringUtils.isBlank(roleName)) {
            return null;
        }
        return new Role(Role.Name.valueOf(roleName));
    }

    public Role partnerRole() {
        if (name == Name.SUPP) {
            return new Role(Name.ADC);
        } else if (name == Name.ADC) {
            return new Role(Name.SUPP);
        }
        return null;
    }

    public Name getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }

        Role role = (Role)o;

        if (name != role.name) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Role{" +
               "name=" + name +
               '}';
    }
}
