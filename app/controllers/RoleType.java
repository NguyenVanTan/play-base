package controllers;

public enum RoleType {

    ADMIN(1, "Admin"), OTHER(2, "Other");

    private int roleId = 0;
    private String roleName = "";

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    RoleType(int roleId, String roleName){
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public static boolean isAdmin(int type){
        return ADMIN.getRoleId() == type;
    }
}
