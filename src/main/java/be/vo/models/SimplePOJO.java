package be.vo.models;

public class SimplePOJO{
    private String firstName;
    private String lastName;

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName( String lastName ){
        this.lastName = lastName;
    }

    public void setFirstName( String firstName ){
        this.firstName = firstName;
    }
}
