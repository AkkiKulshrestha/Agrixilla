package agrixilla.in.models;

public class SalesUploadModel {
    public SalesUploadModel(String Date, String FileName, String FileExtension, String FileLink) {
        this.Date = Date;
        this.FileName = FileName;
        this.FileExtension = FileExtension;
        this.FileLink= FileLink;


    }

    private String Date;
    private String FileName;
    private String FileExtension;
    private String FileLink;

    public String getFileLink() {
        return FileLink;
    }

    public void setFileLink(String fileLink) {
        FileLink = fileLink;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileExtension() {
        return FileExtension;
    }

    public void setFileExtension(String fileExtension) {
        FileExtension = fileExtension;
    }





}
