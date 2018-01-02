package be.lanssens.models;

public class FormBackup{
    private String base64Value;
    private String s3BucketUrl;
    private String fileName;

    public String getBase64Value(){
        return base64Value;
    }

    public void setBase64Value( String base64Value ){
        this.base64Value = base64Value;
    }

    public String getS3BucketUrl(){
        return s3BucketUrl;
    }

    public void setS3BucketUrl( String s3BucketUrl ){
        this.s3BucketUrl = s3BucketUrl;
    }

    public String getFileName(){
        return fileName;
    }

    public void setFileName( String fileName ){
        this.fileName = fileName;
    }
}
