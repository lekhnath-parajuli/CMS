package Objects;

public class KeyValue {
    private String Key;
    private String Value;


    public KeyValue(String key, String value) {
        this.Key = key;
        this.Value = value;
    }

    public String getValue() {
        return this.Value;
    }

    public String getKey() {
        return this.Key;
    }
}
