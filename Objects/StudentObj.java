package Objects;

import java.sql.SQLException;

public class StudentObj extends Operations {
    public StudentObj() throws SQLException {
        createIfNotCreated();
    }
}
