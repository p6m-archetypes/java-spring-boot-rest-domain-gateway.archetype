package {{ root_package }}.core.dto;

public class Create{{ EntityName }}RequestDto {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
