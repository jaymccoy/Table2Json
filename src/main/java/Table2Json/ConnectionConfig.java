package Table2Json;

public class ConnectionConfig {

    public String url;
    public String user;
    public String pass;

    public void ConnectionConfig(String url, String user, String pass) {
        this.url=url;
        this.user=user;
        this.pass=pass;
    }//end function

    @Override
    public String toString() {
        return "ConnectionConfig{"
                + "\"url\":" + getUrl()
                + "\"user\":" + getUser()
                + "\"pass\":" + getPass()
                + "}";
    }//end function

    public String getUrl() {
        return url;
    }//end function
    public void setUrl(String url) {
        this.url = url;
    }//end function

    public String getUser() {
        return user;
    }//end function
    public void setUser(String user) {
        this.user = user;
    }//end function

    public String getPass() {
//        return pass;
        return "************";
    }//end function
    public void setPass(String pass) {
        this.pass = pass;
    }//end function

}//end class