import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;

    public String description;

    // Construtor
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }
}