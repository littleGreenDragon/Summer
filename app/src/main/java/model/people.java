package model;

import com.example.summer.R;

import java.io.Serializable;

public class people implements Serializable {
    private int head;
    private String name;
    private String sign;
    private String Email;
    private boolean isMember;
    private String sex;
    public people()
    {
        head= R.drawable.head;
        name="Demo";
        sign="没有签名，一点意思都没有";
        Email="nothing";
        isMember=false;
        sex="man";
    }

    public int getHead() {
        return head;
    }

    public String getName() {
        return name;
    }

    public String getSign() {
        return sign;
    }

    public String getEmail() {
        return Email;
    }

    public String getSex() {
        return sex;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setMember(boolean member) {
        isMember = member;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


}
