package vo;

/* login 한 user 정보를 저장하기 위한 vo user 객체 */

public class LoginVO {
    private int idx;
    private String userId;
    private String password;
    private String name;
    private String email;
    private String role;
    private  String birth;
    private  boolean gender;

    static LoginVO single;

    private LoginVO() {
    }

    public static LoginVO getInstance(){
        if(single == null){
            single = new LoginVO();
        }

        return single;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }
}
