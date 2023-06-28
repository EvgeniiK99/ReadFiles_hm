package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GlossaryModel {
    @JsonProperty("project_id")
    private String projectId;
    private String name;
    private int code;
    private List<Links> links;
    private Creator creator;

    public static class Links {
        private String link;

        public String getLink() {
            return link;
        }
    }

    public static class Creator {
        private String id, name, surname;
        private boolean human;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getSurname() {
            return surname;
        }

        public boolean getHuman() {
            return human;
        }
    }

    public String getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public List<Links> getLinks() {
        return links;
    }

    public Creator getCreator() {
        return creator;
    }
}
