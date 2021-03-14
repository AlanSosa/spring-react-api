package net.alansosa.backendcursojava.models.responses;

/*
This model is to return a response to the client when an operation was performed AND is not necessarily
needed return details of the object that the operation performed in it.
For example when deleting a post. We don't want to return details of the deleted post.
Besides the postService doesn't return anything when post is deleted.
*/
public class OperationStatusModel {
    //Name of the operation performed
    private String name;

    //Result from the operation.
    private String result;

    public OperationStatusModel() {
    }

    public OperationStatusModel(String name, String result) {
        this.name = name;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
