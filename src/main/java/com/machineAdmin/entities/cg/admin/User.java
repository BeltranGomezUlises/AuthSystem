package com.machineAdmin.entities.cg.admin;

import com.machineAdmin.entities.cg.EntityMongo;
import java.util.Date;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class User extends EntityMongo {

    private String user;
    private String mail;
    private String pass;
    private Object permissions;
    private String phone;

    private LoginAttempt loginAttempt;
    private BlockedUser blocked;

    public User() {
    }

    public void riseLoginAttemps() {
        this.loginAttempt.riseNumberAttempts();
        this.loginAttempt.setLastLoginAttemptDate(new Date());
    }

    public BlockedUser getBlocked() {
        return blocked;
    }

    public void setBlocked(BlockedUser blocked) {
        this.blocked = blocked;
    }

    public LoginAttempt getLoginAttempt() {
        return loginAttempt;
    }

    public void setLoginAttempt(LoginAttempt loginAttempt) {
        this.loginAttempt = loginAttempt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Object getPermissions() {
        return permissions;
    }

    public void setPermissions(Object permissions) {
        this.permissions = permissions;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "User{" + "user=" + user + ", mail=" + mail + ", pass=" + pass + ", permissions=" + permissions + '}';
    }

    public static class LoginAttempt {

        private Date lastLoginAttemptDate;
        private int numberLoginAttempts;

        public LoginAttempt() {
        }

        public LoginAttempt(Date lastLoginAttemptDate, int numberLoginAttempts) {
            this.lastLoginAttemptDate = lastLoginAttemptDate;
            this.numberLoginAttempts = numberLoginAttempts;
        }

        public void riseNumberAttempts() {
            this.numberLoginAttempts++;
        }

        public Date getLastLoginAttemptDate() {
            return lastLoginAttemptDate;
        }

        public void setLastLoginAttemptDate(Date lastLoginAttemptDate) {
            this.lastLoginAttemptDate = lastLoginAttemptDate;
        }

        public int getNumberLoginAttempts() {
            return numberLoginAttempts;
        }

        public void setNumberLoginAttempts(int numberLoginAttempts) {
            this.numberLoginAttempts = numberLoginAttempts;
        }

        @Override
        public String toString() {
            return "LoginAttempt{" + "lastLoginAttemptDate=" + lastLoginAttemptDate + ", numberLoginAttempts=" + numberLoginAttempts + '}';
        }

    }

    public static class BlockedUser {

        private boolean blocked;
        private Date blockedUntilDate;

        public BlockedUser() {
        }

        public BlockedUser(boolean blocked, Date blockedUntilDate) {
            this.blocked = blocked;
            this.blockedUntilDate = blockedUntilDate;
        }

        public boolean isBlocked() {
            return blocked;
        }

        public void setBlocked(boolean blocked) {
            this.blocked = blocked;
        }

        public Date getBlockedUntilDate() {
            return blockedUntilDate;
        }

        public void setBlockedUntilDate(Date blockedUntilDate) {
            this.blockedUntilDate = blockedUntilDate;
        }

        @Override
        public String toString() {
            return "BlocekUser{" + "blocked=" + blocked + ", blockedUntilDate=" + blockedUntilDate + '}';
        }

    }

}
